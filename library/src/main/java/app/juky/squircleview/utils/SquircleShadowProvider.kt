package app.juky.squircleview.utils

import android.graphics.Outline
import android.graphics.RectF
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import androidx.core.graphics.toRect

internal object SquircleShadowProvider {
    fun getShadowProvider(): ViewOutlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            val shape = RectF(0f, 0f, view.width.toFloat(), view.height.toFloat())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                outline.setPath(SquirclePath.getSquirclePath(shape, view.width, view.height))
            } else {
                outline.setRoundRect(shape.toRect(), SquirclePath.getRadiusByHeightOrWidth(view.height, view.width))
            }
        }
    }
}