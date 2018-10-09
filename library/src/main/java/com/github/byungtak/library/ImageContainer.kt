package com.github.byungtak.library


data class ImageContainer(var viewId: Int, var drawableResourceId: Int) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other == null || javaClass != other.javaClass) {
            return false
        }

        val that = other as ImageContainer?

        return viewId == that?.viewId
    }

    override fun hashCode(): Int {
        return viewId.hashCode() % 3
    }
}