package com.github.byungtak.library

import android.content.res.Resources


val Float.toPx: Float
    get() = (this * Resources.getSystem().displayMetrics.density)