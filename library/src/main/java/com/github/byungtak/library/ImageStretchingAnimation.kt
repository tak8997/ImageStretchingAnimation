package com.github.byungtak.library

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.image_stretching_animation_layout.view.*
import java.util.*


typealias ImageIndex  = Int
typealias ImageViewID = Int

class ImageStretchingAnimation @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null

): FrameLayout(context, attributeSet), View.OnClickListener {

    companion object {
        private const val SCALE_DOWN_DURATION = 250L

        private const val FIRST_IMAGE         = 0
        private const val SECOND_IMAGE        = 1
        private const val THIRD_IMAGE         = 2
        private const val LAST_IMAGE          = 3
        private const val DISPLAYING_IMAGE    = 4
    }

    interface ImageTouchListener {
        fun onImageDelivered(selectedImage: ImageContainer)
    }

//    private val view = LayoutInflater.from(context).inflate(R.layout.image_stretching_animation_layout, this)

    private var imageTouchListener: ImageTouchListener? = null

//    private var displayingImage: ImageContainer? = null

    private var displayingViewId: Int = 99
    private var containerViewId: Int? = null

    private var animatorSet: AnimatorSet = AnimatorSet()

    private var animationExpanded = false

//    private val animatedImages = ArrayList<ImageContainer>()

    private val imageIdMap = HashMap<ImageIndex, ImageViewID>()

    private val moveYWithFirstImage by lazy(LazyThreadSafetyMode.NONE) {
        ObjectAnimator.ofFloat(animatedImages[FIRST_IMAGE], "y", 0F, 81F.toPx)
    }

    private val moveYWithSecondImage by lazy(LazyThreadSafetyMode.NONE) {
        ObjectAnimator.ofFloat(animatedImages[SECOND_IMAGE], "y", 0F, 146F.toPx)
    }

    private val moveYWithThirdImage by lazy(LazyThreadSafetyMode.NONE) {
        ObjectAnimator.ofFloat(animatedImages[THIRD_IMAGE], "y", 0F, 208F.toPx)
    }

    private val moveYWithLastImage  by lazy(LazyThreadSafetyMode.NONE) {
        ObjectAnimator.ofFloat(animatedImages[LAST_IMAGE], "y", 0F, 270F.toPx)
    }

    private val imagesShowListener = object : SimpleAnimatorListener() {
        override fun onAnimationStart(animation: Animator?) {
            animatedImages.forEach {
                it.visibility = View.VISIBLE
            }

            setLayoutHeight(500.toPx)
        }
    }

    private val imagesHideListener = object : SimpleAnimatorListener() {
        override fun onAnimationEnd(animation: Animator?) {
            animatedImages.forEach {
                it.visibility = View.INVISIBLE
            }

            setLayoutHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.image_stretching_animation_layout, this)

        initializeImageIdHashMap()
//        initializeImages()
//        initializeImageClickListener()
    }

    override fun onClick(view: View?) {
        if(animationExpanded) {
            if(view?.id == displayingViewId) {
                stopAnimation()
                setAnimationExpanded(false)

                return
            } else {
                var selectedId = 0

                for(i in 0 until animatedImages.count()) {
                    if(view?.getTag(i) == animatedImages[i].getTag(i)) {
                        selectedId = i
                    }
                }

//                val selectedId = when(view?.tag) {
//                    R.id.image_first  -> 0
//                    R.id.image_second -> 1
//                    R.id.image_third  -> 2
//                    R.id.image_last   -> 3
//                    else              -> {
//                        stopAnimation()
//                        setAnimationExpanded(false)
//
//                        return
//                    }
//                }

                val selectedImage = animatedImages[selectedId]

                displayingImage.setImageResource(selectedImage.resources.)

                animatedImages.removeAt(selectedId)

                rearrangeImages(displayingImage = displayingImage)

                displayingImage = selectedImage

                stopAnimation()
                setAnimationExpanded(false)
            }
        }
        else {
            playAnimation()
            setAnimationExpanded(true)
        }
    }

    fun stopAnimation() {
        animatorSet.removeAllListeners()
        animatorSet = AnimatorSet()

        with(animatorSet) {
            interpolator = ReverseInterpolator()

            addListener(imagesHideListener)

            setDuration(SCALE_DOWN_DURATION)
                    .play(moveYWithFirstImage)
                    .with(moveYWithSecondImage)
                    .with(moveYWithThirdImage)
                    .with(moveYWithLastImage)

            start()
        }
    }

    fun playAnimation() {
        animatorSet.removeAllListeners()
        animatorSet = AnimatorSet()

        with(animatorSet) {
            interpolator = LinearInterpolator()

            addListener(imagesShowListener)

            setDuration(SCALE_DOWN_DURATION)
                    .play(moveYWithFirstImage)
                    .with(moveYWithSecondImage)
                    .with(moveYWithThirdImage)
                    .with(moveYWithLastImage)

            start()
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

    private fun rearrangeImages(displayingImage: AppCompatImageView) {
//        val tempImages = ArrayList<ImageContainer>()
//
//        tempImages.addAll(animatedImages)
//
//        animatedImages.add(displayingImage)
//
//        for(i in 0 until animatedImages.count() - 1) {
//            animatedImages[i].image      = imageIdMap[i]?.let { view.findViewById<AppCompatImageView>(it) } ?: AppCompatImageView(context)
//
//            animatedImages[i].avatar     = tempImages[i].avatar
//            animatedImages[i].resourceId = tempImages[i].resourceId
//            animatedImages[i].image.setImageResource(tempImages[i].resourceId)
//        }
//
////        animatedImages[LAST_IMAGE].image      = view.findViewById(R.id.image_last)
//
//        animatedImages[LAST_IMAGE].avatar     = displayingImage.avatar
//        animatedImages[LAST_IMAGE].resourceId = displayingImage.resourceId
//        animatedImages[LAST_IMAGE].image.setImageResource(displayingImage.resourceId)
    }

//    private fun initializeImages() {
//        animatedImages.add(ImageContainer(Avatar.Lucy, image_first, R.drawable.avatar_lucy))
//        animatedImages.add(ImageContainer(Avatar.Valerie, image_second, R.drawable.avatar_valarie))
//        animatedImages.add(ImageContainer(Avatar.Henry, image_third, R.drawable.avatar_henry))
//        animatedImages.add(ImageContainer(Avatar.Albert, image_last, R.drawable.avatar_albert))
//
//        for(i in 0 until animatedImages.count()) {
//            animatedImages[i].image = imageIdMap[i]?.let { view.findViewById<AppCompatImageView>(it) } ?: AppCompatImageView(context)
//            animatedImages[i].image.setImageResource(animatedImages[i].resourceId)
//        }
//    }

    private fun initializeImageIdHashMap() {
//        imageIdMap[FIRST_IMAGE]      = R.id.image_first
//        imageIdMap[SECOND_IMAGE]     = R.id.image_second
//        imageIdMap[THIRD_IMAGE]      = R.id.image_third
//        imageIdMap[LAST_IMAGE]       = R.id.image_last
//        imageIdMap[DISPLAYING_IMAGE] = R.id.image_displaying
    }

//    private fun initializeImageClickListener() {
//        image_first.setOnClickListener(this)
//        image_second.setOnClickListener(this)
//        image_third.setOnClickListener(this)
//        image_last.setOnClickListener(this)
//        image_displaying.setOnClickListener(this)
//    }

    private var animatedImages = mutableListOf<AppCompatImageView>()

    fun setAnimatedImages(vararg drawables: Int) {
        for(i in 0 until drawables.count()) {
            val animatedImage = AppCompatImageView(context)

            animatedImage.setImageResource(drawables[i])
            animatedImage.setOnClickListener(this)
            animatedImage.tag = ImageContainer(i, drawables[i])

            layout_image_container.addView(animatedImage)

            val layoutParams = displayingImage.layoutParams

            layoutParams?.width  = 52.toPx
            layoutParams?.height = 52.toPx

            animatedImage.layoutParams = layoutParams

            animatedImages.add(animatedImage)
//            animatedImages.add(ImageContainer(Avatar.Lucy, , drawables[i]))
        }
    }

    private var displayingImage = AppCompatImageView(context)

    fun setDisplayingImage(drawable: Int) {
        displayingImage.setImageResource(drawable)
        displayingImage.id = displayingViewId

        layout_image_container.addView(displayingImage)

        val layoutParams = displayingImage.layoutParams

        layoutParams?.width  = 64.toPx
        layoutParams?.height = 64.toPx

        displayingImage.layoutParams = layoutParams
    }
}