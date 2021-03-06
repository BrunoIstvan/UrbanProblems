package br.com.urbanproblems.views.main

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.support.v4.content.FileProvider
import android.util.Log
import br.com.urbanproblems.R
import br.com.urbanproblems.models.Problem
import br.com.urbanproblems.models.ResponseStatus
import br.com.urbanproblems.persistences.DatabaseProblem
import br.com.urbanproblems.utils.AppCtx
import java.io.File
import java.util.concurrent.Executors


class MainViewModel : ViewModel() {

    val deleteResponseStatusLiveData: MutableLiveData<ResponseStatus> = MutableLiveData()
    private val db: DatabaseProblem = DatabaseProblem.getDatabase(AppCtx.getInstance().ctx!!)!!

    fun removeProblems(list: ArrayList<Problem>) {

        for(prob in list) {
            Executors.newSingleThreadExecutor().execute {
                db.problemDAO().remove(prob)
            }
            //RemoveAsyncTask(db).execute(prob)
        }
        deleteResponseStatusLiveData.value = ResponseStatus(true,
            AppCtx.getInstance().ctx!!.getString(R.string.message_delete_success))

    }

    fun shareProblemByWhatsApp(activity: Activity, problem: Problem) {

        Intent(Intent.ACTION_SEND).apply {
            type = "*/*"
            setPackage("com.whatsapp")

            // recupera o título do problema
            var contentMessage = problem.title

            // verifica se tem algum detalhe gravado
            problem.detail?.let {
                contentMessage += "\n\n" + it
            }

            // recupera a latitude e longitude
            problem.longitude?.let { lon ->
                problem.latitude?.let { lat ->
                    contentMessage += "\n\nhttp://maps.google.com/maps?q=$lat,$lon&iwloc=A"
                }
            }

            // informa o conteúdo completo no corpo da mensagem
            putExtra(Intent.EXTRA_TEXT, contentMessage)

            problem.photo?.let {
                val photoURI = FileProvider.getUriForFile(
                    activity.applicationContext,
                    activity.applicationContext.packageName + ".br.com.urbanproblems",
                    File(problem.photo)
                )
                putExtra(Intent.EXTRA_STREAM, photoURI)
            }

            try {
                activity.startActivity(this)
            } catch (ex: android.content.ActivityNotFoundException) {
                Log.e("SHARE", ex.message)
            }
        }

    }

    fun deleteProblem(problem: Problem) {

        Executors.newSingleThreadExecutor().execute {
            db.problemDAO().remove(problem)
        }
        //DeleteAsyncTask(db!!).execute(problem)
        deleteResponseStatusLiveData.value = ResponseStatus(true,
            AppCtx.getInstance().ctx!!.getString(R.string.message_delete_success))

    }

    /*
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
    */

}