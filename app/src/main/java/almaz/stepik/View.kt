package almaz.stepik

import android.content.Context
import android.widget.EditText

interface View {

    fun getCoursesSearch() : EditText

    fun getAdapter(): CourseAdapter

    fun getContext(): Context

    fun showProgressBar()

    fun hideProgressBar()

    fun hideItemProgressBar()

    fun showItemProgressBar()

    fun showEmptyCourses()

    fun hideEmptyCourses()
}