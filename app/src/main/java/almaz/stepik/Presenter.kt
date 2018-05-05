package almaz.stepik

import almaz.stepik.DataClasses.Course
import com.jakewharton.rxbinding.widget.RxTextView
import rx.Subscription
import java.util.concurrent.TimeUnit

class Presenter(val view: MainActivity){

    val model = Model()
    private var subscriptionToSearch: Subscription? = null
    private var mode = Mode.COURSES

    fun initialize() {
        loadCourses("")
        subscribeToCourses()
    }

    private fun subscribeToCourses(){
        setEmptyList()
        subscriptionToSearch = RxTextView.textChanges(view.getCoursesSearch())
                .filter { it.isNotEmpty() }
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe({ s -> loadCourses(s.toString().trim()) })
    }

    private fun setEmptyList() {
        view.getAdapter().setUserList(mutableListOf())
    }

    fun subscribeToFavorites(){
        setEmptyList()
        subscriptionToSearch = RxTextView.textChanges(view.getCoursesSearch())
                .filter { it.isNotEmpty() }
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe({ s -> view.getAdapter().filter.filter(s.toString().trim())})
    }

    fun changeToSearchView() {
        if(mode == Mode.FAVORITES){
            unsubscribe()
            setSearchEmpty()
            loadCourses("")
            subscribeToCourses()
        }
        mode = Mode.COURSES
    }

    fun changeToFavoritesView() {
        if(mode == Mode.COURSES){
            unsubscribe()
            setSearchEmpty()
            loadFavoritesFromDB()
            subscribeToFavorites()
        }
        mode = Mode.FAVORITES
    }

    fun unsubscribe(){
        if(subscriptionToSearch?.isUnsubscribed == false)
            subscriptionToSearch?.unsubscribe()
    }

    fun setSearchEmpty(){
        view.getCoursesSearch().setText("")
    }

    fun loadFavoritesFromDB(){
        model.getFavorites(view.getContext(), view.getAdapter())
    }

    fun loadCourses(query: String, page: Int = 1){
        model.loadCourses(query, page, view.getContext(), view.getAdapter())
    }

    fun addToFavorites(course: Course) {
        model.addToFavorite(view.getContext(), course)
    }

    fun deleteFromFavorites(course: Course) {
        model.deleteCourse(view.getContext(), course)
    }

}