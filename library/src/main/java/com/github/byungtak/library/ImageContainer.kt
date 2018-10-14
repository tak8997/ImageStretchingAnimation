package com.github.byungtak.library

import android.graphics.Point
import android.support.v7.widget.AppCompatImageView


data class ImageContainer(
        val image: AppCompatImageView,
        val imageResourceId: Int,
        val imageName: String,
        val imagePoint: Point

) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || javaClass != other.javaClass) {
            return false
        }

        val that = other as ImageContainer?

        return imageName == that?.imageName
    }

    override fun hashCode(): Int {
        return imageResourceId.hashCode() % 3
    }
}