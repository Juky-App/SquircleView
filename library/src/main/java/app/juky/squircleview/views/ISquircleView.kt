package app.juky.squircleview.views

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes

internal interface ISquircleView {
    fun onDraw(canvas: Canvas?)

    fun onSizeChanged(newWidth: Int, newHeight: Int, oldWidth: Int, oldHeight: Int)

    fun setImage(drawable: Drawable?)

    fun setImage(@DrawableRes resId : Int)
}