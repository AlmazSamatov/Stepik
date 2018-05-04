package almaz.stepik

import com.jakewharton.rxbinding.widget.RxTextView
import rx.Subscription
import java.util.*
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
        view.getAdapter().setUserList(Collections.emptyList())
    }

    fun subscribeToFavorites(){
        setEmptyList()
        subscriptionToSearch = RxTextView.textChanges(view.getCoursesSearch())
                .filter { it.isNotEmpty() }
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe({ s -> model.getFavoritesByName(s.toString().trim(), view.getContext(),
                        view.getAdapter()) })
    }

    fun changeToSearchView() {
        if(mode == Mode.FAVORITES){
            unsubscribe()
            loadCourses("")
            subscribeToCourses()
        }
        mode = Mode.COURSES
    }

    fun changeToFavoritesView() {
        if(mode == Mode.COURSES){
            unsubscribe()
            loadFavoritesFromDB()
            subscribeToFavorites()
        }
        mode = Mode.FAVORITES
    }

    fun unsubscribe(){
        subscriptionToSearch?.unsubscribe()
    }

    fun loadFavoritesFromDB(){
        model.getFavorites(view.getContext(), view.getAdapter())
    }

    fun loadCourses(query: String){
        model.loadCourses(query, view.getAdapter())
    }

}