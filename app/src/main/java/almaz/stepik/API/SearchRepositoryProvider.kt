package almaz.stepik.API

object SearchRepositoryProvider {

    private val apiService: StepikAPI = StepikAPI.create()

    fun getSearchRepository(): SearchRepository {
        return SearchRepository(apiService)
    }
}