package almaz.stepik

import almaz.stepik.DataClasses.Course
import android.content.Context
import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity() {

    private val presenter = Presenter(this)

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
    }

    fun getCoursesSearch(): EditText{
        return courses_search
    }

    fun getAdapter(): CourseAdapter{
        return courses_recycler_view.adapter as CourseAdapter
    }

    fun getContext(): Context{
        return applicationContext
    }

    fun onFavoriteClickListener(img: ImageView, course: Course){
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