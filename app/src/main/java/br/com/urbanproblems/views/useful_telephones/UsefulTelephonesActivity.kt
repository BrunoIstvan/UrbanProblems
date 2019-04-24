package br.com.urbanproblems.views.useful_telephones

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import br.com.urbanproblems.R
import br.com.urbanproblems.extensions.hideDialog
import br.com.urbanproblems.extensions.showActionDialog
import br.com.urbanproblems.extensions.showDialog
import br.com.urbanproblems.extensions.showLongSnack
import br.com.urbanproblems.models.UsefulTelephone
import kotlinx.android.synthetic.main.activity_utilities.*


class UsefulTelephonesActivity : AppCompatActivity() {

    private lateinit var usefulTelephonesViewModel: UsefulTelephonesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_utilities)

        usefulTelephonesViewModel = ViewModelProviders.of(this).get(UsefulTelephonesViewModel::class.java)
        usefulTelephonesViewModel.loadingLiveData.observe(this, Observer<Boolean> {
            if(it == true) {
                showDialog()
            } else {
                hideDialog()
            }
        })
        usefulTelephonesViewModel.telephones.observe(this, Observer<ArrayList<UsefulTelephone>> {
            // FUNCIONANDO
            rvUtilTelephones.adapter = UsefulTelephonesAdapter(this, it!!) { useful ->


                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.CALL_PHONE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    showActionDialog(getString(R.string.message_confirmation),
                        getString(R.string.message_confirm_calling) + " ${useful.number} (${useful.name})? "
                    ,onPositiveClick = {

                        val callIntent = Intent(Intent.ACTION_CALL)
                        callIntent.data = Uri.parse("tel:${useful.number}")

                        startActivity(callIntent)

                    })

                } else {
                    showLongSnack(getString(R.string.message_confirm_calling))
                }

            }
            //val layoutManager = LinearLayoutManager(this)
            val gridLayoutManager = GridLayoutManager(this, 2)
            rvUtilTelephones.layoutManager = gridLayoutManager
        })
        usefulTelephonesViewModel.listAll()

    }

}
