package com.mobiwarez.laki.seapp.ui.toys.sharedtoys


import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.mobiwarez.data.givenToys.db.model.GivenToyModel
import com.mobiwarez.laki.seapp.R
import com.mobiwarez.laki.seapp.fragments.RxBaseFragment
import com.mobiwarez.laki.seapp.remotebackend.RepoHandler
import com.mobiwarez.laki.seapp.ui.imageview.ImageDetailActivity
import com.mobiwarez.laki.seapp.ui.toys.create.NewToActivity
import com.mobiwarez.laki.seapp.util.DatabaseManger
import com.mobiwarez.laki.seapp.util.StringConstants
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
//import com.example.laki.myapplication.backend.myApi.model.ItemStatusResponse


/**
 * A simple [Fragment] subclass.
 */
class SharedToysFragment : RxBaseFragment(), SharedToysContract.View, SharedToysAdapter.EditToy {

    var presenter: SharedToysContract.Presenter? = null

    private var fab: FloatingActionButton? = null

    private var editedModel: GivenToyModel? = null
    private var editedPosition: Int? = null

    private var sharedToysRecycler: RecyclerView? = null
    private var sharedToysLayoutManager: RecyclerView.LayoutManager? = null

    private var sharedToysAdapter: SharedToysAdapter? = null

    private val REQUEST_IMAGE_CAPTURE : Int = 134
    private val PICK_IMAGE_REQUEST: Int = 135

    private var actualImage: File? = null
    private var compressedImage: File? = null
    private var imagePath: String? = null
    private var deleteProgress: ProgressDialog? = null



