package br.com.brunofernandowagner.views.signin

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.brunofernandowagner.R
import br.com.brunofernandowagner.extensions.hideDialog
import br.com.brunofernandowagner.extensions.showDialog
import br.com.brunofernandowagner.extensions.showLongSnack
import br.com.brunofernandowagner.extensions.showLongToast
import br.com.brunofernandowagner.models.ResponseStatus
import br.com.brunofernandowagner.models.User
import br.com.brunofernandowagner.views.main.MainActivity
import br.com.brunofernandowagner.views.signup.SignUpActivity
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    private lateinit var signInViewModel: SignInViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        supportActionBar?.title = getString(R.string.title_signin)

        signInViewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
        signInViewModel.loadingLiveData.observe(this, loadingObserver)
        signInViewModel.responseStatusLiveData.observe(this, responseStatusObserver)
        signInViewModel.userLiveData.observe(this, userObserver)

        // prepara o serviço de shared preferences
        sharedPreferences = getSharedPreferences("myapp", Context.MODE_PRIVATE)

        // se foi armazenado anteriormente para manter conectado...
        if(sharedPreferences.getBoolean("MANTER_CONECTADO", false)) {

            chkKeepConnected.isChecked = true

            // verificar se o id do usuário está preenchido
            if(sharedPreferences.getString("USUARIO", "").isNotEmpty()) {

                // recuperar
                val uid = sharedPreferences.getString("USUARIO", "")

                // executar a busca pelo usuário que quer se manter conectado...
                signInViewModel.getUserByUid(uid)

            }

        }

        // configurar o click do botão para ir até a tela de cadastro
        btnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // configurar o click do botão Entrar
        btnSignIn.setOnClickListener {
            signInViewModel.signIn(edtEmail.editText?.text.toString(),
                edtPassword.editText?.text.toString())

        }

    }


    private var userObserver = Observer<User> {

        if(it != null) {

            val editor = sharedPreferences.edit()
            editor.putBoolean("MANTER_CONECTADO", chkKeepConnected.isChecked)
            editor.putString("USUARIO", it.id)
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USER", it)
            startActivity(intent)
            finish()

        }

    }


    private var responseStatusObserver = Observer<ResponseStatus> {

        if(it!!.success) {

            showLongToast(it.message)

        } else {

            showLongSnack(it.message)

        }

    }


    private var loadingObserver = Observer<Boolean> {

        if(it == true) {
            showDialog()
        } else {
            hideDialog()
        }

    }


}
