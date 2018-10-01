package com.github.byungtak.library

import android.support.v7.widget.AppCompatImageView


internal data class ImageContainer(var avatar: Avatar,
                                   var image: AppCompatImageView,
                                   var resourceId: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || javaClass != other.javaClass) {
            return false
        }

        val that = other as ImageContainer?

        return avatar == that?.avatar
    }

    override fun hashCode(): Int {
        return avatar.hashCode()
    }
}