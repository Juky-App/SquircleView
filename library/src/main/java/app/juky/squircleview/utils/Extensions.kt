package app.juky.squircleview.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.core.content.ContextCompat
import app.juky.squircleview.R

fun Context.getDefaultRippleDrawable(): Drawable? {
    val ripple = TypedValue().also { this.theme.resolveAttribute(android.R.attr.selectableItemBackground, it, true) }.resourceId
    return ContextCompat.getDrawable(this, ripple)
}

fun Context.getTransparentRippleDrawable(): Drawable? = ContextCompat.getDrawable(this, R.drawable.transparent_foreground)