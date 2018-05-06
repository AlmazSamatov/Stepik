package almaz.stepik.Model

import almaz.stepik.API.RepositoryProvider
import almaz.stepik.DataClasses.Course
import almaz.stepik.DataClasses.Response
import almaz.stepik.RecyclerView.CourseAdapter
import android.content.Context
import io.reactivex.Notification
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlin.concurrent.thread

class Model(){

    private val searchRepository = RepositoryProvider.getSearchRepository()
    private var subscriptionToWeb: Disposable? = null
    private var subscriptionToDB: Disposable? = null
    private var lastQuery = ""
    private var lastPage = 0
    private var hasNext = false

    fun loadCourses(query: String = "", page: Int = 1, context: Context, adapter: CourseAdapter,
                    hideProgressBar: () -> Unit, showEmptyCourses: () -> Unit, isSet: Boolean) {
        val db = RepositoryProvider.getFavoritesRepository(context)

        if(isSet){
            lastQuery = query
            lastPage = page
        }

        if(!isSet && hasNext || isSet){

            disposeFromWeb()
            disposeFromDB()

            val webQuery =  if(isSet) searchRepository.searchCourses(query, page)
                                        .subscribeOn(Schedulers.newThread())
                            else
                                searchRepository.searchCourses(lastQuery, lastPage + 1)
                                        .subscribeOn(Schedulers.newThread())

            val dbQuery = db?.getCourseDao()?.getFavorites()
                    ?.subscribeOn(Schedulers.newThread())
                    ?.toObservable()
                    ?.materialize()

            subscriptionToWeb = Observable.zip(webQuery, dbQuery,
                    BiFunction<Response, Notification<MutableList<Course>>, Response>
                    { t1, t2 ->
                        if(t2.isOnNext){
                            for(course in t1.searchResults){
                                for(favorite in t2.value!!){
                                    if(course.id == favorite.id){
                                        course.isFavorite = true
                                        break
                                    }
                                }
                            }
                        }
                        return@BiFunction t1
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        db?.close()
                        hasNext = it.meta.hasNext
                        val courses = it.searchResults.filter { course -> course.courseTitle != null } as MutableList<Course>
                        if(isSet) {
                            adapter.setUserList(courses)
                            if(courses.size == 0)
                                showEmptyCourses()
                        } else {
                            adapter.addUserList(courses)
                            lastPage++
                        }
                        hideProgressBar()
                    }, {
                        db?.close()
                        hideProgressBar()
                        if(isSet)
                            showEmptyCourses()
                        it.printStackTrace()
                    })
        } else{
            hideProgressBar()
        }
    }

    fun getFavorites(context: Context, adapter: CourseAdapter, hideProgressBar: () -> Unit, showEmptyCourses: () -> Unit) {
        val db = RepositoryProvider.getFavoritesRepository(context)

        disposeFromDB()

        subscriptionToDB = db?.getCourseDao()?.getFavorites()
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe({
                    db.close()
                    adapter.setUserList(it)
                    hideProgressBar()
                    if(it.size == 0)
                        showEmptyCourses()
                }, {
                    db.close()
                    hideProgressBar()
                    showEmptyCourses()
                    it.printStackTrace()
                })
    }

    fun addToFavorite(context: Context, course: Course){
        val db = RepositoryProvider.getFavoritesRepository(context)

        course.isFavorite = true
        thread(start = true) { db?.getCourseDao()?.insert(course); db?.close() }
    }

    fun deleteCourse(context: Context, course: Course){
        val db = RepositoryProvider.getFavoritesRepository(context)

        thread(start = true) { db?.getCourseDao()?.delete(course); db?.close() }
    }

    fun disposeFromWeb(){
        subscriptionToWeb?.dispose()
    }

    fun disposeFromDB() {
        subscriptionToDB?.dispose()
    }

}