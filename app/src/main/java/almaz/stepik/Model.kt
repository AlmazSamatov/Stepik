package almaz.stepik

import almaz.stepik.API.RepositoryProvider
import almaz.stepik.DataClasses.Course
import android.content.Context
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class Model(){

    private val searchRepository = RepositoryProvider.getSearchRepository()

    fun loadCourses(query: String, adapter: CourseAdapter, page: Int = 1){

        searchRepository.searchCourses(query, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    adapter.setUserList(it.searchResults)
                }, {
                    it.printStackTrace()
                })
    }

    fun getFavorites(context: Context, adapter: CourseAdapter) {
        val db = RepositoryProvider.getFavoritesRepository(context)

        db?.getCourseDao()?.getFavorites()
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe({
                    adapter.setUserList(it)
                }, {
                    it.printStackTrace()
                })
    }

    fun getFavoritesByName(name: String, context: Context, adapter: CourseAdapter) {
        val db = RepositoryProvider.getFavoritesRepository(context)

        db?.getCourseDao()?.getFavoritesByName(name)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe({
                    adapter.setUserList(it)
                }, {
                    it.printStackTrace()
                })
    }

    fun addToFavorite(context: Context, course: Course){
        val db = RepositoryProvider.getFavoritesRepository(context)

        Observable.fromCallable { db?.getCourseDao()?.insert(course) }
                .subscribeOn(Schedulers.io())
    }
}