package app.juky.squircleview.utils

import android.graphics.Outline
import android.graphics.RectF
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.IntRange
import androidx.core.graphics.toRect
import app.juky.squircleview.data.Constants

internal object SquircleShadowProvider {
    fun getShadowProvider(
        @IntRange(from = 0, to = Constants.DEFAULT_CORNER_SMOOTHING)
        cornerSmoothing: Int = Constants.DEFAULT_CORNER_SMOOTHING.toInt()
    ): ViewOutlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            val shape = RectF(0f, 0f, view.width.toFloat(), view.height.toFloat())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                outline.setPath(SquirclePath.getSquirclePath(shape, view.width, view.height, cornerSmoothing))
            } else {
                outline.setRoundRect(shape.toRect(), SquirclePath.getRadiusByHeightOrWidth(view.height, view.width, cornerSmoothing))
            }
        }
    }
}