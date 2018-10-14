package com.github.byungtak.library

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Point
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.image_stretching_animation_layout.view.*


class ImageStretchingAnimation @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null

): FrameLayout(context, attributeSet), View.OnClickListener {

    companion object {
        private const val SCALE_DOWN_DURATION = 250L
    }

    interface ImageTouchListener {
        fun onImageDelivered(selectedImage: ImageContainer)
    }

    private var imageTouchListener: ImageTouchListener? = null

    private val imageContainers = mutableListOf<ImageContainer>()

    private var containerViewId: Int? = null
    private var displayingViewId: Int = 99

    private var displayingImage = AppCompatImageView(context)

    private var animationExpanded = false

    init {
        LayoutInflater.from(context).inflate(R.layout.image_stretching_animation_layout, this)
    }

    override fun onClick(view: View?) {
        if(animationExpanded) {
            if(view?.id == displayingViewId) {
                setAnimationExpanded(false)
                setLayoutHeight(ViewGroup.LayoutParams.WRAP_CONTENT)

                for(i in 0 until imageContainers.count()) {
                    imageContainers[i].image.visibility = View.INVISIBLE

                    stopAnimation(i)
                }

                return
            } else {
                var selectedId = 0

                for(i in 0 until imageContainers.count()) {
                    val imageId   = imageContainers[i].image.tag
                    val clickedId = view?.tag

                    if(imageId == clickedId) {
                        selectedId = i

                        break
                    }
                }

                val selectedImage = imageContainers[selectedId]

                displayingImage.setImageResource(selectedImage.imageResourceId)





                setAnimationExpanded(false)
                setLayoutHeight(ViewGroup.LayoutParams.WRAP_CONTENT)

                for(i in 0 until imageContainers.count()) {
                    imageContainers[i].image.visibility = View.INVISIBLE

                    stopAnimation(i)
                }

            }
        }
        else {
            setLayoutHeight(500.toPx)
            setAnimationExpanded(true)

            for(i in 0 until imageContainers.count()) {
                imageContainers[i].image.visibility = View.VISIBLE

                playAnimation(i)
            }
        }
    }

    fun stopAnimation(index: Int) {
        val imageAnimatorY = ValueAnimator.ofFloat(imageContainers[index].imagePoint.y.toFloat(), 0F).apply {
            addUpdateListener { animation ->
                imageContainers[index].image.y = animation.animatedValue as Float
                imageContainers[index].image.requestLayout()
            }

            duration = SCALE_DOWN_DURATION
        }

        AnimatorSet().apply {
            play(imageAnimatorY)
            start()
        }
    }

    fun playAnimation(index: Int) {
        val imageAnimatorY = ValueAnimator.ofFloat(0F, imageContainers[index].imagePoint.y.toFloat()).apply {
            addUpdateListener { animation ->
                imageContainers[index].image.y = animation.animatedValue as Float
                imageContainers[index].image.requestLayout()
            }

            duration = SCALE_DOWN_DURATION
        }

        AnimatorSet().apply {
            play(imageAnimatorY)
            start()
        }
    }

    fun setDisplayingImage(image: String) {
        val imageResouceId = resources.getIdentifier(image, "drawable", context.packageName)

        displayingImage.setImageResource(imageResouceId)
        displayingImage.setOnClickListener(this)
        displayingImage.id = displayingViewId

        layout_image_container.addView(displayingImage)

        val layoutParams = displayingImage.layoutParams

        layoutParams?.width  = 64.toPx
        layoutParams?.height = 64.toPx

        displayingImage.layoutParams = layoutParams
    }

    fun setAnimatedImages(vararg images: String) {
        for(index in 0 until images.count()) {
            val imageName = images[index]

            val imageResouceId = resources.getIdentifier(imageName, "drawable", context.packageName)

            val animatedImage = AppCompatImageView(context)

            animatedImage.setImageResource(imageResouceId)
            animatedImage.setOnClickListener(this)
            animatedImage.tag = index
            animatedImage.visibility = View.INVISIBLE

            layout_image_container.addView(animatedImage)

            val layoutParams = displayingImage.layoutParams

            layoutParams?.width  = 52.toPx
            layoutParams?.height = 52.toPx

            animatedImage.layoutParams = layoutParams

            val imageContainer = ImageContainer(animatedImage, imageResouceId, imageName, Point(0, 200 + (200*index)))

            imageContainers.add(imageContainer)
        }
    }

    fun setContainerViewId(containerViewId: Int) {
        this.containerViewId = containerViewId
    }

    fun setAnimationExpanded(animationExpanded: Boolean) {
        this.animationExpanded = animationExpanded
    }

    fun getAnimationExpanded(): Boolean = animationExpanded

    fun setImageClickListener(imageTouchListener: ImageTouchListener) {
        this.imageTouchListener = imageTouchListener
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