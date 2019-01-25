package br.com.brunofernandowagner.views.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import br.com.brunofernandowagner.R
import br.com.brunofernandowagner.extensions.hideDialog
import br.com.brunofernandowagner.extensions.showDialog
import br.com.brunofernandowagner.extensions.showLongSnack
import br.com.brunofernandowagner.models.Problem
import br.com.brunofernandowagner.models.ResponseStatus
import br.com.brunofernandowagner.models.User
import br.com.brunofernandowagner.views.MyProfileActivity
import br.com.brunofernandowagner.views.about.AboutActivity
import br.com.brunofernandowagner.views.maps.MapsActivity
import br.com.brunofernandowagner.views.saveproblem.SaveProblemActivity
import br.com.brunofernandowagner.views.signin.SignInActivity
import br.com.brunofernandowagner.views.viewproblem.ViewProblemActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var user: User
    private lateinit var problems: ArrayList<Problem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.title_main)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.loading.observe(this, loadingObserver)
        mainViewModel.user.observe(this, userObserver)
        mainViewModel.userResponseStatus.observe(this, userResponseStatusObserver)
        mainViewModel.listProblemsReponseStatus.observe(this, listProblemsResponseStatusObserver)
        mainViewModel.problems.observe(this, listAllMyProblemsObserver)

        fab.setOnClickListener { view ->
            startActivity(Intent(this, SaveProblemActivity::class.java))
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        if(intent.hasExtra("USER")) {

            user = intent.getParcelableExtra("USER")
            fillUserData(user)
            // mainViewModel.getUserByUid(user.id)

        }

    }

    override fun onBackPressed() {

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {

            drawer_layout.closeDrawer(GravityCompat.START)

        } else {

            super.onBackPressed()

        }

    }

    override fun onStart() {

        super.onStart()

        hideDialog()
        if(!::user.isInitialized || user == null) {

            startActivity(Intent(this, SignInActivity::class.java))
            finish()

        } else {

            mainViewModel.getUserByUid(user.id!!)

        }

    }

    private var listAllMyProblemsObserver = Observer<ArrayList<Problem>> {

        hideDialog()
        problems = it!!
        rvProblems.adapter = MainAdapter(this, it!!) { problem ->
            val intent = Intent(this, ViewProblemActivity::class.java)
            intent.putExtra("PROBLEM", problem)
            startActivity(intent)
        }
        val layoutManager = LinearLayoutManager(this)
        //val gridLayoutManager = GridLayoutManager(this, 2)
        rvProblems.layoutManager = layoutManager

    }

    private var listProblemsResponseStatusObserver = Observer<ResponseStatus> {

        if(it!!.success) {

            hideDialog()

        } else {

            showLongSnack(it.message)

        }

    }

    private var userResponseStatusObserver = Observer<ResponseStatus> {

        if(it!!.success) {

            hideDialog()
            mainViewModel.listAllMyProblems(user.id!!)

        } else {

            showLongSnack(it!!.message)

        }
    }

    private var loadingObserver = Observer<Boolean> {
        if(it == true) {
            showDialog()
        } else {
            hideDialog()
        }
    }

    private var userObserver = Observer<User> {

        user = it!!
        fillUserData(it!!)

    }

    private fun fillUserData(user: User) {

        nav_view.getHeaderView(0).tvFullname.text = user.name
        nav_view.getHeaderView(0).tvEmail.text = user.email
        if (!user.avatar.isNullOrEmpty()) {
            Picasso.get().load(user.avatar).into(nav_view.getHeaderView(0).ivMyProfile)
        } else {
            nav_view.getHeaderView(0).ivMyProfile.setImageResource(R.drawable.fpo_avatar)
        }
        hideDialog()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra("PROBLEMS", problems)
                startActivity(intent)
            }
//            R.id.menu_configuracoes -> {
//                startActivity(Intent(this, SettingsActivity::class.java))
//            }
//            R.id.menu_conversas -> {
//                startActivity(Intent(this, MyChatsActivity::class.java))
//            }
//            R.id.menu_em_andamento -> {
//                startActivity(Intent(this, RunningExchangesActivity::class.java))
//            }
//            R.id.menu_finalizados -> {
//                startActivity(Intent(this, FinishedExchangesActivity::class.java))
//            }
            R.id.menu_my_profile -> {
                val intent = Intent(this, MyProfileActivity::class.java)
                intent.putExtra("USER", user)
                startActivity(intent)
            }
//            R.id.menu_meus_desejos -> {
//                startActivity(Intent(this, MyWishesActivity::class.java))
//            }
//            R.id.menu_meus_jogos -> {
//                startActivity(Intent(this, MyGamesActivity::class.java))
//            }
//            R.id.menu_oportunidades -> {
//                startActivity(Intent(this, OpportunitiesActivity::class.java))
//            }
            R.id.menu_logout -> logout()
            R.id.menu_about_app -> startActivity(Intent(this, AboutActivity::class.java))
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_navigation, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_logout -> logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {

        // prepara o serviço de shared preferences
        val sharedPreferences = getSharedPreferences("myapp", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("MANTER_CONECTADO", false)
        editor.remove("USUARIO")
        editor.apply()

        startActivity(Intent(this, SignInActivity::class.java))
        finish()

    }

    override fun onDestroy() {

        super.onDestroy()
//        val uid = MyApp.myUser.uid
//        mainViewModel.logout(uid!!)

    }

}
