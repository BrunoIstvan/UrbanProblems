package br.com.urbanproblems.views.signin

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.urbanproblems.MyApp
import br.com.urbanproblems.R
import br.com.urbanproblems.extensions.hideDialog
import br.com.urbanproblems.extensions.showDialog
import br.com.urbanproblems.extensions.showLongSnack
import br.com.urbanproblems.models.ResponseStatus
import br.com.urbanproblems.models.User
import br.com.urbanproblems.views.main.MainActivity
import br.com.urbanproblems.views.signup.SignUpActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    private lateinit var signInViewModel: SignInViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { }
                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>,
                                                                token: PermissionToken ) { }
            }).check()

        //supportActionBar?.title = getString(R.string.title_signin)

        signInViewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
        signInViewModel.loadingLiveData.observe(this, loadingObserver)
        signInViewModel.responseStatusLiveData.observe(this, responseStatusObserver)
        signInViewModel.userLiveData.observe(this, userObserver)

        // prepara o serviço de shared preferences
        sharedPreferences = getSharedPreferences("myapp", Context.MODE_PRIVATE)

        sharedPreferences?.let { shared ->

            // se foi armazenado anteriormente para manter conectado...
            if(shared.getBoolean("MANTER_CONECTADO", false)) {

                chkKeepConnected.isChecked = true

                // verificar se o id do usuário está preenchido
                if(shared.getString("USUARIO", "").isNotEmpty()) {

                    // recuperar
                    val uid = sharedPreferences.getString("USUARIO", "")

                    // executar a busca pelo usuário que quer se manter conectado...
                    signInViewModel.getUserByUid(uid!!)

                }

            }
        }

        // configurar o click do botão para ir até a tela de cadastro
        btnSignUp.setOnClickListener {
            hideDialog()
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // configurar o click do botão Entrar
        btnSignIn.setOnClickListener {
            signInViewModel.signIn(edtEmail.editText?.text.toString(),
                edtPassword.editText?.text.toString())
        }

    }

    private var userObserver = Observer<User> { user ->

        user?.let {

            // showDialog()
            val editor = sharedPreferences.edit()
            editor.putBoolean("MANTER_CONECTADO", chkKeepConnected.isChecked)
            editor.putString("USUARIO", it.id)
            editor.apply()

            br.com.urbanproblems.MyApp.user = it
            hideDialog()
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }

    }

    private var responseStatusObserver = Observer<ResponseStatus> {
        if(it!!.success) {
            //showLongToast(it.message)
        } else {
            showLongSnack(it.message)
        }
    }

    private var loadingObserver = Observer<Boolean> {
        if(it == true) {
            //hideDialog()
            showDialog()
        } else {
            hideDialog()
        }
    }

}
