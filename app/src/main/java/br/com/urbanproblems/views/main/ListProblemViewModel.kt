package br.com.urbanproblems.views.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import br.com.urbanproblems.MyApp
import br.com.urbanproblems.models.Problem
import br.com.urbanproblems.persistences.DatabaseProblem
import br.com.urbanproblems.utils.AppCtx

class ListProblemViewModel : ViewModel() {

    var problems: LiveData<List<Problem>>

    private val db: DatabaseProblem = DatabaseProblem.getDatabase(AppCtx.getInstance().ctx!!)!!

    init {
        problems = db.problemDAO().listAllByUser(br.com.urbanproblems.MyApp.user!!.id!!)
    }

}