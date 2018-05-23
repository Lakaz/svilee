package com.mobiwarez.laki.sville.ui.toys.create


//import com.mobiwarez.laki.seapp.Manifest
import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
import android.provider.Settings
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.Snackbar
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import com.evernote.android.job.JobManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.location.places.Places
import com.google.firebase.analytics.FirebaseAnalytics
import com.mobiwarez.data.givenToys.db.model.GivenToyModel
import com.mobiwarez.laki.sville.DashBoardActivity
import com.mobiwarez.laki.sville.R
import com.mobiwarez.laki.sville.extensions.loadAvatar
import com.mobiwarez.laki.sville.extensions.loadImg
import com.mobiwarez.laki.sville.fragments.RxBaseFragment
import com.mobiwarez.laki.sville.job.UploadItemJob
import com.mobiwarez.laki.sville.prefs.GetUserData
import com.mobiwarez.laki.sville.ui.imageview.ImageDetailActivity
import com.mobiwarez.laki.sville.ui.toys.sharedtoys.SharedToysActivity
import com.mobiwarez.laki.sville.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_create_toy.*
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class CreateToyFragment : RxBaseFragment(), CreateToyContract.View , LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private var imagePath: String? = null

    private var acquiredLocationnFailed: Boolean = false

    private var currentLocation: Location? = null
    private var googleApiClient: GoogleApiClient? = null

    private val REQUEST_CHECK_SETTINGS = 180

    private var photoTaker: PhotoTaker<*>? = null
    private var imageLoader: ImageLoader? = null

    private var disposable: CompositeDisposable? = null

    private var newToyViewModel: NewToyViewModel? = null

    private var mBottomSheet: View? = null
    private var mBottomSheetDialog: BottomSheetDialog? = null
    private var mDialogBehavior: BottomSheetBehavior<*>? = null
    private var mBehavior: BottomSheetBehavior<*>? = null

    private val myPlaceList = arrayListOf<MyPlace>()

    private var presenter: CreateToyContract.Presenter? = null

    //private var view: View? = null

    private val UPDATE_INTERVAL = 5000
    private val FASTEST_INTERVAL = 4000
    private var lastLocation: Location? = null

    private var numUpdates: Int = 0

    private var selectedLocation: String? = null

    private var itemViewModel: NewToyViewModel? = null


    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var firebaseAnalytics: FirebaseAnalytics


    var locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(2000)





    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_create_toy, container, false)
        return view
    }

    override fun onPause() {
        super.onPause()
        disposable!!.clear()
    }

    override fun onResume() {
        super.onResume()
        disposable = CompositeDisposable()
        googleApiClient?.connect()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpBottomSheetDialog()
        //toy_category.setOnClickListener { v -> showCategories() }
        //toy_age_group.setOnClickListener { v -> showAgeGroups() }
        setAddressButton.setOnClickListener { v -> showAddresses() }
        setUpUi()
        setTextListeners()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val rightDrawable = AppCompatResources
                    .getDrawable(context, R.drawable.ic_arrow_drop_down_circle_grey_24dp)
            //toy_age_group.setCompoundDrawablesWithIntrinsicBounds(null, null,rightDrawable, null)
            //toy_category.setCompoundDrawablesWithIntrinsicBounds(null, null,rightDrawable, null)
        } else {
            //Safely create our VectorDrawable on pre-L android versions.
            val leftDrawable = VectorDrawableCompat
                    .create(context.resources, R.drawable.ic_arrow_drop_down_circle_grey_24dp, null)
            //toy_age_group.setCompoundDrawablesWithIntrinsicBounds(null, null, leftDrawable, null)
            //toy_category.setCompoundDrawablesWithIntrinsicBounds(null, null,leftDrawable, null)
        }

    }

    private fun setUpUi() {
        user_name.text = GetUserData.getUserDisplayName(context)
        user_avatar.loadAvatar(GetUserData.getRealPhotoUrl(context))
        newToyViewModel?.pictureUrl?.let {
            toy_image.loadImg(it)
            toy_image.setOnClickListener { _ ->
                val intent = Intent(context, ImageDetailActivity::class.java)
                intent.putExtra(StringConstants.IMAGE_URL_KEY, it)
                startActivity(intent)
            }
        }
        //newToyViewModel?.category?.let { toy_category.text = it }
        //newToyViewModel?.ageGroup?.let { toy_age_group.text = it }
        newToyViewModel?.description?.let { item_description.setText(it) }
        newToyViewModel?.pickUpTime?.let { pick_up_time.setText(it) }
        newToyViewModel?.title?.let { item_title.setText(it) }
        newToyViewModel?.address?.let { user_location.text = it }
        newToyViewModel?.location?.let { currentLocation = it }
        newToyViewModel?.pickUpLocation?.let { pick_up_location.setText(it) }


    }

    private fun setTextListeners() {
        item_description.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                firebaseAnalytics.logEvent("wrote_item_description", null)
                newToyViewModel?.description = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        pick_up_time.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                firebaseAnalytics.logEvent("wrote_pick_up_time", null)
                newToyViewModel?.pickUpTime = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        pick_up_location.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                newToyViewModel?.pickUpLocation = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        item_title.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                firebaseAnalytics.logEvent("wrote_gift_title", null)
                newToyViewModel?.title = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }


    override fun showPhoto(uri: Uri) {
        //imageLoader!!.loadImage(uri.toString(), toyImage)
    }

    override fun showPresentLocation(address: String) {
        user_location.text = address
    }

    override fun showAddresses() {
        showBottomSheetDialog(myPlaceList!!)
    }

    override fun showMessages(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showCategories() {

        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Select Item Category")
                .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                .setItems(R.array.category_array) { dialog, which ->
                    var category = (dialog as AlertDialog).listView.adapter.getItem(which).toString()

                    //toy_category.text = category

                    newToyViewModel?.category = category
                }

        builder.show()

    }

    override fun showAgeGroups() {

        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Select Age Group")
                .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                .setItems(R.array.age_group_array) { dialog, which ->
                    var ageGroup = (dialog as AlertDialog).listView.adapter.getItem(which).toString()
                    //toy_age_group.text = ageGroup
                    newToyViewModel?.ageGroup = ageGroup
                }

        builder.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
        setHasOptionsMenu(true)
        JobManager.create(activity)
        newToyViewModel = ViewModelProviders.of(activity).get(NewToyViewModel::class.java)
        imageLoader = ImageLoaderImpl(context)
        photoTaker = PhotoTakerImpl(context, Schedulers.io())
        presenter = CreateToyPresenter(this)
        createGoogleApi()
    }

    private fun createGoogleApi() {
        Timber.tag("LifeCycles");
        Timber.d("Activity Created");
        Log.d(TAG, "createGoogleApi()")
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addApi(Places.GEO_DATA_API)
                    .build()
        }
    }


    private fun prepareGivenToyViewModel(): GivenToyModel {
        val now = Date()
        val given = GivenToyModel()

        var toyAgeGroup = if(newToyViewModel?.ageGroup == "All") "all" else newToyViewModel?.ageGroup
        var toyCategory = if(newToyViewModel?.category == "All") "all" else newToyViewModel?.category

        when (toyAgeGroup) {
            "Baby: 0 to 2 years" -> toyAgeGroup = "baby"
            "Toddler: 3 to 5 years" -> toyAgeGroup = "toddler"
            "Kid: 5 years+" -> toyAgeGroup = "kid"
        }


        if (TextUtils.isEmpty(toyAgeGroup)) toyAgeGroup = "all"
        if (TextUtils.isEmpty(toyCategory)) toyCategory = "all"

        val pickUpLocation = if(TextUtils.isEmpty(newToyViewModel?.pickUpLocation)) "no pickup location" else newToyViewModel?.pickUpLocation
        val pickUpTime = if(TextUtils.isEmpty(newToyViewModel?.pickUpTime)) "no pickup time" else newToyViewModel?.pickUpTime
        val loca = if(TextUtils.isEmpty(selectedLocation)) "no place" else selectedLocation

        val description = if (TextUtils.isEmpty(item_description.text.toString())) "no description" else item_description.text.toString()
        given.itemTitle = newToyViewModel?.title
        given.toyGiverName = GetUserData.getUserDisplayName(context)
        given.toyAgeGroup = toyAgeGroup
        given.toyCategory = toyCategory
        given.isToySynced = 0
        given.isToyGiven = 0
        given.toyDescription = description
        given.toyImagePath = if (!TextUtils.isEmpty(newToyViewModel?.pictureUrl)) newToyViewModel?.pictureUrl else "no_image"
        given.receiverName = "none"
        given.userAvatar = GetUserData.getRealPhotoUrl(context)
        given.latitude = currentLocation?.latitude?.toFloat()!!
        given.longitude = currentLocation?.longitude?.toFloat()!!
        given.giverUUID = GetUserData.getUserUUID(context)
        given.givenLocationName = loca
        given.toyId = IdGenerator.createPostId(context)
        given.timePosted = now.toString()
        given.pickUpTime = pickUpTime
        given.pickUpLocation = pickUpLocation

        return given


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item!!.itemId == R.id.action_create_toy) {
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_new_toy, menu)
        val view = menu?.findItem(R.id.action_create_toy)?.actionView
        view?.setOnClickListener {
            firebaseAnalytics.logEvent("give_gift_clicked", null)
           if(currentLocation != null) {

               if (TextUtils.isEmpty(selectedLocation) and !acquiredLocationnFailed){
                   activity.showToast("Acquiring your location, please wait")

               }

               else{
                   if (!TextUtils.isEmpty(newToyViewModel?.title)) {
                       showGivenItems()
                   }else{
                       Toast.makeText(context, "Please enter title", Toast.LENGTH_SHORT).show()
                   }

               }

           }
           else{
                Toast.makeText(context, "Fetching location. Please try again.", Toast.LENGTH_SHORT).show()
           }

        }
    }

    private fun setUpBottomSheetDialog() {
        mBottomSheet = activity.findViewById(R.id.placesbottomSheet)
        mBehavior = BottomSheetBehavior.from(mBottomSheet!!)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.findViewById<View>(R.id.fakeShadow).visibility = View.GONE
        }


        mBehavior!!.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }


    private fun getCurrentPlace() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        val result = Places.PlaceDetectionApi.getCurrentPlace(googleApiClient!!, null)


        result.setResultCallback({ likelyPlaces ->
            for (placeLikelihood in likelyPlaces) {
                val place = placeLikelihood.getPlace()

                val myPlace = MyPlace()
                myPlace.placeAddress = place.address.toString()
                myPlace.placeName = place.name.toString()
                myPlaceList?.add(myPlace)
                Log.i(TAG, String.format("MyPlace '%s' has likelihood: %g",
                        placeLikelihood.place.name,
                        placeLikelihood.likelihood))
            }

            likelyPlaces.release()
        })
    }


