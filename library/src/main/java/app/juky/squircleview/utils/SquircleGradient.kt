package app.juky.squircleview.utils

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import app.juky.squircleview.data.GradientDirection
import app.juky.squircleview.data.SquircleCore

internal object SquircleGradient {
    const val DEFAULT_COLOR_VALUE = -1

    fun onViewSizeChanged(newWidth: Int, newHeight: Int, view: View, core: SquircleCore) {
        if (newWidth > 0 && newHeight > 0) {
            drawGradient(view, core)
        }
    }

    private fun drawGradient(view: View, core: SquircleCore) {
        core.shapePaint.shader = getGradient(
            view = view,
            gradientStartColor = core.gradientStartColor,
            gradientEndColor = core.gradientEndColor,
            gradientDirection = core.gradientDirection,
            gradientDrawable = core.gradientDrawable
        )

        view.invalidate()
    }

    private fun getGradient(view: View, gradientStartColor: Int, gradientEndColor: Int, gradientDirection: GradientDirection, gradientDrawable: GradientDrawable?): LinearGradient? {
        if (gradientStartColor == DEFAULT_COLOR_VALUE && gradientEndColor == DEFAULT_COLOR_VALUE && gradientDrawable == null) {
            // No colors nor drawable known, no gradient available
            return null
        }

        if (gradientDrawable == null) {
            // Using manual colors instead of gradient drawable
            return getGradientByColor(view, gradientStartColor, gradientEndColor, gradientDirection)
        }

        return getGradientByDrawable(view, gradientDrawable)
    }

    private fun getGradientByColor(view: View, gradientStartColor: Int, gradientEndColor: Int, gradientDirection: GradientDirection): LinearGradient {
        val direction = GradientDirection.getCoordinatesByDirection(view, gradientDirection)

        return LinearGradient(
            direction.startX.toFloat(),
            direction.startY.toFloat(),
            direction.endX.toFloat(),
            direction.endY.toFloat(),
            gradientStartColor,
            gradientEndColor,
            Shader.TileMode.REPEAT
        )
    }

    private fun getGradientByDrawable(view: View, gradientDrawable: GradientDrawable): LinearGradient {
        fun transparentColor() = ContextCompat.getColor(view.context, android.R.color.transparent)
        val direction = GradientDirection.getCoordinates(view, gradientDrawable.orientation)

        // Gradient drawable does not contain colors in Android < N
        val startColor = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            gradientDrawable.colors?.getOrNull(0) ?: transparentColor()
        } else {
            transparentColor()
        })

        // Gradient drawable does not contain colors in Android < N
        val stopColor = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            gradientDrawable.colors?.getOrNull(1) ?: transparentColor()
        } else {
            transparentColor()
        })

        return LinearGradient(
            direction.startX.toFloat(),
            direction.startY.toFloat(),
            direction.endX.toFloat(),
            direction.endY.toFloat(),
            startColor,
            stopColor,
            Shader.TileMode.REPEAT
        )
    }
}