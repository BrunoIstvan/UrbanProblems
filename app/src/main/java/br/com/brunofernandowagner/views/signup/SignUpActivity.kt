package br.com.brunofernandowagner.views.signup

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.brunofernandowagner.R
import br.com.brunofernandowagner.extensions.hideDialog
import br.com.brunofernandowagner.extensions.showDialog
import br.com.brunofernandowagner.extensions.showLongSnack
import br.com.brunofernandowagner.extensions.showLongToast
import br.com.brunofernandowagner.models.ResponseStatus
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpViewModel: SignUpViewModel

    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        signUpViewModel.loading.observe(this, loadingObserver)
        signUpViewModel.authResponseStatus.observe(this, authStatusObserver)
        signUpViewModel.saveUserResponseStatus.observe(this, saveUserStatusObserver)

        btnSignUp.setOnClickListener {
            signUpViewModel.signUp(edtName.editText?.text.toString(),
                edtEmail.editText?.text.toString(),
                edtPassword.editText?.text.toString(),
                edtConfirmPassword.editText?.text.toString())
        }

    }

    private var saveUserStatusObserver = Observer<ResponseStatus> {
        if(it!!.success) {
            showLongToast(it!!.message)
            finish()
        } else {
            showLongSnack(it!!.message)
        }
    }


    private var authStatusObserver = Observer<ResponseStatus> {
        if(it!!.success) {
            showLongToast(getString(R.string.message_signup_success))
            finish()
        } else {
            showLongSnack(it!!.message)
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