/*
    private fun createTempFile(): File {
        return File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis().toString() + "_image.jpeg")
    }
*/


    private fun showGivenItems() {
        val model = prepareGivenToyViewModel()
        val manger = DatabaseManger(context)
        val subscription = manger.saveGivenItem(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            _-> UploadItemJob.buildPostItemJob(model.toyId)
                        },
                        {
                            error ->
                            Timber.d(error)
                            showMessages("Failed to create item")
                        },
                        {
                            activity.showLongToast("Your gift is being uploaded. You can offer another gift")
                            val intent = Intent(context, DashBoardActivity::class.java)
                            startActivity(intent)
                            activity.finish()
                        }
                )

        subscriptions.add(subscription)
    }


    private fun clearViewModel(){
        newToyViewModel?.ageGroup = null
        newToyViewModel?.category = null
        newToyViewModel?.description = null
        newToyViewModel?.pictureUrl = null
        newToyViewModel?.pickUpLocation = null
        newToyViewModel?.pickUpTime = null
    }


    override fun onStop() {
        super.onStop()
        if (googleApiClient?.isConnected!!) googleApiClient?.disconnect()
    }

    @SuppressLint("InflateParams")
    private fun showBottomSheetDialog(places: List<MyPlace>) {
        if (mBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val view = layoutInflater.inflate(R.layout.places_bottom_sheet, null)
        view.findViewById<View>(R.id.fakeShadow).visibility = View.GONE

        val recyclerView = view.findViewById<View>(R.id.places_recyclerView) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = PlacesAdapter(places) { item ->
            mDialogBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN

            showPresentLocation(item.placeName)
            selectedLocation = item.placeName
            mBottomSheetDialog!!.hide()
        }

        mBottomSheetDialog = BottomSheetDialog(activity)
        mBottomSheetDialog!!.setContentView(view)
        mDialogBehavior = BottomSheetBehavior.from(view.parent as View)

        mBottomSheetDialog!!.show()
        mBottomSheetDialog!!.setOnDismissListener { dialog -> mBottomSheetDialog = null }


    }


/*
    private fun loadImageFromGallery() {
        val intent = Intent()
        intent.type = "image*/
/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_CHOOSE)
    }


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun storeImage(image: Bitmap) {
        //File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        actualImage = getOutputMediaFile(MEDIA_TYPE_IMAGE)
        if (actualImage == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ")// e.getMessage());
            return
        }
        try {
            val fos = FileOutputStream(actualImage!!)
            image.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.close()
        } catch (e: FileNotFoundException) {
            Log.d(TAG, "File not found: " + e.message)
        } catch (e: IOException) {
            Log.d(TAG, "Error accessing file: " + e.message)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {


*/
/*
            val extras = data!!.extras
            val imageBitmap = extras!!.get("data") as Bitmap

            try {
                compressedImage = Compressor(context)
                        .setMaxWidth(640)
                        .setMaxHeight(480)
                        .setQuality(80)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).absolutePath)
                        .compressToFile(actualImage!!)

            } catch (e: IOException) {
                e.printStackTrace()
            }

            imagePath = compressedImage!!.toString()

            Glide.with(this).load(Uri.fromFile(compressedImage))
                    .into(toy_image)


*//*

        }


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {


            val extras = data!!.extras
            val imageBitmap = extras!!.get("data") as Bitmap

            storeImage(imageBitmap)

            try {
                compressedImage = Compressor(context)
                        .setMaxWidth(640)
                        .setMaxHeight(480)
                        .setQuality(80)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).absolutePath)
                        .compressToFile(actualImage!!)

            } catch (e: IOException) {
                e.printStackTrace()
            }

            imagePath = compressedImage!!.toString()

            Glide.with(this).load(Uri.fromFile(compressedImage))
                    .into(toy_image)
        }

    }
*/

    companion object {

        val TAG = CreateToyFragment::class.java.simpleName

        fun newInstance(): CreateToyFragment {
            return CreateToyFragment()
        }

        /** Create a file Uri for saving an image or video  */
        private fun getOutputMediaFileUri(type: Int): Uri {
            return Uri.fromFile(getOutputMediaFile(type))
        }

        /** Create a File for saving an image or video  */
        private fun getOutputMediaFile(type: Int): File? {
            // To be safe, you should check that the SDCard is mounted
            // using Environment.getExternalStorageState() before doing this.

            val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "Sache")
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory")
                    return null
                }
            }


            // Create a media file name
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val mediaFile: File
            if (type == MEDIA_TYPE_IMAGE) {

                mediaFile = File(mediaStorageDir.path + File.separator +
                        "IMG_" + timeStamp + ".jpg")
            } else if (type == MEDIA_TYPE_VIDEO) {
                mediaFile = File(mediaStorageDir.path + File.separator +
                        "VID_" + timeStamp + ".mp4")
            } else {
                return null
            }

            return mediaFile
        }
    }


    override fun onLocationChanged(location: Location?) {
        currentLocation = location
        if (numUpdates == 2) {
            getAddress()
            stopLocationUpdates()
        }
        else{
            numUpdates = numUpdates + 1
        }
    }


    private fun stopLocationUpdates() {
        if (googleApiClient?.isConnected!!) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this)
        }
    }


    override fun onConnected(p0: Bundle?) {
        checkLocationSettings()
        getCurrentPlace()

    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }


    private fun getAddress() {

        val locationProvider = ReactiveLocationProvider(context)


        val addressObservable = locationProvider.getReverseGeocodeObservable(currentLocation?.latitude!!,
                currentLocation?.longitude!!, 3)


        val subscription =addressObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            addresses ->
                                if (addresses.isEmpty()){

                                }else{
                                    selectedLocation = addresses[0].subLocality+" , "+addresses[0].subAdminArea
                                    user_location.text = selectedLocation
                                    pick_up_location.setText(selectedLocation)

                                }

                        },
                        {
                            err -> activity.showToast("Failed to get location")
                            acquiredLocationnFailed = true
                        }
                )


        subscriptions.add(subscription)
    }

    private fun checkLocationSettings() {
        Log.d(TAG, "checking location settings")
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        //SettingsClient client = LocationServices.getSettingsClient(this);
        //Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())

        result.setResultCallback { locationSettingsResult ->
            val status = locationSettingsResult.status
            val locationSettingsStates = locationSettingsResult.locationSettingsStates

            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    Log.d(TAG, "location settings are correct")
                    //startLocationUpdates();
                    getLastKnownLocation()
                }

                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    Log.d(TAG, "location settings needs to be configured")
                    try {
                        status.startResolutionForResult(
                                activity,
                                REQUEST_CHECK_SETTINGS)
                    } catch (e: IntentSender.SendIntentException) {
                        e.printStackTrace()
                    }

                }

                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.d(TAG, "location settings cannot be configured")
            }
        }

    }


    // Start location Updates
    private fun startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates()")
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL.toLong())
                .setFastestInterval(FASTEST_INTERVAL.toLong())


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        if(googleApiClient?.isConnected!!) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this)
            //LocationServices.getFusedLocationProviderClient(activity).re
        }




    }



    private fun getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation()")

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
        if (currentLocation != null) {
            Log.i(TAG, "LasKnown location. " +
                    "Long: " + currentLocation!!.getLongitude() +
                    " | Lat: " + currentLocation!!.getLatitude())
            //writeLastLocation();
            startLocationUpdates()
        } else {
            Log.w(TAG, "No location retrieved yet")
            if (isLocationServiceEnabled()) {
                startLocationUpdates()
            } else {
                enableGps()
                startLocationUpdates()
            }
        }


    }

    fun isLocationServiceEnabled(): Boolean {
        var locationManager: LocationManager? = null
        var gps_enabled = false
        var network_enabled = false

        if (locationManager == null)
            locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
            Log.d("CheckGPS", "Failed to check if gps is enabled")
            //do nothing...
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
            //do nothing...
        }

        return gps_enabled

    }



    private fun enableGps() {

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Enable Location")

        builder.setMessage("Location services are disabled. Enable Location to continue")
        builder.setPositiveButton("Enable") { dialogInterface, i ->
            val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(myIntent)
        }

        builder.setNegativeButton("Cancel") { dialogInterface, i ->
            //getActivity().onBackPressed();
        }

        builder.show()

    }





}// Required empty public constructor
