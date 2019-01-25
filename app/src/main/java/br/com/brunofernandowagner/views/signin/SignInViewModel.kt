package br.com.brunofernandowagner.views.signin

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.brunofernandowagner.R
import br.com.brunofernandowagner.models.ResponseStatus
import br.com.brunofernandowagner.models.User
import br.com.brunofernandowagner.repositories.UserRepository
import br.com.brunofernandowagner.utils.AppCtx

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

        val user = User(id = null, email = pEmail, password = pPassword)

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
            onComplete = {

                loadingLiveData.value = false

                if(it?.id == null) {

                    responseStatusLiveData.value = ResponseStatus(false,
                        AppCtx.getInstance().ctx!!.getString(R.string.error_get_by_id))

                } else {

                    userLiveData.value = it
                    responseStatusLiveData.value = ResponseStatus(true,
                        AppCtx.getInstance().ctx!!.getString(R.string.message_hi_again))

                }

            },
            onError = {

                loadingLiveData.value = false
                responseStatusLiveData.value = ResponseStatus(false, it!!)

            })

    }

}