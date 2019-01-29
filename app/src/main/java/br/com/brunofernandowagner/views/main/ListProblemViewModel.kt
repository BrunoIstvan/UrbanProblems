package br.com.brunofernandowagner.views.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import br.com.brunofernandowagner.MyApp
import br.com.brunofernandowagner.models.Problem
import br.com.brunofernandowagner.persistences.DatabaseProblem
import br.com.brunofernandowagner.utils.AppCtx

class ListProblemViewModel : ViewModel() {

    var problems: LiveData<List<Problem>>

    private val db: DatabaseProblem = DatabaseProblem.getDatabase(AppCtx.getInstance().ctx!!)!!

    init {
        problems = db.problemDAO().listAllByUser(MyApp.user!!.id!!)
    }

}