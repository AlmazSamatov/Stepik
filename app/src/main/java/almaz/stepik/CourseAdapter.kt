package almaz.stepik

import almaz.stepik.DataClasses.Course
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.course_item.view.*

class CourseAdapter(val courseList: List<Course>): RecyclerView.Adapter<CourseAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return courseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(courseList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.course_item, parent, false)
        return ViewHolder(v)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItems(course: Course) {
            itemView.course_name.text = course.courseTitle
            itemView.course_cover.setImageDrawable(course.image)
        }
    }

}


