package almaz.stepik.Database

import almaz.stepik.DataClasses.Course
import android.arch.persistence.room.*
import io.reactivex.Maybe

@Dao
interface CourseDao{

    @Query("SELECT * FROM course")
    fun getFavorites(): Maybe<MutableList<Course>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(course: Course): Long

    @Delete
    fun delete(course: Course): Int

}