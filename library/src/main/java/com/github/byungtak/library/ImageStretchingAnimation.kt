package com.github.byungtak.library

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Point
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.image_stretching_animation_layout.view.*


class ImageStretchingAnimation @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null

): FrameLayout(context, attributeSet), View.OnClickListener {

    companion object {
        private const val IMAGE_STRETCHING_DURATION = 250L

        private const val IMAGE_DEFAULT_WIDTH  = 52
        private const val IMAGE_DEFAULT_HEIGHT = 52

        private const val IMAGE_STRETCHING_START = 0F
    }

    interface ImageTouchListener {
        fun onImageDelivered(index: Int)
    }

    private var imageTouchListener: ImageTouchListener? = null

    private val imagePoints = mutableListOf<Point>()
    private val images      = mutableListOf<AppCompatImageView>()
    private var imageNames  = mutableListOf<String>()

    private var containerViewId: Int? = null

    private var deviceHeightInPixel: Int? = null

    private var displayingImageName = ""

    private var animationExpanded = false

    private var lastImageIndex = 0
    private var length         = 0

    private var lastImageName          = ""
    private var selectedId             = 0
    private var displayingImageClicked = false

    private val imagesShowListener = object : SimpleAnimatorListener() {
        override fun onAnimationStart(animation: Animator?) {
            setLayoutHeight(deviceHeightInPixel ?: 2560.toPx)

            for(index in 0 until images.count()) {
                images[index].visibility = View.VISIBLE
            }
        }
    }

    private val imagesHideListener = object : SimpleAnimatorListener() {
        override fun onAnimationEnd(animation: Animator?) {
            for(index in 0 until images.count()) {
                images[index].visibility = View.INVISIBLE
            }

            if(displayingImageClicked) {
                displayingImageClicked = false

                return
            }

            clearImageCaching(length)

            setDisplayingImage(imageNames[selectedId])

            imageNames.removeAt(selectedId)

            setStretchingTargetImage(lastImageName, lastImageIndex)
            setStretchingTargetImages(imageNames)
            setLayoutHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.image_stretching_animation_layout, this)

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display       = windowManager.defaultDisplay
        val size          = Point()

        display.getSize(size)

        deviceHeightInPixel = size.y
    }

    override fun onClick(view: View?) {
        if(animationExpanded) {
            if(view?.id == R.id.image_displaying) {
                expandStretchingImages(false, images.count())

                displayingImageClicked = true
            } else {
                length = images.count()

                for(index in 0 until images.count()) {
                    val imageId   = images[index].tag
                    val clickedId = view?.tag

                    if(imageId == clickedId) {
                        selectedId = index

                        break
                    }
                }

                lastImageName = displayingImageName

                expandStretchingImages(false, images.count())

                imageTouchListener?.onImageDelivered(selectedId)
            }
        } else {
            expandStretchingImages(true, images.count())
        }
    }

    fun setImagesInterval(interval: Int) {
        for(index in 0 until imageNames.count()) {
            imagePoints.add(Point(0, interval + (interval * index)))
        }
    }

    fun setContainerViewId(containerViewId: Int) {
        this.containerViewId = containerViewId
    }

    fun setImageClickListener(imageTouchListener: ImageTouchListener) {
        this.imageTouchListener = imageTouchListener
    }

    fun setDisplayingImage(imageName: String) {
        val imageResouceId = imageName.getImageResource(context)

        image_displaying.setImageResource(imageResouceId)
        image_displaying.setOnClickListener(this)

        displayingImageName = imageName
    }

    fun setStretchingTargetImages(vararg drwableImageNames: String) {
        for(index in 0 until drwableImageNames.count()) {
            images.add(AppCompatImageView(context))

            setStretchingTargetImage(drwableImageNames[index], index)
        }

        lastImageIndex = images.count() - 1
    }

    private fun setStretchingTargetImages(drwableImageNames: MutableList<String>) {
        imageNames = mutableListOf()

        for(index in 0 until drwableImageNames.count()) {
            setStretchingTargetImage(drwableImageNames[index], index)
        }
    }

    private fun setStretchingTargetImage(imageName: String, index: Int) {
        val imageResouceId = resources.getIdentifier(imageName, "drawable", context.packageName)

        val stretchedImage = images[index]

        stretchedImage.setImageResource(imageResouceId)
        stretchedImage.setOnClickListener(this)
        stretchedImage.tag = index

        if(stretchedImage.parent == null) {
            stretchedImage.visibility = View.INVISIBLE

            layout_image_container.addView(stretchedImage)
        }

        val layoutParams = image_displaying.layoutParams

        layoutParams?.width  = IMAGE_DEFAULT_WIDTH.toPx
        layoutParams?.height = IMAGE_DEFAULT_HEIGHT.toPx

        stretchedImage.layoutParams = layoutParams

        imageNames.add(imageName)
    }

    private fun expandStretchingImages(expanding: Boolean, length: Int) {
        animationExpanded = expanding

        if(expanding) {
            for(index in 0 until length) {
                playAnimation(index)
            }
        } else {
            for(index in 0 until length) {
                stopAnimation(index)
            }
        }
    }

    private fun stopAnimation(index: Int) {
        ValueAnimator.ofFloat(imagePoints[index].y.toFloat(), IMAGE_STRETCHING_START).apply {
            addUpdateListener { animation ->
                images[index].y = animation.animatedValue as Float
                images[index].requestLayout()
            }

            if(index == 0) {
                addListener(imagesHideListener)
            }

            duration = IMAGE_STRETCHING_DURATION

        }.start()
    }

    private fun playAnimation(index: Int) {
        ValueAnimator.ofFloat(IMAGE_STRETCHING_START, imagePoints[index].y.toFloat()).apply {
            addUpdateListener { animation ->
                images[index].y = animation.animatedValue as Float
                images[index].requestLayout()
            }

            if(index == 0) {
                addListener(imagesShowListener)
            }

            duration = IMAGE_STRETCHING_DURATION

        }.start()
    }

    private fun clearImageCaching(length: Int) {
        for(index in 0 until length) {
            images[index].setImageResource(android.R.color.transparent)
        }
    }

    private fun setLayoutHeight(height: Int) {
        val animationView = containerViewId?.let {
            val rootView = rootView as ViewGroup
            rootView.findViewById<ImageStretchingAnimation>(it)
        }

        val layoutParams = animationView?.layoutParams

        layoutParams?.height = height

        animationView?.layoutParams = layoutParams
    }

}