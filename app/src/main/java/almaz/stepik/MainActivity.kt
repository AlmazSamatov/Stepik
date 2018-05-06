package almaz.stepik

import almaz.stepik.DataClasses.Course
import android.content.Context
import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity(), almaz.stepik.View {

    private val presenter: PresenterInterface = Presenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
    }

    private fun initialize() {
        initializeRecyclerView()
        setBottomNavigationListener()
        presenter.initialize()
    }

    private fun setBottomNavigationListener(){
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.action_courses -> presenter.changeToSearchView()
                R.id.action_favorites -> presenter.changeToFavoritesView()
            }
            false
        }
    }

    private fun initializeRecyclerView(){
        courses_recycler_view.setHasFixedSize(true)
        courses_recycler_view.layoutManager = LinearLayoutManager(this)
        courses_recycler_view.adapter = CourseAdapter({ imageView: ImageView, course: Course -> onFavoriteClickListener(imageView, course)})
        courses_recycler_view.addOnScrollListener(object : EndlessRecyclerOnScrollListener(){
            override fun onLoadMore() {
                presenter.onLoadMore()
            }
        })
    }

    override fun getCoursesSearch(): EditText =  courses_search

    override fun getAdapter() = courses_recycler_view.adapter as CourseAdapter

    override fun getContext(): Context = applicationContext

    override fun showProgressBar(){
        courses.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar(){
        courses.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    override fun hideItemProgressBar(){
        item_progress_bar.visibility = View.GONE
    }

    override fun showItemProgressBar(){
        item_progress_bar.visibility = View.VISIBLE
    }

    override fun showEmptyCourses(){
        empty_courses.visibility = View.VISIBLE
    }

    override fun hideEmptyCourses(){
        empty_courses.visibility = View.GONE
    }

    private fun onFavoriteClickListener(img: ImageView, course: Course){
        img.setOnClickListener {
            if(course.isFavorite){
                img.setImageResource(R.drawable.ic_favorite_border_black_48dp)
                presenter.deleteFromFavorites(course)
                course.isFavorite = false
            } else{
                img.setImageResource(R.drawable.ic_favorite_black_48dp)
                presenter.addToFavorites(course)
                course.isFavorite = true
            }
            courses_recycler_view.adapter.notifyDataSetChanged()
        }
    }
}