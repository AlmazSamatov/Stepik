package almaz.stepik

import almaz.stepik.DataClasses.Course
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.course_item.view.*
import java.util.*

class CourseAdapter(val favoriteClickListener: (ImageView, Course) -> Unit): RecyclerView.Adapter<CourseAdapter.ViewHolder>(), Filterable {

    private val courseList: MutableList<Course> = mutableListOf()
    private val courseListFiltered: MutableList<Course> = mutableListOf()

    private val filter = object: Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val query = p0.toString().toLowerCase()

            if(query.isEmpty()){
                courseListFiltered.clear()
                courseListFiltered.addAll(courseList)
            } else{
                courseListFiltered.clear()
                courseList.filterTo(courseListFiltered) {
                    it.courseTitle.toLowerCase().contains(query)
                }
            }

            val results = FilterResults()
            results.values = courseListFiltered
            return results
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            notifyDataSetChanged()
        }
    }

    fun setUserList(list: MutableList<Course>){
        courseList.clear()
        courseList.addAll(list)
        courseListFiltered.clear()
        courseListFiltered.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return courseListFiltered.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(courseListFiltered[position], favoriteClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.course_item, parent, false)
        return ViewHolder(v)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItems(course: Course, clickListener: (ImageView, Course) -> Unit) {
            itemView.course_name.text = course.courseTitle
            loadImg(itemView.course_cover, course.courseCover)
            clickListener(itemView.addToFavourites, course)
            if(course.isFavorite)
                itemView.addToFavourites.setImageResource(R.drawable.ic_favorite_black_48dp)
        }

        fun loadImg(img: ImageView, url: String?){
            if(url != null)
                Picasso.get().load(url)
                        .resize(70, 70)
                        .centerCrop()
                        .into(img);
        }
    }

    override fun getFilter(): Filter {
        return filter
    }

}


