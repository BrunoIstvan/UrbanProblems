package br.com.brunofernandowagner.views.viewproblem

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.com.brunofernandowagner.MyApp
import br.com.brunofernandowagner.R
import br.com.brunofernandowagner.extensions.hideDialog
import br.com.brunofernandowagner.extensions.showActionDialog
import br.com.brunofernandowagner.extensions.showDialog
import br.com.brunofernandowagner.extensions.showLongToast
import br.com.brunofernandowagner.models.Problem
import br.com.brunofernandowagner.views.saveproblem.SaveProblemActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_view_problem.*

class ViewProblemActivity : AppCompatActivity() {

    private lateinit var problem: Problem
    private lateinit var userId: String
    private lateinit var origem: String
    private lateinit var viewProblemViewModel: ViewProblemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_problem)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewProblemViewModel = ViewModelProviders.of(this).get(ViewProblemViewModel::class.java)
        viewProblemViewModel.loadingLiveData.observe(this, Observer {
            if(it == true) {
                showDialog()
            } else {
                hideDialog()
            }
        })
        viewProblemViewModel.responseDeleteLiveData.observe(this, Observer {
            if (it!!.success) {
                showLongToast(it.message)
                finish()
            }
        })

    }

    override fun onStart() {
        super.onStart()

        if (intent.hasExtra("PROBLEM")) {
            problem = intent.getParcelableExtra("PROBLEM")
        }

        if(intent.hasExtra("ORIGEM")) {
            origem = intent.getStringExtra("ORIGEM")
        } else {
            origem = ""
        }
        if(origem == "LISTA") {
            fillProblemData(problem)
            intent.removeExtra("ORIGEM")
        } else {

            MyApp.problemId = problem.id!!
            ViewModelProviders.of(this)
                .get(ViewProblemViewModel::class.java)
                .problem
                .observe(this,
                    Observer<Problem> {
                        if(it == null) {
                            finish()
                        } else {
                            fillProblemData(it!!)
                        }
                    })

        }

        if(intent.hasExtra("USER")) {
            userId = intent.getStringExtra("USER")
        }

    }

    private fun fillProblemData(problem: Problem) {

        tvTitle.text = problem.title
        tvDetail.text = problem.detail
        if(!problem.photo.isNullOrEmpty()) {
            Glide.with(this).load(problem.photo).into(ivProblem)
        } else {
            Glide.with(this).load(R.drawable.no_cover_available).into(ivProblem)
        }
        var completeAddress: String = ""
        if(!problem.address.isNullOrEmpty()) {
            completeAddress += "${getString(R.string.label_address)}: ${problem.address}\n"
        }
        if(!problem.addressNumber.isNullOrEmpty()) {
            completeAddress += "Nº.: ${problem.addressNumber}\n"
        }
        if(!problem.neighborhood.isNullOrEmpty()) {
            completeAddress += "${getString(R.string.label_neighborhood)}: ${problem.neighborhood}\n"
        }
        if(!problem.city.isNullOrEmpty() ) {
            completeAddress += "${getString(R.string.label_city)}: ${problem.city}\n"
        }
        if(!problem.state.isNullOrEmpty()) {
            completeAddress += "${getString(R.string.label_state)}: ${problem.state}\n"
        }
        if(!problem.postalCode.isNullOrEmpty()) {
            completeAddress += "${getString(R.string.label_postalcode)}: ${problem.postalCode}\n"
        }
        tvAddress.text = completeAddress
        if(!completeAddress.isNullOrEmpty()) {
            tvAddress.visibility = View.VISIBLE
            tvEmpty.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.view_problem_navigation, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_edit -> edit()
            R.id.action_delete -> confirmDelete()
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun edit() {

        val intent = Intent(this, SaveProblemActivity::class.java)
        intent.putExtra("PROBLEM", problem)
        startActivity(intent)

    }

    private fun confirmDelete() {

        showActionDialog(getString(R.string.message_confirmation),
            getString(R.string.message_confirm_delete_problem),
            onPositiveClick = { delete() })

    }

    private fun delete() {

        viewProblemViewModel.deleteProblem(problem)

    }

}
