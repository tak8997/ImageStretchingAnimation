package com.github.byungtak.imagestretchinganimation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.byungtak.library.ImageStretchingAnimation
import kotlinx.android.synthetic.main.activity_main.*

internal class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        image_stretching_animation.setContainerViewId(R.id.image_stretching_animation)
        image_stretching_animation.setDisplayingImage("avatar_dave")
        image_stretching_animation.setStretchingTargetImages(
                "avatar_lucy",
                "avatar_valarie",
                "avatar_henry",
                "avatar_albert"
        )
        image_stretching_animation.setImagesInterval(250)
        image_stretching_animation.setImageClickListener(object : ImageStretchingAnimation.ImageTouchListener {
            override fun onImageDelivered(index: Int) {
                Toast.makeText(this@MainActivity, "clicked", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
