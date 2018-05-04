package almaz.stepik

import almaz.stepik.DataClasses.Course
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(Course::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getCourseDao(): CourseDao
}