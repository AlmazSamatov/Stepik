package almaz.stepik

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.EditText
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
        bottom_navigation.setOnNavigationItemReselectedListener {
            when(it.itemId) {
                R.id.action_courses -> presenter.changeToSearchView()
                R.id.action_favorites -> presenter.changeToFavoritesView()
            }
        }
    }

    private fun initializeRecyclerView(){
        courses_recycler_view.layoutManager = LinearLayoutManager(this)
        courses_recycler_view.adapter = CourseAdapter()
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
}