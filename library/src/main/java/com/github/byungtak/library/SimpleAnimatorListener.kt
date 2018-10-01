package com.github.byungtak.library

import android.animation.Animator


open class SimpleAnimatorListener : Animator.AnimatorListener {

    override fun onAnimationStart(animation: Animator?) = Unit

    override fun onAnimationRepeat(animation: Animator?) = Unit

    override fun onAnimationEnd(animation: Animator?) = Unit

    override fun onAnimationCancel(animation: Animator?) = Unit

}