package app.juky.squircleview.views

import android.graphics.Canvas
import app.juky.squircleview.data.SquircleStyle

internal interface ISquircleView {
    val style: SquircleStyle

    fun onDraw(canvas: Canvas?)

    fun onSizeChanged(newWidth: Int, newHeight: Int, oldWidth: Int, oldHeight: Int)
}