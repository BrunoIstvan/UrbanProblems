package br.com.brunofernandowagner.views.saveproblem

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.com.brunofernandowagner.MyApp
import br.com.brunofernandowagner.R
import br.com.brunofernandowagner.extensions.*
import br.com.brunofernandowagner.models.GeoLocation
import br.com.brunofernandowagner.models.Problem
import br.com.brunofernandowagner.models.ResponseStatus
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.imagepicker.model.Image
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_save_problem.*
import java.text.SimpleDateFormat
import java.util.*

class SaveProblemActivity : AppCompatActivity() {

    private lateinit var problem: Problem
    private lateinit var saveProblemViewModel: SaveProblemViewModel
    private lateinit var photoPath: String

    private lateinit var problemLatLng: LatLng
    private var geoLocation = GeoLocation()

    private lateinit var locationListener: LocationListener

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_problem)

        saveProblemViewModel = ViewModelProviders.of(this).get(SaveProblemViewModel::class.java)
        saveProblemViewModel.loadingLiveData.observe(this, Observer<Boolean> {
            if(it!!) showDialog() else hideDialog()
        })
        saveProblemViewModel.responseStatusLiveData.observe(this, Observer<ResponseStatus> {
            if(it?.success == true) showLongToast(it.message) else showLongSnack(it!!.message)
        })
        saveProblemViewModel.updateResponseStatusLiveData.observe(this, Observer<ResponseStatus> {
            if (it?.success == true) {
                showLongToast(it.message)
                finish()
            } else {
                showLongSnack(it!!.message)
            }
        })
        saveProblemViewModel.problemLiveData.observe(this, Observer<Problem> {
            fillProblemData(it!!)
        })
        saveProblemViewModel.geoLocationLiveData.observe(this, Observer<GeoLocation> { loc ->
            fillProblemLocation(loc)
            loc?.let {
                val myLocation = LatLng(it.latitude!!, it.longitude!!)
                fillAddress(myLocation)
            }
        })
        saveProblemViewModel.latLngLiveData.observe(this, Observer<LatLng> {
            fillAddress(it!!)
        })
        saveProblemViewModel.imagePathLiveData.observe(this, Observer<String> {
            if(!it.isNullOrEmpty()) {
                problem.photo = it
                Glide.with(this).load(it).into(ivProblem)
            }
        })
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        problem = if(intent.hasExtra("PROBLEM")) {
            intent.getParcelableExtra("PROBLEM")
        } else {
            Problem()
        }
        saveProblemViewModel.setProblem(problem)

        initLocationListener()

        btnLocation.setOnClickListener {
            requestLocationUpdates()
        }

    }

    private fun fillProblemLocation(it: GeoLocation?) {
        problem.address = it!!.address
        problem.addressNumber = it.addressNumber
        problem.neighborhood = it.neighborhood
        problem.city = it.city
        problem.state = it.state
        problem.postalCode = it.postalCode
        problem.latitude = it.latitude
        problem.longitude = it.longitude
    }

    private fun fillProblemData(problem: Problem) {

        edtTitle.editText?.setText(problem.title)
        edtDetail.editText?.setText(problem.detail)
        if(!problem.photo.isNullOrEmpty()) {
            photoPath = problem.photo!!
            Glide.with(this).load(problem.photo).into(ivProblem)
        } else {
            Glide.with(this).load(R.drawable.no_cover_available).into(ivProblem)
        }

        problem.latitude?.let { lat ->
            problem.longitude?.let { lon ->
                val myLocation = LatLng(lat, lon)
                problemLatLng = myLocation
                fillAddress(myLocation)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_problem_navigation, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_save -> confirmSave()
            R.id.action_camera -> choosePicture()
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmSave() {
        showActionDialog(getString(R.string.message_confirmation),
                         getString(R.string.message_confirm_save_problem),
                         onPositiveClick = { save() })
    }

    private fun save() {

        problem.datetime = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .format(Calendar.getInstance().time)
        problem.userId = MyApp.user!!.id
        problem.title = edtTitle.editText?.text.toString()
        problem.detail = edtDetail.editText?.text.toString()

        if (::photoPath.isInitialized) problem.photo = photoPath

        fillProblemLocation(geoLocation)

        saveProblemViewModel.saveProblem(problem)

    }

    private fun choosePicture() {

        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle(getString(R.string.title_select_action))
        val pictureDialogItems = arrayOf(getString(R.string.option_select_from_gallery),
            getString(R.string.option_take_a_picture))

        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> choosePhotoFromGallery()
                1 -> takePhotoFromCamera()
            }
        }

        pictureDialog.show()

    }

    private fun choosePhotoFromGallery() {

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
            photoPath = image.path
            problem.photo = photoPath
            saveProblemViewModel.setImagePath(photoPath)
            Glide.with(this).load(image.path).into(ivProblem)
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun fillAddress(minhaPosicao: LatLng) {

        val endereco = getFormattedAddress(minhaPosicao)
        tvAddress.text = endereco
        tvAddress.visibility = View.VISIBLE
        tvEmpty.visibility = View.GONE

    }

    private fun initLocationListener() {
        locationListener = object : LocationListener{
            override fun onLocationChanged(location: Location?) {
                location?.let {loc ->
                    problemLatLng = LatLng(loc.latitude, loc.longitude)
                    hideDialog()
                    saveProblemViewModel.setLatLng(problemLatLng)
                }
            }
            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) { }
            override fun onProviderEnabled(p0: String?) { }
            override fun onProviderDisabled(p0: String?) { }
        }
    }

    private fun requestLocationUpdates() {
        showDialog()
        // Desativar o loadingLiveData após 10 segundos...
        Handler().postDelayed({
            hideDialog()
        }, 10000L)

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
        }
    }

    private fun getFormattedAddress(latLng: LatLng): String {

        val geocode = Geocoder(applicationContext, Locale.getDefault())
        val address = geocode.getFromLocation(latLng.latitude, latLng.longitude, 1)

        if(address.size > 0) {

            geoLocation = GeoLocation()
            geoLocation.latitude = latLng.latitude
            geoLocation.longitude = latLng.longitude

            var completeAddress = "\n"
            if(!address[0].thoroughfare.isNullOrEmpty()) {
                geoLocation.address = address[0].thoroughfare
                completeAddress += "${getString(R.string.label_address)}: ${address[0].thoroughfare}\n"
            }
            if(!address[0].subThoroughfare.isNullOrEmpty()) {
                geoLocation.addressNumber = address[0].subThoroughfare
                completeAddress += "Nº.: ${address[0].subThoroughfare}\n"
            }
            if(!address[0].subLocality.isNullOrEmpty()) {
                geoLocation.neighborhood = address[0].subLocality
                completeAddress += "${getString(R.string.label_neighborhood)}: ${address[0].subLocality}\n"
            }
            if(!address[0].locality.isNullOrEmpty() ) {
                geoLocation.city = address[0].locality
                completeAddress += "${getString(R.string.label_city)}: ${address[0].locality}\n"
            } else if(!address[0].subAdminArea.isNullOrEmpty()) {
                geoLocation.city = address[0].subAdminArea
                completeAddress += "${getString(R.string.label_city)}: ${address[0].subAdminArea}\n"
            }
            if(!address[0].adminArea.isNullOrEmpty()) {
                geoLocation.state = address[0].adminArea
                completeAddress += "${getString(R.string.label_state)}: ${address[0].adminArea}\n"
            }
            if(!address[0].postalCode.isNullOrEmpty()) {
                geoLocation.postalCode = address[0].postalCode
                completeAddress += "${getString(R.string.label_postalcode)}: ${address[0].postalCode}\n"
            }
            fillProblemLocation(geoLocation)
            return completeAddress
        } else {

            return getString(R.string.message_location_not_found)

        }
    }

}
