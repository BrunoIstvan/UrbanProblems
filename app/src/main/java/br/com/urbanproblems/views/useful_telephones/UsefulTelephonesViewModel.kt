package br.com.urbanproblems.views.useful_telephones

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.urbanproblems.R
import br.com.urbanproblems.models.UsefulTelephone
import br.com.urbanproblems.utils.AppCtx

class UsefulTelephonesViewModel: ViewModel() {

    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val telephones: MutableLiveData<ArrayList<UsefulTelephone>> = MutableLiveData()

    fun listAll() {

        val ctx = AppCtx.getInstance().ctx!!

        loadingLiveData.value = true

        telephones.value = arrayListOf(
            UsefulTelephone(1, ctx.getString(R.string.useful_police), "190", R.drawable.ic_police),
            UsefulTelephone(2, ctx.getString(R.string.useful_ambulance), "192", R.drawable.ic_ambulance),
            UsefulTelephone(3, ctx.getString(R.string.useful_fire), "193", R.drawable.ic_fireman),
            UsefulTelephone(4, ctx.getString(R.string.useful_traffic), "1188", R.drawable.ic_traffic),
            UsefulTelephone(5, ctx.getString(R.string.useful_subway), "08007707722", R.drawable.ic_subway),
            UsefulTelephone(6, ctx.getString(R.string.useful_bus), "156", R.drawable.ic_bus)
        )

        loadingLiveData.value = false

    }


}