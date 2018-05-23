package com.mobiwarez.laki.sville.ui.toys.create

//import com.mobiwarez.laki.seapp.R.id.arts_image
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.provider.MediaStore
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.mobiwarez.laki.sville.R
import com.mobiwarez.laki.sville.ui.toys.models.CategoryModel
import com.mobiwarez.laki.sville.ui.toys.sharedtoys.SharedToysAdapter
import com.mobiwarez.laki.sville.util.showToast
import com.transitionseverywhere.Rotate
import com.transitionseverywhere.TransitionManager
import kotlinx.android.synthetic.main.fragment_new_create_toy.*
//import kotlinx.android.synthetic.main.fragment_photo.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import id.zelory.compressor.Compressor


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewCreateToyFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewCreateToyFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class NewCreateToyFragment : Fragment(), CategoryAdapter.dragListener, LowerCategoryAdapter.dragListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private var mBottomSheet: View? = null
    private var mBottomSheetDialog: BottomSheetDialog? = null
    private var mDialogBehavior: BottomSheetBehavior<*>? = null
    private var mBehavior: BottomSheetBehavior<*>? = null



    private val REQUEST_CODE_CHOOSE = 206
    private val REQUEST_IMAGE_CAPTURE = 207

    private var actualImage: File? = null
    private var compressedImage: File? = null
    private var imagePath: String? = null

    private var newToyViewModel: NewToyViewModel? = null



    private var categoryRecycler: RecyclerView? = null
    private var categoryLayoutManager: RecyclerView.LayoutManager? = null
    private var categoryAdapter: CategoryAdapter? = null

    private var lowerCategoryRecycler: RecyclerView? = null
    private var lowerCategoryLayoutManager: RecyclerView.LayoutManager? = null
    private var lowerCategoryAdapter: LowerCategoryAdapter? = null

    lateinit var firebaseAnalytics: FirebaseAnalytics

    var draggedView: View? = null
    //var wholeView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
        setHasOptionsMenu(true)
        newToyViewModel = ViewModelProviders.of(activity).get(NewToyViewModel::class.java)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_new_create_toy, container, false)

        categoryRecycler = view!!.findViewById(R.id.upper_menu_rv)
        lowerCategoryRecycler = view!!.findViewById(R.id.lower_menu_rv)
        initializeRecyclerView()


        return view
    }


    private fun initializeRecyclerView() {

        if (categoryRecycler!!.adapter == null) {
            categoryAdapter = CategoryAdapter(activity.applicationContext, this)
            categoryRecycler!!.adapter = categoryAdapter
        } else {
            categoryAdapter = categoryRecycler!!.adapter as CategoryAdapter
        }

        categoryLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoryRecycler!!.layoutManager = categoryLayoutManager



        if (lowerCategoryRecycler!!.adapter == null) {
            lowerCategoryAdapter = LowerCategoryAdapter(activity.applicationContext, this)
            lowerCategoryRecycler!!.adapter = lowerCategoryAdapter
        } else {
            lowerCategoryAdapter = lowerCategoryRecycler!!.adapter as LowerCategoryAdapter
        }

        lowerCategoryLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        lowerCategoryRecycler!!.layoutManager = lowerCategoryLayoutManager

    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_photo, menu)
        //super.onCreateOptionsMenu(menu, inflater)

        //inflater.inflate(R.menu.menu_details_fragment, menu);
        val view = menu?.findItem(R.id.action_proceed)?.actionView
        view?.setOnClickListener {

            if (TextUtils.isEmpty(newToyViewModel?.category)){
                activity.showToast("Please drag your gift into the box")

            }else {
                firebaseAnalytics.logEvent("move_to_posting_gift_clicked", null)
                EventBus.getDefault().post(MoveToDetailsMessage())
            }
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpDropListener()
        setUpBottomSheetDialog()

        image_clear_button.setOnClickListener {
            gift_image.setImageDrawable(null)
            image_clear_button.setImageResource(R.drawable.ic_add_a_photo_white_24dp)
            showBottomSheetDialog()
        }

        chosen_image.setOnClickListener {
            val dialog = ClearboxDialogFragment.newInstance()
            dialog.show(fragmentManager, ClearboxDialogFragment.TAG)
        }

        age_group_image.setOnClickListener {
            val dialog = AgeGroupDialogFragment.newInstance()
            dialog.show(fragmentManager, AgeGroupDialogFragment.TAG)
        }
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
        populateMenu()
        populateLowerMenu()
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }


    override fun setDraggeImage(image: ImageView, view: View) {
        draggedView = image
        //wholeView = view
    }

    @Subscribe
    fun emptyBox(message: EmptyBoxMessage) {
        chosen_image.setImageDrawable(null)
        //wholeView?.visibility = View.VISIBLE
        gift_image.setImageDrawable(null)
        image_clear_button.visibility = View.INVISIBLE
        age_group_image.visibility = View.INVISIBLE
        age_group_tv.text = null
        age_group_tv.visibility = View.INVISIBLE
        newToyViewModel?.ageGroup = null
        newToyViewModel?.category = null
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showChoosePhotoDialog(message: DoneSettingAgeMessage){
        age_group_tv.visibility = View.VISIBLE
        age_group_image.visibility = View.VISIBLE
        
        newToyViewModel?.ageGroup?.let { age_group_tv.text = it }
        showBottomSheetDialog()
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


    private fun populateMenu(){
        val menuItems = listOf(
                CategoryModel("Ars & Craft", R.drawable.arts),
                CategoryModel("Books", R.drawable.book),
                CategoryModel("Boy's Clothes", R.drawable.boy_clothes),
                CategoryModel("Girl's Clothes", R.drawable.girl_clothes))

        categoryAdapter?.onItemUpdate(menuItems)
    }

    private fun populateLowerMenu(){
        val menuItems = listOf(
                CategoryModel("Kid's Decor", R.drawable.kids_decor),
                CategoryModel("Meal Time", R.drawable.meal_time),
                CategoryModel("Toy's & Games", R.drawable.games),
                CategoryModel("Other", R.drawable.kids_decor))

        lowerCategoryAdapter?.onItemUpdate(menuItems)

    }

    private fun storeImage(image: Bitmap) {
        //File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        actualImage = NewCreateToyFragment.getOutputMediaFile(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
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

            newToyViewModel?.pictureUrl = imagePath
            //addImageText.visibility = View.GONE

            image_clear_button.setImageResource(R.drawable.ic_clear_white_24dp)
            Glide.with(this).load(Uri.fromFile(compressedImage))
                    .into(gift_image)


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

            newToyViewModel?.pictureUrl = imagePath
            //addImageText.visibility = View.GONE

            image_clear_button.setImageResource(R.drawable.ic_clear_white_24dp)
            Glide.with(this).load(Uri.fromFile(compressedImage))
                    .into(gift_image)
        }

    }



    private fun setUpBottomSheetDialog() {
        mBottomSheet = activity.findViewById(R.id.photobottomsheet)
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

    private fun showBottomSheetDialog(){

        image_clear_button.visibility = View.VISIBLE
        gift_image.visibility = View.VISIBLE

        if (mBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val view = layoutInflater.inflate(R.layout.photo_bottom_sheet, null)
        //view.findViewById<View>(R.id.fakeShadow).visibility = View.GONE

        val galleryButton = view.findViewById<FloatingActionButton>(R.id.gallery_button)
        val cameraButton = view.findViewById<FloatingActionButton>(R.id.camera_button)

        galleryButton.setOnClickListener {
            firebaseAnalytics.logEvent("load_image_from_gallery_clicked", null)
            loadImageFromGallery()
            mBottomSheetDialog!!.hide()
        }
        cameraButton.setOnClickListener {
            firebaseAnalytics.logEvent("take_gift_image_camera_clicked", null)
            dispatchTakePictureIntent()
            mBottomSheetDialog!!.hide()
        }

        mBottomSheetDialog = BottomSheetDialog(activity)
        mBottomSheetDialog!!.setContentView(view)
        mDialogBehavior = BottomSheetBehavior.from(view.parent as View)

        mBottomSheetDialog!!.show()
        mBottomSheetDialog!!.setOnDismissListener { dialog -> mBottomSheetDialog = null }


    }


    private fun setUpDropListener() {
        box_image.setOnDragListener { v, event ->
            val action = event?.action

            when (action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    //TransitionManager.beginDelayedTransition(left_cover_container, Rotate())
                    //left_cover_image.rotation = 135f
                    v?.alpha = 0.5f

                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    //v?.setBackgroundDrawable(enterShape)
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    //v?.setBackgroundDrawable(normalShape)
                    true
                }
                DragEvent.ACTION_DROP -> {

                    // Gets the item containing the dragged data
                    firebaseAnalytics.logEvent("gift_item_category_dropped", null)
                    val item = event.clipData.getItemAt(0)

                    newToyViewModel?.category = item.text.toString()

                    // Gets the text data from the item.
                    val category = item.text

                    //activity.showToast(category.toString())
                    // Dropped, reassign View to ViewGroup
                    //val view = event.getLocalState() as View
                    //val owner = view.parent as ViewGroup
                    //owner.removeView(view)
                    //val container = v as LinearLayout
                    //container.addView(view)
                    //view.visibility = View.VISIBLE

                    true
                }



                DragEvent.ACTION_DRAG_ENDED -> {
                    //v?.setBackgroundDrawable(normalShape)
                    v?.alpha = 1f

                    if (event.result) {

                        //draggedView?.visibility = View.INVISIBLE
                        //wholeView?.visibility = View.INVISIBLE

                        chosen_image.setImageDrawable((draggedView as ImageView).drawable)


                        val timer = Timer()

                        timer.schedule(object : TimerTask() {
                            override fun run() {
                                val ageGroupDialogFragment = AgeGroupDialogFragment.newInstance()
                                ageGroupDialogFragment.show(fragmentManager, AgeGroupDialogFragment.TAG)
                           }
                        }, 300L)

                        timer.purge()

                    }
                    true
                }
                else -> {
                    true
                }
            }// do nothing
            //true
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
/*
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
*/
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        val TAG = NewCreateToyFragment::class.java.simpleName
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewCreateToyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = NewCreateToyFragment()


        /** Create a file Uri for saving an image or video  */
        private fun getOutputMediaFileUri(type: Int): Uri {
            return Uri.fromFile(getOutputMediaFile(type))
        }

        /** Create a File for saving an image or video  */
        private fun getOutputMediaFile(type: Int): File? {
            // To be safe, you should check that the SDCard is mounted
            // using Environment.getExternalStorageState() before doing this.

            val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "sVille")
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("sVille", "failed to create directory")
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


    private class MyDragShadowBuilder// Defines the constructor for myDragShadowBuilder
    (v: View) : View.DragShadowBuilder(v) {

        private var shadow: Drawable

        init {

            // Creates a draggable image that will fill the Canvas provided by the system.
            shadow = ColorDrawable(Color.LTGRAY)
        }// Stores the View parameter passed to myDragShadowBuilder.

        // Defines a callback that sends the drag shadow dimensions and touch point back to the
        // system.
        override fun onProvideShadowMetrics(size: Point, touch: Point) {
            // Defines local variables
            val width: Int
            val height: Int

            // Sets the width of the shadow to half the width of the original View
            width = view.width * 2

            // Sets the height of the shadow to half the height of the original View
            height = view.height * 2

            // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
            // Canvas that the system will provide. As a result, the drag shadow will fill the
            // Canvas.
            //shadow.setBounds(0, 0, width, height)

            view.layoutParams.height = height
            view.layoutParams.width = width


            // Sets the size parameter's width and height values. These get back to the system
            // through the size parameter.
            size.set(width, height)

            // Sets the touch point's position to be in the middle of the drag shadow
            touch.set(width / 2, height / 2)
        }

        // Defines a callback that draws the drag shadow in a Canvas that the system constructs
        // from the dimensions passed in onProvideShadowMetrics().
        override fun onDrawShadow(canvas: Canvas) {

            // Draws the ColorDrawable in the Canvas passed in from the system.
            //shadow.draw(canvas)
            view.draw(canvas)
        }

        companion object {

            // The drag shadow image, defined as a drawable thing
            //private var shadow: Drawable
        }
    }


/*
    class DragListener: View.OnDragListener{

        override fun onDrag(v: View?, event: DragEvent?): Boolean {


            val action = event?.action
            when (action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    v?.alpha = 0.5f
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    //v?.setBackgroundDrawable(enterShape)
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    //v?.setBackgroundDrawable(normalShape)

                }
                DragEvent.ACTION_DROP -> {
                    // Dropped, reassign View to ViewGroup
                    //val view = event.getLocalState() as View
                    //val owner = view.parent as ViewGroup
                    //owner.removeView(view)
                    //val container = v as LinearLayout
                    //container.addView(view)
                    //view.visibility = View.VISIBLE
                    return true
                }
                DragEvent.ACTION_DRAG_ENDED ->  {
                    //v?.setBackgroundDrawable(normalShape)
                    v?.alpha = 1f

                }
                else -> {
                }
            }// do nothing
            return true
        }
    }
*/
}
