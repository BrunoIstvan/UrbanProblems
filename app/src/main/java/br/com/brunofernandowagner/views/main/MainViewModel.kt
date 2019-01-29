package br.com.brunofernandowagner.views.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.brunofernandowagner.R
import br.com.brunofernandowagner.models.ResponseStatus
import br.com.brunofernandowagner.models.User
import br.com.brunofernandowagner.repositories.UserRepository
import br.com.brunofernandowagner.utils.AppCtx

class MainViewModel : ViewModel() {

    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val userResponseStatusLiveData: MutableLiveData<ResponseStatus> = MutableLiveData()
    val userLiveData: MutableLiveData<User> = MutableLiveData()

    private val userRepository = UserRepository()

    fun getUserByUid(uid: String) {
        loadingLiveData.value = true
        userRepository.getUserByUid(uid,
            onComplete = {
                loadingLiveData.value = false
                if(it?.id == null) {
                    userResponseStatusLiveData.value = ResponseStatus(false,
                        AppCtx.getInstance().ctx!!.getString(R.string.error_get_by_id))
                } else {
                    userLiveData.value = it
                    userResponseStatusLiveData.value = ResponseStatus(true, "")
                }
            },
            onError = {
                loadingLiveData.value = false
                userResponseStatusLiveData.value = ResponseStatus(false, it!!)
            })
    }

}