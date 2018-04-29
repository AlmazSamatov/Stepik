package almaz.stepik

import almaz.stepik.API.SearchRepositoryProvider
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = SearchRepositoryProvider.getSearchRepository()

        repository.searchCourses("java")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    result ->
                    Log.d("Result", result.meta.toString())
                }, {
                    error -> error.printStackTrace()
                })


    }
}