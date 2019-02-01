package br.com.brunofernandowagner.views.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.AsyncTask
import br.com.brunofernandowagner.R
import br.com.brunofernandowagner.models.Problem
import br.com.brunofernandowagner.models.ResponseStatus
import br.com.brunofernandowagner.persistences.DatabaseProblem
import br.com.brunofernandowagner.utils.AppCtx

class MainViewModel : ViewModel() {

    val deleteResponseStatusLiveData: MutableLiveData<ResponseStatus> = MutableLiveData()
    private val db: DatabaseProblem = DatabaseProblem.getDatabase(AppCtx.getInstance().ctx!!)!!

    fun removeProblems(list: ArrayList<Problem>) {

        for(prob in list) {
            RemoveAsyncTask(db).execute(prob)
        }
        deleteResponseStatusLiveData.value = ResponseStatus(true,
            AppCtx.getInstance().ctx!!.getString(R.string.message_delete_success))

    }

    companion object {

        private class RemoveAsyncTask internal
        constructor(appDatabase: DatabaseProblem) : AsyncTask<Problem, Void, String>() {
            private val db: DatabaseProblem = appDatabase
            override fun doInBackground(vararg params: Problem?): String {
                db.problemDAO().remove(params[0]!!)
                return ""
            }
        }
    }

}