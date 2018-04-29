package almaz.stepik.API

import almaz.stepik.DataClasses.Response
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface StepikAPI {

    @GET("search-results")
    fun search(@Query("query") query: String,
               @Query("page") page: Int): Observable<Response>

    companion object Factory {
        fun create(): StepikAPI {
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://stepik.org/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

            return retrofit.create(StepikAPI::class.java);
        }
    }

}