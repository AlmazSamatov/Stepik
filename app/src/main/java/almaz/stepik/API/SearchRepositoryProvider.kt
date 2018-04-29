package almaz.stepik.API

object SearchRepositoryProvider {
    fun getSearchRepository(): SearchRepository {
        return SearchRepository(StepikAPI.create())
    }
}