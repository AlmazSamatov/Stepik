package almaz.stepik

import almaz.stepik.API.RepositoryProvider
import almaz.stepik.DataClasses.Course
import almaz.stepik.DataClasses.Response
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


    fun loadCourses(query: String, page: Int = 1, context: Context, adapter: CourseAdapter) {
        val db = RepositoryProvider.getFavoritesRepository(context)

        if(subscriptionToDB?.isDisposed == false)
            subscriptionToDB?.dispose()

        val webQuery = searchRepository.searchCourses(query, page)
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
                                if(course.course == favorite.course){
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
                    adapter.setUserList(it.searchResults)
                }, {
                    it.printStackTrace()
                })
    }

    fun getFavorites(context: Context, adapter: CourseAdapter) {
        val db = RepositoryProvider.getFavoritesRepository(context)

        if(subscriptionToDB?.isDisposed == false)
            subscriptionToDB?.dispose()

        subscriptionToDB = db?.getCourseDao()?.getFavorites()
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe({
                    db.close()
                    adapter.setUserList(it)
                }, {
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

}