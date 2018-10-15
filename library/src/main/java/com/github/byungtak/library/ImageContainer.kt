package com.github.byungtak.library

import android.graphics.Point
import android.support.v7.widget.AppCompatImageView


data class ImageContainer(
        var image: AppCompatImageView,
        var imageResourceId: Int,
        var imageName: String,
        var imagePoint: Point

) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || javaClass != other.javaClass) {
            return false
        }

        val that = other as ImageContainer?

        return imageResourceId == that?.imageResourceId
    }

    override fun hashCode(): Int {
        return imageResourceId.hashCode() % 3
    }
}