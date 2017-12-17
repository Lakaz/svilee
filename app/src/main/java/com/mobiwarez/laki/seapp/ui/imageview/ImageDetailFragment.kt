package com.mobiwarez.laki.seapp.ui.imageview


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobiwarez.laki.seapp.R
import com.mobiwarez.laki.seapp.extensions.loadImg
import com.mobiwarez.laki.seapp.util.StringConstants
import kotlinx.android.synthetic.main.fragment_image_detail.*


/**
 * A simple [Fragment] subclass.
 */
class ImageDetailFragment : Fragment() {

    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageUrl = arguments.getString(StringConstants.IMAGE_URL_KEY)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_image_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        full_image.loadImg(imageUrl!!)
    }

    companion object {

        val TAG: String = ImageDetailFragment::class.java.simpleName

        fun newInstance(imageUrl: String): ImageDetailFragment {
            val arguments = Bundle()
            arguments.putString(StringConstants.IMAGE_URL_KEY, imageUrl)
            val frag = ImageDetailFragment()
            frag.arguments = arguments
            return frag
        }
    }


}// Required empty public constructor
