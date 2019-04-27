package br.com.urbanproblems.views.splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import br.com.urbanproblems.R
import br.com.urbanproblems.views.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private val tempoaguardosplashscreen = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        //        val preferences = getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
        //        val isFirstOpen = preferences.getBoolean("open_first", true)
        //
        //        if (isFirstOpen) {
        //            markAppAlreadyOpen(preferences)
            showSplash()
        //        } else {
        //            showLogin()
        //        }
    }

    //    private fun markAppAlreadyOpen(preferences: SharedPreferences) {
    //        val editor = preferences.edit()
    //        editor.putBoolean("open_first", false)
    //        editor.apply()
    //    }

    private fun showLogin() {
        val nextScreen = Intent(this, SignInActivity::class.java)
        startActivity(nextScreen)
        finish()
    }

    private fun showSplash() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.animacao_splash)
        anim.reset()
        ivLogo.clearAnimation()
        ivLogo.startAnimation(anim)

        Handler().postDelayed({
            showLogin()
        }, tempoaguardosplashscreen)
    }
}
