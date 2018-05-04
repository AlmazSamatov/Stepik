package almaz.stepik

import almaz.stepik.DataClasses.Course
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import io.reactivex.Single

@Dao
interface CourseDao {

    @Query("SELECT * FROM course")
    fun getFavorites(): Single<List<Course>>

    @Query("SELECT * FROM course WHERE courseTitle = :name")
    fun getFavoritesByName(name: String): Single<List<Course>>

    @Insert
    fun insert(course: Course)

    @Delete
    fun delete(course: Course)
}