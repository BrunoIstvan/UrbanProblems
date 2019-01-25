package br.com.brunofernandowagner.utils

import android.content.Context

class AppCtx {

    var ctx: Context? = null
        private set

    fun initialize(context: Context) {
        this.ctx = context
    }

    companion object {
        private var mInstance: AppCtx? = null

        fun getInstance(): AppCtx {
            if (mInstance == null) mInstance = getSync()
            return mInstance as AppCtx
        }

        private fun getSync(): AppCtx {
            if (mInstance == null) mInstance = AppCtx()
            return mInstance as AppCtx
        }
    }

}