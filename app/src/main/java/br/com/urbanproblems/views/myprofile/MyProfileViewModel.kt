package br.com.urbanproblems.views.myprofile

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.urbanproblems.R
import br.com.urbanproblems.models.ResponseStatus
import br.com.urbanproblems.models.User
import br.com.urbanproblems.repositories.UserRepository
import br.com.urbanproblems.utils.AppCtx


class MyProfileViewModel : ViewModel() {

    val responseStatusLiveData : MutableLiveData<ResponseStatus> = MutableLiveData()
    val uploadResponseStatusLiveData : MutableLiveData<ResponseStatus> = MutableLiveData()
    val updateResponseStatusLiveData : MutableLiveData<ResponseStatus> = MutableLiveData()
    val loadingLiveData : MutableLiveData<Boolean> = MutableLiveData()

    private val userRepository = UserRepository()

    fun saveUserProfile(user: User) {

        loadingLiveData.value = true

        userRepository.saveUserData(user,
            onComplete = {
                loadingLiveData.value = false
                updateResponseStatusLiveData.value = ResponseStatus(true,
                    AppCtx.getInstance().ctx!!.getString(R.string.message_profile_updated_success))
            },
            onError = {
                loadingLiveData.value = false
                updateResponseStatusLiveData.value = ResponseStatus(false, it!!)
            })

    }

}