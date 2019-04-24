package br.com.urbanproblems.views.saveproblem

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.urbanproblems.R
import br.com.urbanproblems.models.GeoLocation
import br.com.urbanproblems.models.Problem
import br.com.urbanproblems.models.ResponseStatus
import br.com.urbanproblems.persistences.DatabaseProblem
import br.com.urbanproblems.utils.AppCtx
import com.google.android.gms.maps.model.LatLng
import java.util.concurrent.Executors

class SaveProblemViewModel : ViewModel() {

    val responseStatusLiveData : MutableLiveData<ResponseStatus> = MutableLiveData()
    val updateResponseStatusLiveData : MutableLiveData<ResponseStatus> = MutableLiveData()
    val loadingLiveData : MutableLiveData<Boolean> = MutableLiveData()
    val problemLiveData: MutableLiveData<Problem> = MutableLiveData()
    val geoLocationLiveData: MutableLiveData<GeoLocation> = MutableLiveData()
    val latLngLiveData: MutableLiveData<LatLng> = MutableLiveData()
    val imagePathLiveData : MutableLiveData<String> = MutableLiveData()

    private lateinit var db: DatabaseProblem

    fun saveProblem(problem: Problem) {

        val ctx = AppCtx.getInstance().ctx!!

        if (problem.title.isNullOrEmpty()) {
            updateResponseStatusLiveData.value = ResponseStatus(false,
                ctx.getString(R.string.message_input_title))
            return
        }

        if(problem.photo.isNullOrEmpty()) {
            updateResponseStatusLiveData.value = ResponseStatus(false,
                ctx.getString(R.string.message_take_picture))
            return
        }

        loadingLiveData.value = true

        db = DatabaseProblem.getDatabase(AppCtx.getInstance().ctx!!)!!

        problem.id?.let {
            Executors.newSingleThreadExecutor().execute {
                db.problemDAO().update(problem)
            }
            /*
            UpdateAsyncTask(db).execute(problem)
            */
            loadingLiveData.value = false
            updateResponseStatusLiveData.value = ResponseStatus(true,
                AppCtx.getInstance().ctx!!.getString(R.string.message_save_problem_success))
        } ?: run {
            Executors.newSingleThreadExecutor().execute {
                db.problemDAO().insert(problem)
            }
            /*
            InsertAsyncTask(db).execute(problem)
            */
            loadingLiveData.value = false
            updateResponseStatusLiveData.value = ResponseStatus(true,
                AppCtx.getInstance().ctx!!.getString(R.string.message_save_problem_success))
        }

    }

    fun setProblem(problem: Problem) { this.problemLiveData.value = problem }

    fun setLatLng(latLngLiveData: LatLng) { this.latLngLiveData.value = latLngLiveData }

    fun setImagePath(imagePath: String) { this.imagePathLiveData.value = imagePath }

    /*
    companion object {

        private class InsertAsyncTask internal
        constructor(appDatabase: DatabaseProblem) : AsyncTask<Problem, Void, String>() {

            private val db: DatabaseProblem = appDatabase

            override fun doInBackground(vararg params: Problem?): String {
                db.problemDAO().insert(params[0]!!)
                return ""
            }

        }

        private class UpdateAsyncTask internal
        constructor(appDatabase: DatabaseProblem) : AsyncTask<Problem, Void, String>() {

            private val db: DatabaseProblem = appDatabase

            override fun doInBackground(vararg params: Problem?): String {
                db.problemDAO().update(params[0]!!)
                return ""
            }

        }
    }
    */

}