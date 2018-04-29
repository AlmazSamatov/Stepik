package almaz.stepik.API

import almaz.stepik.DataClasses.Response
import io.reactivex.Observable

class SearchRepository(val api: StepikAPI){
    fun searchCourses(query: String, page: Int = 1): Observable<Response> {
        return api.search(query = query, page = page);
    }
}