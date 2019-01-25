package br.com.brunofernandowagner.apis

import br.com.brunofernandowagner.models.User
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface UserAPI {

    @POST("/api/v1/user/signup")
    fun signUp(@Body user: User) : Call<User>


    @POST("/api/v1/user/signin")
    fun signIn(@Body user: User) : Call<User>


    @POST("/api/v1/user/")
    fun save(@Body user: User) : Call<User>


    @GET("/api/v1/user/{id}")
    fun getById(@Path("id") id: String) : Call<User>


    @GET("/api/v1/user/validate")
    fun getByEmail(@Query("email") email: String) : Call<User>


}