package com.github.byungtak.library

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

//    private val imageContainers = mutableListOf<ImageContainer>()
    private var images: MutableList<AppCompatImageView> = mutableListOf()
    private var imageNames: MutableList<String> = mutableListOf()

    private val imagePoints = mutableListOf<Point>()

    private var containerViewId: Int? = null

    private var displayingImage: AppCompatImageView? = null
    private var displayingImageName = ""

    private var animationExpanded = false

    private var lastImageIndex = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.image_stretching_animation_layout, this)
    }

    override fun onClick(view: View?) {
        if(animationExpanded) {
            if(view?.id == R.id.image_displaying) {
                setAnimationExpanded(false)
                setLayoutHeight(ViewGroup.LayoutParams.WRAP_CONTENT)

                for(i in 0 until images.count()) {
                    images[i].visibility = View.GONE

                    stopAnimation(i)
                }

                return
            } else {
                var selectedId = 0

                for(i in 0 until images.count()) {
                    val imageId   = images[i].tag
                    val clickedId = view?.tag

                    if(imageId == clickedId) {
                        selectedId = i

                        break
                    }
                }

                //보여지는 걸 복사 맨 마지막으로 가게

                val lastImageName = displayingImageName

//                val lastImage = AppCompatImageView(context)
//                lastImage.setImageResource(lastImageName.getImageResource(context))
//                lastImage.tag = lastImageIndex

                setDisplayingImage(imageNames[selectedId])

                for(i in 0 until images.count()) {
                    images[i].setImageResource(0)
                }

                imageNames.removeAt(selectedId)

                val tempImageNames = mutableListOf<String>()

                tempImageNames.addAll(imageNames)

//                images = mutableListOf()
//                imageNames = mutableListOf()

                setAnimatedImages(tempImageNames)

                setAnimationExpanded(false)
                setLayoutHeight(ViewGroup.LayoutParams.WRAP_CONTENT)

                for(i in 0 until images.count()) {
//                    images[i].visibility = View.GONE

                    stopAnimation(i)
                }

                val lastImage = AppCompatImageView(context)

                lastImage.setImageResource(lastImageName.getImageResource(context))
                lastImage.tag = lastImageIndex
                lastImage.bringToFront()

                images.add(lastImage)
                imageNames.add(lastImageName)
            }
        }
        else {
            setLayoutHeight(500.toPx)
            setAnimationExpanded(true)

            for(i in 0 until images.count()) {
                images[i].visibility = View.VISIBLE

                playAnimation(i)
            }
        }
    }

    private fun rearrangeImages(lastImageName: String) {
        val tempImages = mutableListOf<AppCompatImageView>()

        for(i in 0 until images.count()) {
            val image = AppCompatImageView(context)


        }

    }

//    private fun rearrangeImages(displayedImage: ImageContainer) {
//        val tempImages = mutableListOf<ImageContainer>()
//
//        tempImages.addAll(imageContainers)
//
//        imageContainers.add(displayedImage)
//
//        for(i in 0 until imageContainers.count() - 1) {
//            imageContainers[i].image = AppCompatImageView(context)
//            imageContainers[i].imageResourceId = tempImages[i].imageResourceId
//
//            imageContainers[i].image.setImageResource(tempImages[i].imageResourceId)
//        }
//
//        imageContainers[lastImageIndex].image      = AppCompatImageView(context)
//        imageContainers[lastImageIndex].imageResourceId = displayedImage.imageResourceId
//
//        imageContainers[lastImageIndex].image.setImageResource(displayedImage.imageResourceId)
//    }

    fun stopAnimation(index: Int) {
        val imageAnimatorY = ValueAnimator.ofFloat(imagePoints[index].y.toFloat(), 0F).apply {
            addUpdateListener { animation ->
                images[index].y = animation.animatedValue as Float
                images[index].requestLayout()
            }

            duration = SCALE_DOWN_DURATION
        }

        AnimatorSet().apply {
            play(imageAnimatorY)
            start()
        }
    }

    fun playAnimation(index: Int) {
        val imageAnimatorY = ValueAnimator.ofFloat(0F, imagePoints[index].y.toFloat()).apply {
            addUpdateListener { animation ->
                images[index].y = animation.animatedValue as Float
                images[index].requestLayout()
            }

            duration = SCALE_DOWN_DURATION
        }

        AnimatorSet().apply {
            play(imageAnimatorY)
            start()
        }
    }

    fun setDisplayingImage(imageName: String) {
        val imageResouceId = imageName.getImageResource(context)

        image_displaying.setImageResource(imageResouceId)
        image_displaying.setOnClickListener(this)

        displayingImageName = imageName
    }

    fun setAnimatedImages(vararg drwableImages: String) {
        images = mutableListOf()
        imageNames = mutableListOf()

        for(index in 0 until drwableImages.count()) {
            val imageName = drwableImages[index]

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

            images.add(animatedImage)
            imageNames.add(imageName)

            imagePoints.add(Point(0, 200 + (200*index)))
        }

        lastImageIndex = images.count() - 1
    }

    fun setAnimatedImages(drwableImages: MutableList<String>) {
        images = mutableListOf()
        imageNames = mutableListOf()

        for(index in 0 until drwableImages.count()) {
            val imageName = drwableImages[index]

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

            images.add(animatedImage)
            imageNames.add(imageName)
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