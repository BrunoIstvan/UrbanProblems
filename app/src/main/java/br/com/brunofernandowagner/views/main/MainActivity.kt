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
import br.com.brunofernandowagner.MyApp
import br.com.brunofernandowagner.R
import br.com.brunofernandowagner.extensions.hideDialog
import br.com.brunofernandowagner.extensions.showActionDialog
import br.com.brunofernandowagner.models.Problem
import br.com.brunofernandowagner.models.User
import br.com.brunofernandowagner.views.about.AboutActivity
import br.com.brunofernandowagner.views.maps.MapsActivity
import br.com.brunofernandowagner.views.myprofile.MyProfileActivity
import br.com.brunofernandowagner.views.saveproblem.SaveProblemActivity
import br.com.brunofernandowagner.views.signin.SignInActivity
import br.com.brunofernandowagner.views.useful_telephones.UsefulTelephonesActivity
import br.com.brunofernandowagner.views.viewproblem.ViewProblemActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var problems : ArrayList<Problem> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        // inicializa o click do Fab Button
        initializeFabButton()

        // inicializa o comportamento do navigationview
        initializeNavigationView()


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
        if(MyApp.user == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        } else {
            fillUserData(MyApp.user!!)
            initializeListProblemsViewModel()
        }

    }

    private fun listAll(list: ArrayList<Problem>) {

        hideDialog()
        if(list != null) {

            problems = list

            // FUNCIONANDO
            rvProblems.adapter = MainAdapter(this, list) { problem ->
                val intent = Intent(this, ViewProblemActivity::class.java)
                MyApp.problemId = problem.id!!
                intent.putExtra("PROBLEM", problem)
                intent.putExtra("ORIGEM", "LISTA")
                startActivity(intent)
            }
            val layoutManager = LinearLayoutManager(this)
            //val gridLayoutManager = GridLayoutManager(this, 2)
            rvProblems.layoutManager = layoutManager
        }

    }

    private fun fillUserData(user: User) {

        nav_view.getHeaderView(0).tvFullname.text = user.name
        nav_view.getHeaderView(0).tvEmail.text = user.email
        if (!user.avatar.isEmpty()) {
            Glide.with(this).load(user.avatar).into(nav_view.getHeaderView(0).ivMyProfile)
        } else {
            nav_view.getHeaderView(0).ivMyProfile.setImageResource(R.drawable.fpo_avatar)
        }
        hideDialog()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra("PROBLEMS", problems as ArrayList<Problem>)
                startActivity(intent)
            }
            R.id.menu_utils_telephones -> startActivity(Intent(this, UsefulTelephonesActivity::class.java))
            R.id.menu_my_profile -> startActivity(Intent(this, MyProfileActivity::class.java))
            R.id.menu_logout -> confirmLogout()
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
            R.id.action_logout -> confirmLogout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmLogout() {

        showActionDialog(getString(R.string.message_confirmation),
            getString(R.string.message_ask_logout),
            onPositiveClick = { logout() })

    }

    private fun logout() {

        // prepara o serviço de shared preferences
        val sharedPreferences = getSharedPreferences("myapp", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("MANTER_CONECTADO", false)
        editor.remove("USUARIO")
        editor.apply()
        MyApp.user = null

        startActivity(Intent(this, SignInActivity::class.java))
        finish()

    }

    private fun initializeNavigationView() {
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun initializeFabButton() {
        fab.setOnClickListener { view ->
            val intent = Intent(this, SaveProblemActivity::class.java)
            intent.putExtra("USER", MyApp.user!!.id!!)
            startActivity(intent)
        }
    }

    private fun initializeListProblemsViewModel() {
        ViewModelProviders
            .of(this)
            .get(ListProblemViewModel::class.java)
            .problems
            .observe(this, Observer<List<Problem>> { list ->
                listAll(ArrayList(list!!))
            })
    }

}
