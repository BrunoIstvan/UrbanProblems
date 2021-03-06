package br.com.urbanproblems.views.viewproblem

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.urbanproblems.MyApp
import br.com.urbanproblems.R
import br.com.urbanproblems.models.Problem
import br.com.urbanproblems.models.ResponseStatus
import br.com.urbanproblems.persistences.DatabaseProblem
import br.com.urbanproblems.utils.AppCtx
import java.util.concurrent.Executors

class ViewProblemViewModel : ViewModel() {

    private val db = DatabaseProblem.getDatabase(AppCtx.getInstance().ctx!!)!!
    lateinit var problem: LiveData<Problem>
    var loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var responseDeleteLiveData: MutableLiveData<ResponseStatus> = MutableLiveData()


    init {
        getProblemById(br.com.urbanproblems.MyApp.problemId)
    }


    private fun getProblemById(id: Int) {

        problem = db.problemDAO().getProblemById(id)

    }

    fun deleteProblem(problem: Problem) {

        loadingLiveData.value = true
        Executors.newSingleThreadExecutor().execute {
            db.problemDAO().remove(problem)
        }
        //DeleteAsyncTask(db!!).execute(problem)
        responseDeleteLiveData.value = ResponseStatus(true,
            AppCtx.getInstance().ctx!!.getString(R.string.message_delete_success))
        loadingLiveData.value = false

    }

    /*
    private inner class DeleteAsyncTask internal
    constructor(appDatabase: DatabaseProblem) : AsyncTask<Problem, Void, String>() {
        private val db: DatabaseProblem = appDatabase
        override fun doInBackground(vararg params: Problem?): String {
            db.problemDAO().remove(params[0]!!)
            return ""
        }
    }
    */

}