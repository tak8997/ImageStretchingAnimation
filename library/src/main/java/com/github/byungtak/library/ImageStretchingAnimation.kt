package com.github.byungtak.library

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.image_stretching_animation_layout.view.*

typealias ImageIndex  = Int
typealias ImageViewID = Int

internal class ImageStretchingAnimation @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null

): FrameLayout(context, attributeSet), View.OnClickListener {


    companion object {
        private const val SCALE_DOWN_DURATION = 250L

        private const val ZERO_IMAGE          = 0
        private const val FIRST_IMAGE         = 1
        private const val SECOND_IMAGE        = 2
        private const val LAST_IMAGE          = 3
    }

    interface ImageTouchListener {
        fun onImageDelivered(selectedImage: ImageContainer)
    }

    private val view = LayoutInflater.from(context).inflate(R.layout.image_stretching_animation_layout, this)

    private var imageTouchListener: ImageTouchListener? = null

    private var displayingImage: ImageContainer? = null

    private var animatorSet: AnimatorSet = AnimatorSet()

    private var animationExpanded = false

    private val animatedImages = ArrayList<ImageContainer>()

    private val imageIdMap = HashMap<ImageIndex, ImageViewID>()

    private val moveYWithZeroImage by lazy(LazyThreadSafetyMode.NONE) {
        ObjectAnimator.ofFloat(animatedImages[ZERO_IMAGE].image, "y", 0F, 81F.toPx)
    }

    private val moveYWithFirstImage by lazy(LazyThreadSafetyMode.NONE) {
        ObjectAnimator.ofFloat(animatedImages[FIRST_IMAGE].image, "y", 0F, 146F.toPx)
    }

    private val moveYWithSecondImage by lazy(LazyThreadSafetyMode.NONE) {
        ObjectAnimator.ofFloat(animatedImages[SECOND_IMAGE].image, "y", 0F, 208F.toPx)
    }

    private val moveYWithThirdImage  by lazy(LazyThreadSafetyMode.NONE) {
        ObjectAnimator.ofFloat(animatedImages[LAST_IMAGE].image, "y", 0F, 270F.toPx)
    }

    private val imageContainerShowListener = object : SimpleAnimatorListener() {
        override fun onAnimationStart(animation: Animator?) {
            layout_image_container.visibility = View.VISIBLE
        }
    }

    private val imageContainerHideListener = object : SimpleAnimatorListener() {
        override fun onAnimationEnd(animation: Animator?) {
            layout_image_container.visibility = View.INVISIBLE
        }
    }

    init {
        initializeImageIdHashMap()
        initializeImageClickListener()

        animatedImages.add(ImageContainer(Avatar.Dave, image_zero, R.drawable.avatar_dave))
        animatedImages.add(ImageContainer(Avatar.Lucy, image_first, R.drawable.avatar_lucy))
        animatedImages.add(ImageContainer(Avatar.Valerie, image_second, R.drawable.avatar_valarie))
        animatedImages.add(ImageContainer(Avatar.Henry, image_third, R.drawable.avatar_henry))
        animatedImages.add(ImageContainer(Avatar.Albert, image_fourth, R.drawable.avatar_albert))
    }

    override fun onClick(view: View?) {
        if(animationExpanded) {
            val selectedImage: ImageContainer

            var selectedId = -1

            when(view?.id) {
                R.id.image_zero   -> selectedId = 0
                R.id.image_first  -> selectedId = 1
                R.id.image_second -> selectedId = 2
                R.id.image_third  -> selectedId = 3
            }

            selectedImage = animatedImages[selectedId]

            animatedImages.removeAt(selectedId)

            stopAnimation()
            setAnimationExpanded(false)

            imageTouchListener?.onImageDelivered(selectedImage)

            displayingImage?.let {
                rearrangeImages(it)
            }

            displayingImage = selectedImage
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

            addListener(imageContainerHideListener)

            setDuration(SCALE_DOWN_DURATION)
                    .play(moveYWithZeroImage)
                    .with(moveYWithFirstImage)
                    .with(moveYWithSecondImage)
                    .with(moveYWithThirdImage)

            start()
        }
    }

    fun playAnimation() {
        animatorSet.removeAllListeners()
        animatorSet = AnimatorSet()

        with(animatorSet) {
            interpolator = LinearInterpolator()

            addListener(imageContainerShowListener)

            setDuration(SCALE_DOWN_DURATION)
                    .play(moveYWithZeroImage)
                    .with(moveYWithFirstImage)
                    .with(moveYWithSecondImage)
                    .with(moveYWithThirdImage)

            start()
        }
    }

    fun initializeImages(displayingImage: ImageContainer) {
        this.displayingImage = displayingImage

        animatedImages.clear()

        animatedImages.add(ImageContainer(Avatar.Dave, image_zero, R.drawable.avatar_dave))
        animatedImages.add(ImageContainer(Avatar.Lucy, image_first, R.drawable.avatar_lucy))
        animatedImages.add(ImageContainer(Avatar.Valerie, image_second, R.drawable.avatar_valarie))
        animatedImages.add(ImageContainer(Avatar.Henry, image_third, R.drawable.avatar_henry))
        animatedImages.add(ImageContainer(Avatar.Albert, image_fourth, R.drawable.avatar_albert))

        animatedImages.remove(displayingImage)

        for(i in 0 until animatedImages.count()) {
            animatedImages[i].image = imageIdMap[i]?.let { view.findViewById<AppCompatImageView>(it) } ?: AppCompatImageView(context)
            animatedImages[i].image.setImageResource(animatedImages[i].resourceId)
        }
    }

    fun setAnimationExpanded(animationExpanded: Boolean) {
        this.animationExpanded = animationExpanded
    }

    fun getAnimationExpanded(): Boolean = animationExpanded

    fun setImageClickListener(imageTouchListener: ImageTouchListener) {
        this.imageTouchListener = imageTouchListener
    }

    private fun rearrangeImages(displayingImage: ImageContainer) {
        val tempImages = ArrayList<ImageContainer>()

        tempImages.addAll(animatedImages)

        animatedImages.add(displayingImage)

        for(i in 0 until animatedImages.count() - 1) {
            animatedImages[i].image      = imageIdMap[i]?.let { view.findViewById<AppCompatImageView>(it) } ?: AppCompatImageView(context)
            animatedImages[i].avatar     = tempImages[i].avatar
            animatedImages[i].resourceId = tempImages[i].resourceId

            animatedImages[i].image.setImageResource(tempImages[i].resourceId)
        }

        animatedImages[LAST_IMAGE].image      = view.findViewById(R.id.image_third)
        animatedImages[LAST_IMAGE].avatar     = displayingImage.avatar
        animatedImages[LAST_IMAGE].resourceId = displayingImage.resourceId

        animatedImages[LAST_IMAGE].image.setImageResource(displayingImage.resourceId)
    }

    private fun initializeImageIdHashMap() {
        imageIdMap[ZERO_IMAGE]   = R.id.image_zero
        imageIdMap[FIRST_IMAGE]  = R.id.image_first
        imageIdMap[SECOND_IMAGE] = R.id.image_second
        imageIdMap[LAST_IMAGE]   = R.id.image_third
    }

    private fun initializeImageClickListener() {
        image_zero.setOnClickListener(this)
        image_first.setOnClickListener(this)
        image_second.setOnClickListener(this)
        image_third.setOnClickListener(this)
    }
}