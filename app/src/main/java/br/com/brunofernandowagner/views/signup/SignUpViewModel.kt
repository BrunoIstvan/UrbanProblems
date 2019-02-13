package br.com.brunofernandowagner.views.signup

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.brunofernandowagner.R
import br.com.brunofernandowagner.models.ResponseStatus
import br.com.brunofernandowagner.models.User
import br.com.brunofernandowagner.repositories.UserRepository
import br.com.brunofernandowagner.utils.AppCtx
import br.com.brunofernandowagner.utils.EncUtil
import java.text.SimpleDateFormat
import java.util.*

class SignUpViewModel : ViewModel() {

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val authResponseStatus: MutableLiveData<ResponseStatus> = MutableLiveData()
    val saveUserResponseStatus: MutableLiveData<ResponseStatus> = MutableLiveData()

    private val userRepository = UserRepository()

    fun signUp(pFullName: String, pEmail: String, pPassword: String, pConfirmPassword: String) {

        val ctx = AppCtx.getInstance().ctx!!

        if(pFullName.isEmpty()) {
            authResponseStatus.value = ResponseStatus(false, ctx.getString(R.string.message_input_name))
            return
        }

        if(pEmail.isEmpty()) {
            authResponseStatus.value = ResponseStatus(false, ctx.getString(R.string.message_input_email))
            return
        }

        if(pPassword.isEmpty()) {
            authResponseStatus.value = ResponseStatus(false, ctx.getString(R.string.message_input_password))
            return
        }

        if(pConfirmPassword.isEmpty() || pConfirmPassword != pPassword) {
            authResponseStatus.value = ResponseStatus(false, ctx.getString(R.string.message_password_not_confirmed))
            return
        }

        if(pPassword.trim().length < 8) {
            authResponseStatus.value = ResponseStatus(false, ctx.getString(R.string.message_input_short_password))
            return
        }

        loading.value = true

        userRepository.getUserByEmail(pEmail,
            onComplete = {

                // se uid do usuário for nulo ou vazio é pq esse email ainda não está cadastrado...
                if(it?.id.isNullOrEmpty()) {

                    val vDateRegister = SimpleDateFormat("dd/MM/yyyy").format( Calendar.getInstance().time)

                    var encPassword = EncUtil.encrypt(pPassword)
                    encPassword = encPassword.replace("\n", "/n")

                    // preenche um novo usuário
                    val user = User(id = null,
                                    name = pFullName,
                                    email = pEmail,
                                    password = encPassword,
                                    dateRegister = vDateRegister )

                    // enviar para ser cadastrado
                    userRepository.signUp(user,
                        onComplete = { user ->

                            loading.value = false
                            authResponseStatus.value = ResponseStatus(true, user!!.id!!)

                        },
                        onError = { error ->

                            loading.value = false
                            authResponseStatus.value = ResponseStatus(false, error!!)

                        })

                } else {

                    loading.value = false
                    authResponseStatus.value = ResponseStatus(false,
                        AppCtx.getInstance().ctx!!.getString(R.string.message_user_already_exists))

                }

            },
            onError = {

                loading.value = false
                authResponseStatus.value = ResponseStatus(false, it!!)

            })

    }


//    fun saveUserData(user: User) {
//
//        val ctx = AppCtx.getInstance().ctx!!
//        loading.value = true
        /*
        FirebaseDatabase.getInstance().getReference("Users")
            .child(userLiveData.uid)
            .setValue(userLiveData)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    loadingLiveData.value = false
                    saveUserResponseStatusLiveData.value = ResponseStatus(true, ctx.getString(R.string.message_signup_success))
                } else {
                    loadingLiveData.value = false
                    saveUserResponseStatusLiveData.value = ResponseStatus(false, task!!.exception!!.message!!)
                }
            }
        */

//    }


}