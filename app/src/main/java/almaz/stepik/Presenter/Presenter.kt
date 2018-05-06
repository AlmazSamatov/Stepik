package almaz.stepik.Presenter

import almaz.stepik.DataClasses.Course
import almaz.stepik.DataClasses.Mode
import almaz.stepik.Model.Model
import almaz.stepik.View.View
import com.jakewharton.rxbinding.widget.RxTextView
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class Presenter(private val view: View): PresenterInterface {

    val model = Model()
    private var subscriptionToSearchInCourses: Subscription? = null
    private var subscriptionToSearchInFavorites: Subscription? = null
    private var mode = Mode.COURSES
    private var courseListFromWeb: MutableList<Course> = mutableListOf()
    private var searchInCourses = ""
    private var searchInFavorites = ""


    override fun initialize() {
        loadCourses(searchInCourses)
        subscribeToCourses()
    }

    private fun subscribeToCourses(){
        subscriptionToSearchInCourses = RxTextView.textChanges(view.getCoursesSearch())
                .filter { it.isNotEmpty() }
                .skip(1)
                .debounce(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe({ s -> loadCourses(s.toString().trim()) })
    }

    private fun setEmptyList() {
        view.getAdapter().setUserList(mutableListOf())
    }

    override fun subscribeToFavorites(){
        setEmptyList()
        subscriptionToSearchInFavorites = RxTextView.textChanges(view.getCoursesSearch())
                .filter { it.isNotEmpty() }
                .skip(1)
                .debounce(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe({ s -> view.getAdapter().filter.filter(s.toString().trim())})
    }

    override fun changeToSearchView() {
        if(mode == Mode.FAVORITES){
            disposeFromDB()
            unsubscribe(subscriptionToSearchInFavorites)
            saveFavoritesState()
            if(courseListFromWeb.size == 0)
                loadCourses(searchInCourses)
            else
                restoreCoursesState()
            subscribeToCourses()
        }
        mode = Mode.COURSES
    }

    override fun changeToFavoritesView() {
        if(mode == Mode.COURSES){
            saveCoursesState()
            disposeFromWeb()
            unsubscribe(subscriptionToSearchInCourses)
            loadFavoritesFromDB()
            subscribeToFavorites()
            restoreFavoritesState()
        }
        mode = Mode.FAVORITES
    }

    private fun unsubscribe(subscription: Subscription?){
        subscription?.unsubscribe()
    }

    private fun loadFavoritesFromDB(){
        view.showProgressBar()
        view.hideEmptyCourses()
        model.getFavorites(view.getContext(), view.getAdapter(), view::hideProgressBar, view::showEmptyCourses)
    }

    private fun loadCourses(query: String, page: Int = 1){
        view.showProgressBar()
        view.hideEmptyCourses()
        model.loadCourses(query, page, view.getContext(), view.getAdapter(), view::hideProgressBar,
                view::showEmptyCourses, true)
    }

    override fun addToFavorites(course: Course) {
        model.addToFavorite(view.getContext(), course)
    }

    override fun deleteFromFavorites(course: Course) {
        model.deleteCourse(view.getContext(), course)
    }

    private fun saveCoursesState(){
        searchInCourses = view.getCoursesSearch().text.toString()
        courseListFromWeb.clear()
        courseListFromWeb.addAll(view.getAdapter().getCourseList())
    }

    private fun restoreCoursesState(){
        view.getCoursesSearch().setText(searchInCourses)
        view.getAdapter().setUserList(courseListFromWeb)
    }

    private fun saveFavoritesState(){
        searchInFavorites = view.getCoursesSearch().text.toString()
    }

    private fun restoreFavoritesState(){
        view.getCoursesSearch().setText(searchInFavorites)
    }

    override fun onLoadMore() {
        view.showItemProgressBar()
        model.loadCourses(context = view.getContext(), adapter =  view.getAdapter(),
                hideProgressBar = view::hideItemProgressBar, isSet = false,
                showEmptyCourses = view::showEmptyCourses)
    }

    private fun disposeFromWeb(){
        model.disposeFromWeb()
        view.hideProgressBar()
        view.hideItemProgressBar()
        view.hideEmptyCourses()
    }

    private fun disposeFromDB(){
        model.disposeFromDB()
        view.hideEmptyCourses()
        view.hideProgressBar()
    }
}