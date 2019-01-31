package br.com.brunofernandowagner.views.myprofile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import br.com.brunofernandowagner.MyApp
import br.com.brunofernandowagner.R
import br.com.brunofernandowagner.extensions.hideDialog
import br.com.brunofernandowagner.extensions.showDialog
import br.com.brunofernandowagner.extensions.showLongSnack
import br.com.brunofernandowagner.extensions.showShortSnack
import br.com.brunofernandowagner.models.ResponseStatus
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.imagepicker.model.Image
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyProfileActivity : AppCompatActivity() {

    private lateinit var myProfileViewModel: MyProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        fillUserData()

        myProfileViewModel = ViewModelProviders.of(this).get(MyProfileViewModel::class.java)
        myProfileViewModel.loadingLiveData.observe(this, loadingObserver)
        myProfileViewModel.responseStatusLiveData.observe(this, responseStatusObserver)
        myProfileViewModel.uploadResponseStatusLiveData.observe(this, uploadResponseStatusObserver)
        myProfileViewModel.updateResponseStatusLiveData.observe(this, updateResponseStatusObserver)

    }

    private var updateResponseStatusObserver = Observer<ResponseStatus> {

        if(it?.success == true) {
            showShortSnack(it.message)
            hideDialog()
        } else {
            showLongSnack(it!!.message)
            hideDialog()
        }

    }

    private var uploadResponseStatusObserver = Observer<ResponseStatus> {

        if(it?.success == true) {
            showShortSnack(it!!.message)
            hideDialog()
        } else {
            showLongSnack(it!!.message)
            hideDialog()
        }

    }

    private var responseStatusObserver = Observer<ResponseStatus> {

        if(it?.success == true) {

        } else {
            showLongSnack(it!!.message)
        }

    }

    private var loadingObserver = Observer<Boolean> {

        if(it!!) {
            showDialog()
        } else {
            hideDialog()
        }

    }

    private fun choosePicture() {

        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle(getString(R.string.title_select_action))
        val pictureDialogItems = arrayOf(getString(R.string.option_select_from_gallery),
            getString(R.string.option_take_a_picture))

        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }

        pictureDialog.show()

    }

    private fun choosePhotoFromGallary() {

        ImagePicker.create(this)
            .returnMode(ReturnMode.GALLERY_ONLY) // set whether pick and / or camera action should return immediate result or not.
            .folderMode(true) // folder mode (false by default)
            //.toolbarFolderTitle("Folder") // folder selection title
            //.toolbarImageTitle("Tap to select") // image selection title
            .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
            .includeVideo(true) // Show video on image picker
            .single() // single mode
            //.multi() // multi mode (default mode)
            .limit(1) // max images can be selected (99 by default)
            .showCamera(true) // show camera or not (true by default)
            .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
            //.origin(images) // original selected images, used in multi mode
            //.exclude(images) // exclude anything that in image.getPath()
            //.excludeFiles(files) // same as exclude but using ArrayList<File>
            //.theme(R.style.CustomImagePickerTheme) // must inherit ef_BaseTheme. please refer to sample
            .enableLog(false) // disabling log
            //.imageLoader(GrayscaleImageLoder()) // custom image loader, must be serializeable
            .start() // start image picker activity with request code

    }

    private fun takePhotoFromCamera() {

        ImagePicker.cameraOnly().start(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            //val images : List<Image> = ImagePicker.getImages(data) as List<Image>
            // or get a single image only
            val image: Image = ImagePicker.getFirstImageOrNull(data) as Image

            //myProfileViewModel.uploadImageProfile(user.id!!, image.path)
            MyApp.user!!.avatar = image.path
            myProfileViewModel.saveUserProfile(MyApp.user!!)

            Glide.with(this).load(image.path).into(ivMyProfile)

        }

        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun fillUserData() {

        tvFullname.text = MyApp.user!!.name
        tvEmail.text = MyApp.user!!.email
        tvDateRegistration.text = MyApp.user!!.dateRegister
        if (!MyApp.user!!.avatar.isEmpty()) {
            Glide.with(this).load(MyApp.user!!.avatar).into(ivMyProfile)
        } else {
            ivMyProfile.setImageResource(R.drawable.fpo_avatar)
        }

    }

    private fun saveProfile() {

        myProfileViewModel.saveUserProfile(MyApp.user!!)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_profile_navigation, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            //R.id.action_save -> saveProfile()
            R.id.action_camera -> choosePicture()
        }
        return super.onOptionsItemSelected(item)
    }

}