    //var view: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = SharedToysPresenter(this)
        deleteProgress = ProgressDialog(context)
        deleteProgress?.isIndeterminate = true
        deleteProgress?.setTitle("Deleting item")
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_shared_toys, container, false)

        fab = activity.findViewById(R.id.fab)
        fab!!.setOnClickListener { _ -> startCreate() }
        sharedToysRecycler = view!!.findViewById(R.id.sharedtoys_recycler)
        initializeRecyclerView()
        return view
    }

    private fun startCreate() {
        val intent = Intent(context, NewToActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        //presenter.loadItems();
        getItems()
    }

    override fun showItems(toyViewModels: List<GivenToyModel>) {
        if (toyViewModels.isEmpty()) {
            showMessage("You have not given any items as yet")
        }
        sharedToysAdapter!!.onToysUpdate(toyViewModels)
    }

    override fun showLoading() {

    }

    override fun setItemStatus() {

    }

    override fun showMessage(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showUpdateButton() {

    }

    override fun showEditCategory() {

    }

    override fun showEditAgeGroup() {

    }

    override fun showEditDescription() {

    }


    override fun onPause() {
        super.onPause()
        if (deleteProgress?.isShowing!!) deleteProgress?.dismiss()
    }

    private fun getItems() {
        val databaseManger = DatabaseManger(context)
        val disposabele = databaseManger.getGivenItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            ite -> showItems(ite.reversed())
                        },
                        {
                            err -> showMessage("Failed to load items you offered")
                        })

        subscriptions.add(disposabele)
    }


    private fun initializeRecyclerView() {
        if (sharedToysRecycler!!.adapter == null) {
            sharedToysAdapter = SharedToysAdapter(activity.applicationContext, this)
            sharedToysRecycler!!.adapter = sharedToysAdapter
        } else {
            sharedToysAdapter = sharedToysRecycler!!.adapter as SharedToysAdapter
        }
        sharedToysLayoutManager = LinearLayoutManager(null)
        sharedToysRecycler!!.layoutManager = sharedToysLayoutManager
    }


    override fun editAgeGroup(givenToyModel: GivenToyModel, position: Int) {

        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Select Age Group")
                .setItems(R.array.age_group_array) { dialog, which ->
                    var newAge = (dialog as AlertDialog).listView.adapter.getItem(which).toString()
                    when (newAge) {
                        "Baby: 0 to 2 years" -> newAge = "baby"
                        "Toddler: 3 to 5 years" -> newAge = "toddler"
                        "Kid: 5 years+" -> newAge = "kid"
                    }

                    givenToyModel.toyAgeGroup = newAge
                    sharedToysAdapter!!.updateItem(givenToyModel, position)
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    override fun editCategory(givenToyModel: GivenToyModel, position: Int) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Select Item Category")
                .setItems(R.array.category_array) { dialog, which ->
                    val newCategory = (dialog as AlertDialog).listView.adapter.getItem(which).toString()
                    givenToyModel.toyCategory = newCategory
                    sharedToysAdapter!!.updateItem(givenToyModel, position)
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    override fun editDescription(givenToyModel: GivenToyModel, position: Int) {
        val builder = AlertDialog.Builder(activity)
        val view = activity.layoutInflater.inflate(R.layout.edit_item_description, null)
        val descriptionView = view.findViewById<EditText>(R.id.item_description_edit) as EditText
        descriptionView.setText(givenToyModel.toyDescription)

        builder.setTitle("Edit Description")
        builder.setView(view)
                .setPositiveButton("Edit") { _, _ ->
                    val newDescription = descriptionView.text.toString()
                    if (!TextUtils.isEmpty(newDescription)) {
                        givenToyModel.toyDescription = newDescription
                        sharedToysAdapter?.updateItem(givenToyModel, position)
                    }
                }
                .setNegativeButton("Cancel") { _, _ ->

                }

        builder.show()

    }

    override fun editImage(givenToyModel: GivenToyModel, position: Int) {
        editedModel = givenToyModel
        editedPosition = position
        val items = arrayOf<CharSequence>("Take Photo", "Choose from gallery", "Cancel")
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add Photo")
        builder.setItems(items) { dialogInterface, item ->
            when {
                items[item] == "Take Photo" -> dispatchTakePictureIntent()
                items[item] == "Choose from gallery" -> loadImageFromGallery()
                else -> dialogInterface.dismiss()
            }
        }

        builder.show()

    }


    override fun showFullImage(imageUrl: String) {
        val intent = Intent(context, ImageDetailActivity::class.java)
        intent.putExtra(StringConstants.IMAGE_URL_KEY, imageUrl)
        startActivity(intent)
    }



    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun loadImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun storeImage(image: Bitmap) {
        //File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        actualImage = SharedToysFragment.getOutputMediaFile(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
        if (actualImage == null) {
            Timber.d("Error creating media file, check storage permissions: ")
            return
        }
        try {
            val fos = FileOutputStream(actualImage!!)
            image.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.close()
        } catch (e: FileNotFoundException) {
            Timber.d("File not found: "+ e.message)
        } catch (e: IOException) {
            Timber.d("Error accessing file: "+ e.message)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {


            val extras = data!!.extras
            val imageBitmap = extras!!.get("data") as Bitmap

            storeImage(imageBitmap)

            try {
                compressedImage = Compressor(context)
                        .setMaxWidth(1198)
                        .setMaxHeight(2409)
                        .setQuality(90)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).absolutePath)
                        .compressToFile(actualImage!!)

            } catch (e: IOException) {
                e.printStackTrace()
            }

            imagePath = compressedImage!!.toString()

            editedModel?.toyImagePath = imagePath

            sharedToysAdapter?.updateItem(editedModel!!, editedPosition!!)


        }


    }

    override fun updateModel(givenToyModel: GivenToyModel, position: Int) {

        val builder = AlertDialog.Builder(activity)

        //builder.setTitle(" Item")
        builder.setMessage("You are about to update an item you have offered")
                .setPositiveButton("Update") { _, _ ->
                    doUpdate(givenToyModel, position)
                }
                .setNegativeButton("Cancel") { _, _ ->

                }

        builder.show()
    }

    private fun doUpdate(givenToyModel: GivenToyModel, position: Int) {
        val repoHandler = RepoHandler()
        val subscription = repoHandler.updateItem(givenToyModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {

                        },
                        { err ->
                            showMessage("Sorry, failed to update item")
                        },
                        {
                            updateTodatabase(givenToyModel, position)
                        }
                )

        subscriptions.add(subscription)
    }

    fun updateTodatabase(model: GivenToyModel, position: Int) {
        val manager = DatabaseManger(context)
        val subscription = manager.updateGivenItem(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            response -> showMessage("Item updated successfully")
                        },
                        {
                            err -> showMessage("failed to update item")
                        },
                        {
                            sharedToysAdapter?.updateItem(model, position)
                        }
                )

        subscriptions.add(subscription)
    }

    override fun deleteModel(givenToyModel: GivenToyModel, position: Int) {

        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Delete Item")
        builder.setMessage("You are about to delete an item you have offered")
                .setPositiveButton("Delete") { _, _ ->
                    doDelete(givenToyModel, position)
                }
                .setNegativeButton("Cancel") { _, _ ->

                }

        builder.show()

    }

    private fun doDelete(givenToyModel: GivenToyModel, position: Int) {
        if (givenToyModel.isToySynced == 1) {
            deleteProgress?.show()
            val repo = RepoHandler()
            val subscription = repo.deleteItemListing(givenToyModel.toyId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                response -> deleteFromDb(givenToyModel, position)
                                deleteProgress?.dismiss()
                            },
                            {
                                err -> showMessage("Failed to delete item. Try again")
                                deleteProgress?.dismiss()
                            }
                    )

            subscriptions.add(subscription)

        } else {
            deleteFromDb(givenToyModel, position)
        }
    }

    private fun deleteFromDb(givenToyModel: GivenToyModel, position: Int) {
        val manager = DatabaseManger(context)
        val subscription = manager.deleteGivenItem(givenToyModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { response ->
                            showMessage("Item successfully deleted")
                        },
                        { err ->
                            showMessage("Sorry, failed to delete item")
                        },
                        {
                            sharedToysAdapter?.deleteItem(position)
                        }
                )

        subscriptions.add(subscription)
    }


    override fun editTitle(givenToyModel: GivenToyModel, position: Int) {
        val builder = AlertDialog.Builder(activity)
        val view = activity.layoutInflater.inflate(R.layout.edit_item_title, null)
        val newTitle = view.findViewById<EditText>(R.id.title_edit) as EditText

        newTitle.setText(givenToyModel.itemTitle)

        builder.setTitle("Edit Title")
        builder.setView(view)
                .setPositiveButton("Edit") { _, _ ->
                    val titleNew = newTitle.text.toString()
                    if (!TextUtils.isEmpty(titleNew)) {
                        givenToyModel.itemTitle = titleNew
                        sharedToysAdapter?.updateItem(givenToyModel, position)
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }

        builder.show()
    }

    override fun editPickLocation(givenToyModel: GivenToyModel, position: Int) {
        val builder = AlertDialog.Builder(activity)
        val view = activity.layoutInflater.inflate(R.layout.edit_item_pick_location, null)
        val location = view.findViewById<EditText>(R.id.pick_location_edit) as EditText
        location.setText(givenToyModel.pickUpLocation)

        builder.setTitle("Edit Pick Up location")
        builder.setView(view)
                .setPositiveButton("Edit") { _, _ ->
                    val newLocation = location.text.toString()
                    if (!TextUtils.isEmpty(newLocation)) {
                        givenToyModel.pickUpLocation = newLocation
                        sharedToysAdapter?.updateItem(givenToyModel, position)
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
        builder.show()
    }

    override fun editPickTime(givenToyModel: GivenToyModel, position: Int) {
        val builder = AlertDialog.Builder(activity)
        val view = activity.layoutInflater.inflate(R.layout.edit_item_pick_time, null)
        val timeTextView = view.findViewById<EditText>(R.id.pick_time_edit) as EditText
        timeTextView.setText(givenToyModel.pickUpTime)

        builder.setTitle("Edit Pick Up Time")
        builder.setView(view)
                .setPositiveButton("Edit") { _, _ ->
                    val titleNew = timeTextView.text.toString()
                    if (!TextUtils.isEmpty(titleNew)) {
                        givenToyModel.pickUpTime = titleNew
                        sharedToysAdapter?.updateItem(givenToyModel, position)
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
        builder.show()
    }

    override fun checkItemStatus(givenToyModel: GivenToyModel, position: Int) {
        doStatusCheck(givenToyModel, position)
    }

    private fun doStatusCheck(givenToyModel: GivenToyModel, position: Int){
        val repoHandler = RepoHandler()
        val subscription = repoHandler.getItemStatus(givenToyModel.toyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            response ->
                            if (!TextUtils.isEmpty(response.takerName)){
                                givenToyModel.isToyGiven = 1
                                givenToyModel.receiverName = response.takerName
                                givenToyModel.timeReceived = response.timeTaken
                                updateTodatabase(givenToyModel, position)
                            }else {

                            }
                        },
                        {

                        }
                )

        subscriptions.add(subscription)
    }


    companion object {

        val TAG = SharedToysFragment::class.java.simpleName

        fun newInstance(): SharedToysFragment {
            return SharedToysFragment()
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
                    Environment.DIRECTORY_PICTURES), "SeeApp")
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    //Log.d("MyCameraApp", "failed to create directory")
                    Timber.d("failed to create to create to directory")
                    return null
                }
            }


            // Create a media file name
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val mediaFile: File
            if (type == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {

                mediaFile = File(mediaStorageDir.path + File.separator +
                        "IMG_" + timeStamp + ".jpg")
            } else if (type == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                mediaFile = File(mediaStorageDir.path + File.separator +
                        "VID_" + timeStamp + ".mp4")
            } else {
                return null
            }

            return mediaFile
        }

    }
}// Required empty public constructor
