package br.com.brunofernandowagner.views.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.brunofernandowagner.R
import br.com.brunofernandowagner.models.Problem
import br.com.brunofernandowagner.models.ResponseStatus
import br.com.brunofernandowagner.models.User
import br.com.brunofernandowagner.repositories.ProblemsRepository
import br.com.brunofernandowagner.repositories.UserRepository
import br.com.brunofernandowagner.utils.AppCtx

class MainViewModel : ViewModel() {

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val userResponseStatus: MutableLiveData<ResponseStatus> = MutableLiveData()
    val listProblemsReponseStatus: MutableLiveData<ResponseStatus> = MutableLiveData()
    val user: MutableLiveData<User> = MutableLiveData()
    val problems: MutableLiveData<ArrayList<Problem>> = MutableLiveData()

    private val problemsRepository = ProblemsRepository()
    private val userRepository = UserRepository()

    fun getUserByUid(uid: String) {

        loading.value = true

        userRepository.getUserByUid(uid,
            onComplete = {

                loading.value = false

                if(it?.id == null) {

                    userResponseStatus.value = ResponseStatus(false,
                        AppCtx.getInstance().ctx!!.getString(R.string.error_get_by_id))

                } else {

                    user.value = it
                    userResponseStatus.value = ResponseStatus(true, "")

                }

            },
            onError = {

                loading.value = false
                userResponseStatus.value = ResponseStatus(false, it!!)

            })

    }

    fun listAllMyProblems(uid: String) {

        loading.value = true

        problemsRepository.listAllMyReportedProblems(uid,
            onComplete = {

                loading.value = false
                listProblemsReponseStatus.value = ResponseStatus(true, "")
                problems.value = it

            },
            onError = {

                loading.value = false
                listProblemsReponseStatus.value = ResponseStatus(false, it)

            })

    }

    /*
    fun logout() {

        FirebaseAuth.getInstance().signOut()

    }
    */

}