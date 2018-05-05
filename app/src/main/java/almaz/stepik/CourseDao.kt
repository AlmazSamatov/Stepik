package almaz.stepik

import almaz.stepik.DataClasses.Course
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import io.reactivex.Maybe

@Dao
interface CourseDao {

    @Query("SELECT * FROM course")
    fun getFavorites(): Maybe<MutableList<Course>>

    @Insert
    fun insert(course: Course): Long

    @Delete
    fun delete(course: Course): Int

}