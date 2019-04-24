package br.com.urbanproblems.views.signup

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.urbanproblems.R
import br.com.urbanproblems.extensions.hideDialog
import br.com.urbanproblems.extensions.showDialog
import br.com.urbanproblems.extensions.showLongSnack
import br.com.urbanproblems.extensions.showLongToast
import br.com.urbanproblems.models.ResponseStatus
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpViewModel: SignUpViewModel

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
            showLongToast(it.message)
            finish()
        } else {
            showLongSnack(it.message)
        }
    }


    private var authStatusObserver = Observer<ResponseStatus> {
        if(it!!.success) {
            showLongToast(getString(R.string.message_signup_success))
            finish()
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
