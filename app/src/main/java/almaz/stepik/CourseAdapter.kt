package almaz.stepik

import almaz.stepik.DataClasses.Course
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.course_item.view.*
import java.util.*

class CourseAdapter: RecyclerView.Adapter<CourseAdapter.ViewHolder>() {

    var courseList = Collections.emptyList<Course>()

    fun setUserList(list: List<Course>){
        courseList = list;
        notifyDataSetChanged();
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(courseList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.course_item, parent, false)
        return ViewHolder(v)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItems(course: Course) {
            itemView.course_name.text = course.courseTitle
            loadImg(itemView.course_cover, course.courseCover)
        }

        fun loadImg(img: ImageView, url: String?){
            if(url != null)
                Picasso.get().load(url)
                        .resize(70, 70)
                        .centerCrop()
                        .into(img);
        }
    }



}


