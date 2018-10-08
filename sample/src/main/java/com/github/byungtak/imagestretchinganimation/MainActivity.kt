package com.github.byungtak.imagestretchinganimation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.byungtak.library.ImageContainer
import com.github.byungtak.library.ImageStretchingAnimation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        image_stretching_animation.setViewId(R.id.image_stretching_animation)
        image_stretching_animation.setImageClickListener(object : ImageStretchingAnimation.ImageTouchListener {
            override fun onImageDelivered(selectedImage: ImageContainer) {

            }
        })
    }
}
