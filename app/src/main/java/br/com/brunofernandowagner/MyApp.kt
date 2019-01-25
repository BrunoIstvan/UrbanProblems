package br.com.brunofernandowagner

import android.app.Application
import br.com.brunofernandowagner.utils.AppCtx

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        AppCtx.getInstance().initialize(this)
    }

}