package com.mobiwarez.laki.sville.ui.profile

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.UploadTask
import com.mobiwarez.laki.sville.R
import com.mobiwarez.laki.sville.extensions.loadAvatar
import com.mobiwarez.laki.sville.prefs.GetUserData
import com.mobiwarez.laki.sville.prefs.UserInfo
import com.mobiwarez.laki.sville.ui.toys.create.CreateToyFragment
import com.mobiwarez.laki.sville.ui.toys.create.NewToyViewModel
import com.mobiwarez.laki.sville.ui.toys.create.PhotoFragment
import com.mobiwarez.laki.sville.util.IdGenerator
import id.zelory.compressor.Compressor

import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.content_profile.*
import kotlinx.android.synthetic.main.fragment_photo.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private val REQUEST_CODE_CHOOSE = 206
    private val REQUEST_IMAGE_CAPTURE = 207

    private var actualImage: File? = null
    private var compressedImage: File? = null
    private var imagePath: String? = null

    private var profileViewModel: ProfileViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        profileViewModel?.imagePath = GetUserData.getRealPhotoUrl(this)

        profile_image.loadAvatar(GetUserData.getRealPhotoUrl(this))

        update_profile_btn.setOnClickListener { _ -> uploadProfilePhoto() }


        load_profile_photo.setOnClickListener { _-> showImageDialog() }
    }

    private var photoDownloadUrl: String? = null

    private fun uploadProfilePhoto() {

            val storageReference = FirebaseStorage.getInstance().reference
            val postsImagesRef = storageReference.child("images/+" + IdGenerator.getFileName() + ".jpg")

            postsImagesRef.putFile(Uri.fromFile(compressedImage))
                    .addOnSuccessListener { taskSnapshot ->
                        photoDownloadUrl = taskSnapshot.downloadUrl!!.toString()
                    }
                    .addOnFailureListener {
                        //uploadSuccess = false
                        showFailedMessage()
                    }
                    .addOnCompleteListener { SaveImagePostToPreferences() }
                    .addOnProgressListener {
                        /*
                                                Uri sessionUri = taskSnapshot.getUploadSessionUri();
                                                if (sessionUri != null && !saved) {
                                                    saved = true;
                                                    // A persisted session has begun with the server.
                                                    // Save this to persistent storage in case the process dies.
                                                }
                        */
                    }


    }

    private fun SaveImagePostToPreferences() {
            UserInfo.saveUserProfileUrl(photoDownloadUrl, this)
            showSuccess()

    }


    private fun showSuccess() {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), "Success", Snackbar.LENGTH_SHORT)
        val snackbarLayout = snackbar.view
        val textView = snackbarLayout.findViewById<View>(android.support.design.R.id.snackbar_text) as TextView
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_green_24dp, 0, 0, 0)
        textView.compoundDrawablePadding = resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding)
        snackbar.show()
    }

    private fun showFailedMessage() {
        val mySnackbar = Snackbar.make(findViewById(android.R.id.content), "Failed to update profile photo", Snackbar.LENGTH_SHORT)
        mySnackbar.setAction("Try Again", RetryUpdateListener())
        mySnackbar.show()
    }

    inner class RetryUpdateListener : View.OnClickListener {

        override fun onClick(v: View) {

            uploadProfilePhoto()
        }
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        profileViewModel?.imagePath?.let {
            profile_image.loadAvatar(it)
            //update_profile_btn.visibility = View.VISIBLE
        }
    }

    private fun showImageDialog(){
        val items = arrayOf<CharSequence>("Take Photo", "Choose from gallery", "Cancel")
        val builder = AlertDialog.Builder(this)
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

    private fun loadImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_CHOOSE)
    }


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(this.packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }


    private fun storeImage(image: Bitmap) {
        //File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        actualImage = getOutputMediaFile(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
        if (actualImage == null) {
            Log.d(CreateToyFragment.TAG,
                    "Error creating media file, check storage permissions: ")// e.getMessage());
            return
        }
        try {
            val fos = FileOutputStream(actualImage!!)
            image.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.close()
        } catch (e: FileNotFoundException) {
            Log.d(CreateToyFragment.TAG, "File not found: " + e.message)
        } catch (e: IOException) {
            Log.d(CreateToyFragment.TAG, "Error accessing file: " + e.message)
        }

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
                Log.d("MyCameraApp", "failed to create directory")
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



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {

            var bitmap: Bitmap? = null
            val selectedImage = data?.data
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                storeImage(bitmap)
            } catch (e: IOException) {
                Log.i("TAG", "Some exception " + e);
            }

            try {
                compressedImage = Compressor(this)
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

            update_profile_btn.visibility = View.VISIBLE
            imagePath = compressedImage!!.toString()
            profileViewModel?.imagePath = imagePath
            Glide.with(this).load(Uri.fromFile(compressedImage))
                    .into(profile_image)
        }


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {


            val extras = data!!.extras
            val imageBitmap = extras!!.get("data") as Bitmap

            storeImage(imageBitmap)

            try {
                compressedImage = Compressor(this)
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

            profileViewModel?.imagePath = imagePath

            update_profile_btn.visibility = View.VISIBLE

            Glide.with(this).load(Uri.fromFile(compressedImage))
                    .into(profile_image)
        }

    }

}
