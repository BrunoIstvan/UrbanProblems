package br.com.brunofernandowagner.views.saveproblem

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.AsyncTask
import br.com.brunofernandowagner.R
import br.com.brunofernandowagner.UploadFile
import br.com.brunofernandowagner.models.Problem
import br.com.brunofernandowagner.models.ResponseStatus
import br.com.brunofernandowagner.persistences.DatabaseProblem
import br.com.brunofernandowagner.utils.AppCtx

class SaveProblemViewModel : ViewModel() {

    val responseStatus : MutableLiveData<ResponseStatus> = MutableLiveData()
    val updateResponseStatus : MutableLiveData<ResponseStatus> = MutableLiveData()
    val loading : MutableLiveData<Boolean> = MutableLiveData()

    private lateinit var db: DatabaseProblem

    fun saveProblem(problem: Problem) {

        loading.value = true

        db = DatabaseProblem.getDatabase(AppCtx.getInstance().ctx!!)!!

        if(problem.id == null) {
            InsertAsyncTask(db!!).execute(problem)
            loading.value = false
            updateResponseStatus.value = ResponseStatus(true, AppCtx.getInstance().ctx!!.getString(R.string.message_save_problem_success))
        } else {
            UpdateAsyncTask(db!!).execute(problem)
            loading.value = false
            updateResponseStatus.value = ResponseStatus(true, AppCtx.getInstance().ctx!!.getString(R.string.message_save_problem_success))
        }


    }

    private inner class InsertAsyncTask internal
    constructor(appDatabase: DatabaseProblem) : AsyncTask<Problem, Void, String>() {

        private val db: DatabaseProblem = appDatabase

        override fun doInBackground(vararg params: Problem?): String {
            db.problemDAO().insert(params[0]!!)
            return ""
        }

    }

    private inner class UpdateAsyncTask internal
    constructor(appDatabase: DatabaseProblem) : AsyncTask<Problem, Void, String>() {

        private val db: DatabaseProblem = appDatabase

        override fun doInBackground(vararg params: Problem?): String {
            db.problemDAO().update(params[0]!!)
            return ""
        }

    }

}