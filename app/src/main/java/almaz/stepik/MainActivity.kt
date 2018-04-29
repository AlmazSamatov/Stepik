package almaz.stepik

import almaz.stepik.API.SearchRepositoryProvider
import almaz.stepik.DataClasses.Course
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val list = ArrayList<Course>()

        courses_recycler_view.layoutManager = LinearLayoutManager(this)
        courses_recycler_view.adapter = CourseAdapter(list)
        courses_recycler_view.setHasFixedSize(true)

        val repository = SearchRepositoryProvider.getSearchRepository()

        repository.searchCourses("java")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    result ->
                    list.addAll(result.searchResults)
                    courses_recycler_view.adapter.notifyDataSetChanged()
                }, {
                    error -> error.printStackTrace()
                })

    }
}