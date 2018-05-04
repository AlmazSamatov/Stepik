package almaz.stepik.API

import almaz.stepik.AppDatabase
import android.arch.persistence.room.Room
import android.content.Context

object RepositoryProvider {

    private val apiService = StepikAPI.create()
    private val searchRepository = SearchRepository(apiService)
    private var db: AppDatabase? = null

    fun getSearchRepository(): SearchRepository {
        return searchRepository
    }

    fun getFavoritesRepository(context: Context): AppDatabase? {
        if(db == null)
            db = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "favorites").build()
        return db
    }
}