package br.com.brunofernandowagner

import android.app.Application
import br.com.brunofernandowagner.models.User
import br.com.brunofernandowagner.utils.AppCtx


//import com.google.firebase.FirebaseApp

class MyApp : Application() {

    companion object {
        var problemId: Int = 0
        var user: User? = null
    }

    override fun onCreate() {
        super.onCreate()
        //FirebaseApp.initializeApp(this)
        AppCtx.getInstance().initialize(this)

    }

}