package almaz.stepik.Presenter

import almaz.stepik.DataClasses.Course

interface PresenterInterface {

    fun initialize()

    fun subscribeToFavorites()

    fun changeToSearchView()

    fun changeToFavoritesView()

    fun onLoadMore()

    fun addToFavorites(course: Course)

    fun deleteFromFavorites(course: Course)
}