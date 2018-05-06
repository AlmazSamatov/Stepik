package almaz.stepik.API

import almaz.stepik.AppDatabase
import android.arch.persistence.room.Room
import android.content.Context

object RepositoryProvider {

    private val apiService = StepikAPI.create()
    private val searchRepository = SearchRepository(apiService)
    private var db: AppDatabase? = null
    private val dbName = "favorites"

    fun getSearchRepository(): SearchRepository {
        return searchRepository
    }

    @Synchronized fun getFavoritesRepository(context: Context): AppDatabase? {
        return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, dbName).build()
    }
}