package com.mobiwarez.laki.sville.ui.toys.create


import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.Toast
import com.bumptech.glide.Glide
import com.mobiwarez.laki.sville.R
import com.mobiwarez.laki.sville.extensions.loadImg
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.fragment_photo.*
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [PhotoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhotoFragment : Fragment() {

    private val REQUEST_CODE_CHOOSE = 206
    private val REQUEST_IMAGE_CAPTURE = 207

    private var actualImage: File? = null
    private var compressedImage: File? = null
    private var imagePath: String? = null

    private var mListener: PhotoFragment.Proceed? = null
    private var itemViewModel: NewToyViewModel? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        retainInstance = true

        itemViewModel = ViewModelProviders.of(activity).get(NewToyViewModel::class.java)


/*
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
*/
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_photo, menu)
        //super.onCreateOptionsMenu(menu, inflater)

        //inflater.inflate(R.menu.menu_details_fragment, menu);
        val view = menu?.findItem(R.id.action_proceed)?.actionView
        view?.setOnClickListener { mListener?.moveToDetails() }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        load_photo_gallery.setOnClickListener{ v-> loadImageFromGallery()}
        take_photo_camera.setOnClickListener{ v -> dispatchTakePictureIntent() }
        itemViewModel?.pictureUrl?.let {
            addImageText.visibility = View.GONE
            itemImage.loadImg(it)
        }
    }

    private fun loadImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_CHOOSE)
    }


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is PhotoFragment.Proceed) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    private fun storeImage(image: Bitmap) {
        //File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        actualImage = PhotoFragment.getOutputMediaFile(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {

            var bitmap: Bitmap? = null
            val selectedImage = data?.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(activity.contentResolver, selectedImage)
                    storeImage(bitmap)
                } catch (e: IOException ) {
                    Log.i("TAG", "Some exception " + e);
                }

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

            itemViewModel?.pictureUrl = imagePath
            addImageText.visibility = View.GONE

            Glide.with(this).load(Uri.fromFile(compressedImage))
                    .into(itemImage)


        }


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

            itemViewModel?.pictureUrl = imagePath
            addImageText.visibility = View.GONE

            Glide.with(this).load(Uri.fromFile(compressedImage))
                    .into(itemImage)
        }

    }

    private fun createTempFile(): File {
        return File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis().toString() + "_image.jpeg")
    }


    companion object {

        val TAG = PhotoFragment::class.java.simpleName


        fun newInstance(): PhotoFragment = PhotoFragment()

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

    }

    interface Proceed {
        fun moveToDetails()
    }

}// Required empty public constructor
