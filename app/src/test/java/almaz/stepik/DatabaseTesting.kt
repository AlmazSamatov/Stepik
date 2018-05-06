package almaz.stepik

import almaz.stepik.DataClasses.Course
import almaz.stepik.Database.AppDatabase
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.junit.After

class DatabaseTesting{

    private var db: AppDatabase? = null
    private var course: Course? = null

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AppDatabase::class.java).build()
        course = Course("java", "https://stepik.org/media/cache/images/courses/187/cover/b5afd0216dbb45100bdcc6a607791cc2.png", 1)
    }

    @Test
    fun testAddingToFavorite(){
        db?.getCourseDao()?.insert(course!!)

        db?.getCourseDao()?.getFavorites()
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe({
                    assert(it.contains(course))
                }, {
                    it.printStackTrace()
                })
    }

    @After
    fun closeDb() {
        db?.close()
    }
}