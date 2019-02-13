package br.com.brunofernandowagner.repositories

import br.com.brunofernandowagner.R
import br.com.brunofernandowagner.apis.getUserAPI
import br.com.brunofernandowagner.models.User
import br.com.brunofernandowagner.utils.AppCtx
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

    fun saveUserData(user: User,
                     onComplete: (User?) -> Unit,
                     onError: (String?) -> Unit) {

        getUserAPI()
            .save(user)
            .enqueue(object: Callback<User> {

                override fun onFailure(call: Call<User>, t: Throwable) {
                    onError(t.message)
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    if(response.isSuccessful) {
                        onComplete(response.body())
                    } else {
                        onError( AppCtx.getInstance().ctx?.getString(R.string.message_error_save_data) )
                    }

                }

            })

    }

    fun signUp(user: User,
               onComplete: (User?) -> Unit,
               onError: (String?) -> Unit) {

        getUserAPI()
            .signUp(user)
            .enqueue(object: Callback<User> {

                override fun onFailure(call: Call<User>, t: Throwable) {
                    onError(t.message)
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    if(response.isSuccessful) {
                        onComplete(response.body())
                    } else {
                        onError( AppCtx.getInstance().ctx?.getString(R.string.error_sign_up) )
                    }

                }

            })

    }

    fun signIn(user: User,
               onComplete: (User?) -> Unit,
               onError: (String?) -> Unit) {

        getUserAPI()
            .signIn(user)
            .enqueue(object: Callback<User> {

                override fun onFailure(call: Call<User>, t: Throwable) {
                    onError(t.message)
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    if(response.isSuccessful) {
                        onComplete(response.body())
                    } else {
                        onError( AppCtx.getInstance().ctx?.getString(R.string.error_sign_in) )
                    }

                }

            })

    }

    fun getUserByUid(uid: String,
                     onComplete : (User?) -> Unit,
                     onError : (String?) -> Unit) {

        getUserAPI()
            .getById(uid)
            .enqueue(object: Callback<User> {

                override fun onFailure(call: Call<User>, t: Throwable) {
                    onError(t.message)
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    if(response.isSuccessful) {
                        onComplete(response.body())
                    } else {
                        onError( AppCtx.getInstance().ctx?.getString(R.string.error_get_by_id) )
                    }

                }

            })

    }

    fun getUserByEmail(email: String,
                       onComplete : (User?) -> Unit,
                       onError : (String?) -> Unit) {

        getUserAPI()
            .getByEmail(email)
            .enqueue(object: Callback<User> {

                override fun onFailure(call: Call<User>, t: Throwable) {
                    onError(t.message)
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    if(response.isSuccessful) {
                        onComplete(response.body())
                    } else {
                        onComplete(User(id = ""))
                    }

                }

            })

    }

}