package br.com.urbanproblems.views.signin

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.urbanproblems.R
import br.com.urbanproblems.models.ResponseStatus
import br.com.urbanproblems.models.User
import br.com.urbanproblems.repositories.UserRepository
import br.com.urbanproblems.utils.AppCtx
import br.com.urbanproblems.utils.EncUtil

class SignInViewModel : ViewModel() {

    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val responseStatusLiveData: MutableLiveData<ResponseStatus> = MutableLiveData()
    val userLiveData: MutableLiveData<User> = MutableLiveData()
    private val userRepository = UserRepository()

    fun signIn(pEmail: String, pPassword: String) {

        val ctx = AppCtx.getInstance().ctx!!

        if(pEmail.isEmpty()) {
            responseStatusLiveData.value = ResponseStatus(false, ctx.getString(R.string.message_input_email))
            return
        }

        if(pPassword.isEmpty()) {
            responseStatusLiveData.value = ResponseStatus(false, ctx.getString(R.string.message_input_password))
            return
        }

        loadingLiveData.value = true

        var encPassword = EncUtil.encrypt(pPassword)
        encPassword = encPassword.replace("\n", "/n")

        val user = User(id = null,
                        email = pEmail,
                        password = encPassword)

        userRepository.signIn(user,
            onComplete = {

                loadingLiveData.value = false
                userLiveData.value = it
                responseStatusLiveData.value = ResponseStatus(true,
                    AppCtx.getInstance().ctx!!.getString(R.string.message_signin_success))

            },
            onError = {

                loadingLiveData.value = false
                responseStatusLiveData.value = ResponseStatus(false, it!!)

            })

    }

    fun getUserByUid(uid: String) {

        loadingLiveData.value = true

        userRepository.getUserByUid(uid,
            onComplete = { user ->

                user?.let {
                    userLiveData.value = it
                    responseStatusLiveData.value = ResponseStatus(true,
                        AppCtx.getInstance().ctx!!.getString(R.string.message_hi_again))
                } ?: run {
                    responseStatusLiveData.value = ResponseStatus(false,
                        AppCtx.getInstance().ctx!!.getString(R.string.error_get_by_id))
                }

                loadingLiveData.value = false

            },
            onError = {

                loadingLiveData.value = false
                responseStatusLiveData.value = ResponseStatus(false, it!!)

            })

    }

}