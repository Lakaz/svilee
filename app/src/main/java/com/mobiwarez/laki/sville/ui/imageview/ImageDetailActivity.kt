package com.mobiwarez.laki.sville.ui.imageview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.mobiwarez.laki.sville.R
import com.mobiwarez.laki.sville.util.StringConstants
import kotlinx.android.synthetic.main.activity_image_detail.*

class ImageDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)
        toolbar.title = "sVill"
        setSupportActionBar(toolbar)

        val imageUrl = intent.extras.getString(StringConstants.IMAGE_URL_KEY)


        // Set up activity to go full screen
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Enable some additional newer visibility and ActionBar features to create a more
        // immersive photo viewing experience

        val fragmentManager = supportFragmentManager

        if (fragmentManager.findFragmentByTag(ImageDetailFragment.TAG) == null){
            val imageDetailFragment = ImageDetailFragment.Companion.newInstance(imageUrl)
            fragmentManager.beginTransaction().add(R.id.full_image_container, imageDetailFragment, ImageDetailFragment.TAG).commit()
        }



    }

}
