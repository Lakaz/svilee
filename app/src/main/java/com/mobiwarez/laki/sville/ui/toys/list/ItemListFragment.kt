package com.mobiwarez.laki.sville.ui.toys.list

import android.Manifest
import android.app.ProgressDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.mobiwarez.data.contacts.db.model.ContactModel
import com.mobiwarez.laki.sville.R
import com.mobiwarez.laki.sville.data.contactsmanager.ContactsManager
import com.mobiwarez.laki.sville.converter.EngineToModelConverter
import com.mobiwarez.laki.sville.fragments.RxBaseFragment
import com.mobiwarez.laki.sville.prefs.GetUserData
import com.mobiwarez.laki.sville.recyclerUtils.InfiniteScrollListener
import com.mobiwarez.laki.sville.data.remotebackend.RepoHandler
import com.mobiwarez.laki.sville.ui.chat.FireChatActivity
import com.mobiwarez.laki.sville.ui.imageview.ImageDetailActivity
import com.mobiwarez.laki.sville.ui.models.ToyViewModel
//import com.mobiwarez.laki.seapp.ui.toys.create.CreateToyFragment
import com.mobiwarez.laki.sville.ui.toys.create.NewToActivity
import com.mobiwarez.laki.sville.data.ReceiverManager
import com.mobiwarez.laki.sville.data.accountManager.AccountTracker
import com.mobiwarez.laki.sville.util.DatabaseManger
import com.mobiwarez.laki.sville.util.GenerateChatRoom
import com.mobiwarez.laki.sville.util.StringConstants
import com.mobiwarez.laki.sville.util.showToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_item_list.*
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class ItemListFragment : RxBaseFragment(), ItemListAdapter.ReceiveItems, ItemsContract.View, ItemDelegateAdapter.onViewSelectedListener, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private val UPDATE_INTERVAL = 5000
    private val FASTEST_INTERVAL = 4000

    private var numUpdates: Int = 0


    private var currentLocation: Location? = null

    private var presenter: ItemListPresenter? = null

    private var googleApiClient: GoogleApiClient? = null

    private val REQUEST_CHECK_SETTINGS = 187

    private var itemListRecycler: RecyclerView? = null
    private var itemListLayoutManager: RecyclerView.LayoutManager? = null
    private var itemListAdapter: ItemsListAdapter? = null
    private var floatingActionButton: FloatingActionButton? = null
    private var isRefreshing = false
    private var hasMore = true
    private var currentPage = 0
    private val pageSize = 20

    private var viewModel:ItemListViewModel? = null

    private var age:String? = null
    private var category:String? = null

    private var chatProgressDialog: ProgressDialog? = null
    private var claimProgressDialog: ProgressDialog? = null

    lateinit var firebaseAnalytics: FirebaseAnalytics

    private val converter: EngineToModelConverter = EngineToModelConverter()

    var locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(2000)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
        presenter = ItemListPresenter(this)
        age = arguments.getString("age")
        category = arguments.getString("category")
        createGoogleApi()
        chatProgressDialog = ProgressDialog(context)
        chatProgressDialog?.isIndeterminate = true
        chatProgressDialog?.setTitle("Setting up chat between you and the giver.")

        claimProgressDialog = ProgressDialog(context)
        claimProgressDialog?.isIndeterminate = true


        //initializeRecyclerView();
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_item_list, container, false)

        itemListRecycler = view!!.findViewById(R.id.item_list_recyclerView)
        floatingActionButton = activity.findViewById(R.id.fab)
        floatingActionButton!!.setOnClickListener { _ ->
            firebaseAnalytics.logEvent("give_item_from_search_clicked", null)
            addToy()
        }
        return view
    }

    override fun showRefreshing() {
        swipe_refresher.isRefreshing = true
    }

    override fun stopRefreshing() {
        swipe_refresher.isRefreshing = false
    }


    private fun addToy() {
        val intent = Intent(activity, NewToActivity::class.java)
        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        EventBus.getDefault().post(HideReviewMessage())
        age = if (TextUtils.isEmpty(age)) "all" else age

        when (age) {
            "Baby: 0 to 2 years" -> age = "baby"
            "Toddler: 3 to 5 years" -> age = "toddler"
            "Kid: 5 years+" -> age = "kid"
        //fetchItems(age!!, category!!)
        //fetchItems()
        }

        category = if (TextUtils.isEmpty(category)) "all" else category
        //fetchItems(age!!, category!!)
        //fetchItems()

        if (googleApiClient?.isConnected!!){
            getLastKnownLocation()
        }else{
            googleApiClient?.connect()
        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        item_list_recyclerView.apply {
            setHasFixedSize(true)
            val linearLayout = LinearLayoutManager(context)
            layoutManager = linearLayout
            clearOnScrollListeners()
            addOnScrollListener(InfiniteScrollListener({ age?.let { category?.let { it1 -> fetchItems() } } }, linearLayout))
        }
        swipe_refresher.setColorSchemeColors(
                ActivityCompat.getColor(activity, R.color.colorAccent),
                ActivityCompat.getColor(activity, R.color.colorPrimary),
                ActivityCompat.getColor(activity, R.color.colorPrimaryDark)
        )
        swipe_refresher.setOnRefreshListener {
            isRefreshing = true
            currentPage = 0
            hasMore = true
            swipe_refresher.isRefreshing = false
        }

        viewModel = ViewModelProviders.of(activity).get(ItemListViewModel::class.java)

        initAdapter()

    }

    private fun initAdapter() {
        if (item_list_recyclerView.adapter == null) {
            itemListAdapter = ItemsListAdapter(this)
            item_list_recyclerView.adapter = itemListAdapter
        }

        if(!viewModel?.getItemsListed()?.isEmpty()!!){
            val lists = viewModel?.getItemsListed()

            lists?.let {
                //isLoaded = true
                (item_list_recyclerView.adapter!! as ItemsListAdapter).addItems(it)
            }

        }

    }


    private fun extractArguments(arguments: Bundle) {

        //fetchItems("", "")

        /*
        if (arguments.getBoolean(KEY_FAVOURITE_ARTICLES, false)) {
            setFavouriteItems();
        } else {
            updateFeed(arguments.getInt(KEY_ARTICLES_FEED_ID), arguments.getString(KEY_FEED_TITLE, resources.getString(R.string.app_name)));
        }
*/
    }

    private fun nextPage(size: Int){
        val items = currentPage*pageSize
        if (items < size) {
            currentPage += 1
            hasMore = false
        }
    }


    fun fetchItems() {
        val repo = RepoHandler()

        val subscription = repo.getItemListings(age!!, category!!, currentLocation!!, currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            items ->
                                nextPage(items.nextpage?.toInt()!!)
                                if(items.listings?.isEmpty()!!) {
                                    if (viewModel?.getItemsListed()?.isEmpty()!!){
                                        showMessage("There are no items on offer around here")
                                    }else{
                                        showMessage("There are no more items.")
                                    }
                                } else {
                                    viewModel?.addItems(items.listings)
                                    showToys(items.listings)
                                }
                        },
                        {
                            error -> showLoadingError()
                        },
                        {

                        }
                )

        subscriptions.add(subscription)
    }


    override fun showFullImage(imageUrl: String) {
        val intent = Intent(context, ImageDetailActivity::class.java)
        intent.putExtra(StringConstants.IMAGE_URL_KEY, imageUrl)
        startActivity(intent)
    }



    override fun showToys(toyViewModels: List<ToyViewModel>) {
        itemListAdapter!!.addItems(toyViewModels)
        //(item_list_recyclerView.adapter as ItemListAdapter).onItemsUpdate(toyViewModels)
    }

    override fun showMessage(message: String) {
        Snackbar.make(parent!!, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showNoItems() {
        Snackbar.make(parent!!, "There are no available items in this area", Snackbar.LENGTH_SHORT).show()
    }

    override fun showLoadingError() {
        Snackbar.make(parent!!, "Sorry! Failed to load items", Snackbar.LENGTH_SHORT).show()

    }

    override fun claimItem(toyViewModel: ToyViewModel?) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("You want to receive this item: "+ toyViewModel?.toyDescription)
                .setPositiveButton("Yes") {_, _ ->
                    firebaseAnalytics.logEvent("claim_item_clicked", null)
                    receiveItem(toyViewModel!!)
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        builder.show()

    }



    override fun receiveItem(model: ToyViewModel) {
        val bundle = Bundle()
        bundle.putString("gift_age_group", model.toyAgeGroup)
        bundle.putString("gift_category", model.toyCategory)
        firebaseAnalytics.logEvent("clicked_receive_item", bundle)
        val accountTracker = AccountTracker(activity)
        val subscription = accountTracker.canReceive()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            result -> if(result){
                                receiveIt(model)
                        }else {
                            activity.showToast("You have no credits to receive this item.")
                        }
                        }
                )

        subscriptions.add(subscription)

    }


    override fun showReviews(uuid: String, toyViewModel: ToyViewModel, imageView: ImageView) {
        viewModel?.selectedModel = toyViewModel
        viewModel?.selectedGiver = uuid
        firebaseAnalytics.logEvent("view_reviews_clicked", null)
        imageView.drawable?.let { viewModel?.selectedAvatarDawable = it }
        EventBus.getDefault().post(ShowReviewMessage())

    }

    private fun receiveIt(model: ToyViewModel) {
        if (model.giverUUID != GetUserData.getUserUUID(context)) {

            val builder = AlertDialog.Builder(activity)
            builder.setTitle("You want to receive this item: " + model.toyname)
                    .setPositiveButton("Yes") { _, _ ->
                        claimProgressDialog?.setTitle("Claiming " + model.toyname)
                        claimProgressDialog?.show()
                        firebaseAnalytics.logEvent("agreed_to_receive_pressed", null)
                        doReceiveItem(model)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        firebaseAnalytics.logEvent("cancel_receive_item_clicked", null)
                        dialog.dismiss()
                    }
            builder.show()
        }else {
            Toast.makeText(context, "You cannot receive an item you offered.", Toast.LENGTH_SHORT).show()
        }

    }


    private fun reduceCredits() {
        val accountTracker = AccountTracker(activity)
        val subscription = accountTracker.decreaseCredits()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            response ->
                        },
                        {

                        }
                )

        subscriptions.add(subscription)
    }


    private fun saveReceivedItem(model: ToyViewModel) {
        val receivedItem = converter.convertToyModelToReceivedModel(model)
        val manager = DatabaseManger(context)
        val subscription = manager.saveReceivedItems(receivedItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            response ->
                        },
                        {

                        }
                )

        subscriptions.add(subscription)

    }

    private fun doReceiveItem(model: ToyViewModel) {
        val manager = ReceiverManager()
        val userUuid = GetUserData.getUserUUID(context)
        val userName = GetUserData.getUserDisplayName(context)
        val subscription = manager.receiveReactItem(model.toyId, userName, userUuid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { response ->
                            claimProgressDialog?.dismiss()
                            if (response == "success") {
                                saveReceivedItem(model)
                                reduceCredits()
                                startChat(userUuid, model.giverUUID, model.giverName, model)
                            }else {
                                showMessage("Sorry, failed to claim item for you.")
                            }
                        },

                        {
                            err ->
                                claimProgressDialog?.dismiss()
                                showMessage("Sorry failed to claim "+model.toyDescription+" for you")
                        },
                        {

                        }
                )

        subscriptions.add(subscription)
    }




    private fun startChat(myUUID: String, giverUUID: String, giverName: String, model: ToyViewModel) {
        val contactsManager = ContactsManager(context)
        val subscription = contactsManager.getChatRoom(giverUUID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            room -> if (room != "no chat room"){
                                        showChatRoom(room, giverUUID, giverName)
                                    }else{
                                        chatProgressDialog?.setTitle("Initiating chat between you and "+model.giverName)
                                        chatProgressDialog?.show()
                                        requestChat(myUUID, giverUUID, giverName, model)
                                    }
                        },
                        {

                        }
                )

        subscriptions.add(subscription)
    }

    fun showChatRoom(room: String, giverUUID: String, giverName: String){
        val intent = Intent(activity, FireChatActivity::class.java)
        intent.putExtra("chatRoom", room)
        intent.putExtra("uuid", giverUUID)
        intent.putExtra("name", giverName)
        startActivity(intent)
    }

    fun requestChat(uuid: String, giverUUID: String, myName: String, model: ToyViewModel){
        val contactsManger = ContactsManager(context)
        val chatRoom = GenerateChatRoom.generate(uuid, giverUUID)
        val subscription = contactsManger.requestChat(chatRoom, giverUUID, myName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            response ->
                                chatProgressDialog?.dismiss()
                                if (response == "success"){
                                    addNewContact(model, chatRoom)
                                    showChatRoom(chatRoom, giverUUID, myName)

                                }else{
                                    showMessage("Sorry failed to initiate chat")
                                }
                        },
                        {
                            error ->
                                chatProgressDialog?.dismiss()
                                showMessage("Failed to initiate chat")
                        }
                )

        subscriptions.add(subscription)
    }


    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        if (googleApiClient?.isConnected!!) googleApiClient?.disconnect()

        if (chatProgressDialog?.isShowing!!) chatProgressDialog?.dismiss()

        if (claimProgressDialog?.isShowing!!) claimProgressDialog?.dismiss()
    }

    private fun addNewContact(toyViewModel: ToyViewModel, room: String) {
        val model = ContactModel()
        model.chatRoom = room
        model.contactAvatar = toyViewModel.avatarUrl
        model.contactName = toyViewModel.giverName
        model.contactId = toyViewModel.toyId
        model.contactUUID = toyViewModel.giverUUID

        val manager = ContactsManager(context)
        val subscription = manager.addNewContact(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            _ -> Timber.d("saved successfully")
                        },
                        {
                            error -> showMessage("Failed to save contact")
                            Timber.d(error)
                        }
                )

        subscriptions.add(subscription)
    }


    private fun createGoogleApi() {
        Timber.tag("LifeCycles");
        Timber.d("Activity Created");
        Timber.d("createGoogleApi()")
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()
        }
    }


    override fun onLocationChanged(location: Location?) {
        currentLocation = location
        if (numUpdates == 2) {
            stopLocationUpdates()
            //fetchItems()
        }
        else{
            numUpdates += 1
        }

    }


    private fun stopLocationUpdates() {
        if (googleApiClient?.isConnected!!) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this)
        }
    }


    override fun onConnected(p0: Bundle?) {
        Log.d("API", "connected")
        Timber.d("connected")
        checkLocationSettings()
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }



    private fun checkLocationSettings() {
        Timber.d("checking location settings")
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        //SettingsClient client = LocationServices.getSettingsClient(this);
        //Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())

        result.setResultCallback { locationSettingsResult ->
            val status = locationSettingsResult.status
            val locationSettingsStates = locationSettingsResult.locationSettingsStates

            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    Timber.d("location settings are correct")
                    //startLocationUpdates();
                    getLastKnownLocation()
                }

                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    Timber.d("location settings needs to be configured")
                    try {
                        status.startResolutionForResult(
                                activity,
                                REQUEST_CHECK_SETTINGS)
                    } catch (e: IntentSender.SendIntentException) {
                        e.printStackTrace()
                    }

                }

                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Timber.d( "location settings cannot be configured")
            }
        }

    }


    // Start location Updates
    private fun startLocationUpdates() {
        Timber.i("startLocationUpdates()")
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
        }
    }



    private fun getLastKnownLocation() {
        Timber.d("getLastKnownLocation()")

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
            Timber.i("LasKnown location. Long: ${currentLocation!!.longitude} | Lat: ${currentLocation!!.latitude}")
            startLocationUpdates()
            fetchItems()
        } else {
            Timber.w("No location retrieved yet")
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




    companion object {

        val TAG = ItemListFragment::class.java.simpleName

        fun newInstance(age: String, category: String): ItemListFragment {
            val arguments = Bundle()
            arguments.putString("age", age)
            arguments.putString("category", category)
            val frag = ItemListFragment()
            frag.arguments = arguments
            return frag
        }
    }

}// Required empty public constructor
