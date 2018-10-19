package com.github.byungtak.library

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Point
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.image_stretching_animation_layout.view.*


class ImageStretchingAnimation @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null

): FrameLayout(context, attributeSet), View.OnClickListener {

    companion object {
        private const val IMAGE_STRETCHING_DURATION = 250L
    }

    interface ImageTouchListener {
        fun onImageDelivered(index: Int)
    }

    private var imageTouchListener: ImageTouchListener? = null

    private var images: MutableList<AppCompatImageView>? = null
    private var imageNames: MutableList<String>?         = null

    private val imagePoints = mutableListOf<Point>()

    private var containerViewId: Int? = null

    private var deviceHeightInPixel: Int? = null

    private var displayingImageName = ""

    private var animationExpanded = false

    private var lastImageIndex = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.image_stretching_animation_layout, this)

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()

        display.getSize(size)

        deviceHeightInPixel = size.y
    }

    override fun onClick(view: View?) {
        val length = images?.count() ?: 0

        if(animationExpanded) {
            clearImageCaching(length)

            if(view?.id == R.id.image_displaying) {
                expandStretchingImages(false, ViewGroup.LayoutParams.WRAP_CONTENT, INVISIBLE, length)

                return
            } else {
                var selectedId = 0

                for(index in 0 until length) {
                    val imageId   = images?.get(index)?.tag
                    val clickedId = view?.tag

                    if(imageId == clickedId) {
                        selectedId = index

                        break
                    }
                }

                val lastImageName = displayingImageName

                setDisplayingImage(imageNames?.get(selectedId) ?: "")

                imageNames?.removeAt(selectedId)
                imageNames?.let {
                    setStretchingTargetImages(it)
                }

                expandStretchingImages(false, ViewGroup.LayoutParams.WRAP_CONTENT, View.INVISIBLE, images?.count() ?: 0)
                setStretchingTargetImage(lastImageName, lastImageIndex)
            }
        } else {
            expandStretchingImages(true, deviceHeightInPixel ?: 2560.toPx, View.VISIBLE,length)
        }
    }

    fun setDisplayingImage(imageName: String) {
        val imageResouceId = imageName.getImageResource(context)

        image_displaying.setImageResource(imageResouceId)
        image_displaying.setOnClickListener(this)

        displayingImageName = imageName
    }

    fun setStretchingTargetImages(vararg drwableImageNames: String) {
        images = mutableListOf()
        imageNames = mutableListOf()

        for(index in 0 until drwableImageNames.count()) {
            setStretchingTargetImage(drwableImageNames[index], index)

            imagePoints.add(Point(0, 200 + (200*index)))
        }

        lastImageIndex = (images?.count() ?: 1) - 1
    }

    fun setStretchingTargetImages(drwableImageNames: MutableList<String>) {
        images = mutableListOf()
        imageNames = mutableListOf()

        for(index in 0 until drwableImageNames.count()) {
            setStretchingTargetImage(drwableImageNames[index], index)
        }
    }

    fun setContainerViewId(containerViewId: Int) {
        this.containerViewId = containerViewId
    }

    fun getAnimationExpanded(): Boolean = animationExpanded

    fun setImageClickListener(imageTouchListener: ImageTouchListener) {
        this.imageTouchListener = imageTouchListener
    }

    private fun expandStretchingImages(expanding: Boolean, viewGroupSize: Int, visibility: Int, length: Int) {
        this.animationExpanded = expanding

        setLayoutHeight(viewGroupSize)

        for(index in 0 until length) {
            images?.get(index)?.visibility = visibility

            if(expanding) {
                playAnimation(index)
            } else {
                stopAnimation(index)
            }
        }
    }

    private fun stopAnimation(index: Int) {
        val imageAnimatorY = ValueAnimator.ofFloat(imagePoints[index].y.toFloat(), 0F).apply {
            addUpdateListener { animation ->
                images?.get(index)?.y = animation.animatedValue as Float
                images?.get(index)?.requestLayout()
            }

            duration = IMAGE_STRETCHING_DURATION
        }

        AnimatorSet().apply {
            play(imageAnimatorY)
            start()
        }
    }

    private fun playAnimation(index: Int) {
        val imageAnimatorY = ValueAnimator.ofFloat(0F, imagePoints[index].y.toFloat()).apply {
            addUpdateListener { animation ->
                images?.get(index)?.y = animation.animatedValue as Float
                images?.get(index)?.requestLayout()
            }

            duration = IMAGE_STRETCHING_DURATION
        }

        AnimatorSet().apply {
            play(imageAnimatorY)
            start()
        }
    }

    private fun setStretchingTargetImage(imageName: String, index: Int) {
        val imageResouceId = resources.getIdentifier(imageName, "drawable", context.packageName)

        val animatedImage = AppCompatImageView(context)

        animatedImage.setImageResource(imageResouceId)
        animatedImage.setOnClickListener(this)
        animatedImage.tag = index
        animatedImage.visibility = View.INVISIBLE

        layout_image_container.addView(animatedImage)

        val layoutParams = image_displaying.layoutParams

        layoutParams?.width  = 52.toPx
        layoutParams?.height = 52.toPx

        animatedImage.layoutParams = layoutParams

        images?.add(animatedImage)
        imageNames?.add(imageName)
    }

    private fun clearImageCaching(length: Int) {
        for(index in 0 until length) {
            images?.get(index)?.setImageResource(android.R.color.transparent)
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