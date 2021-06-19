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

    // Draw in onSizeChanged to reduce recalculations on every draw
    fun onViewSizeChanged(newWidth: Int, newHeight: Int, view: View, core: SquircleCore) {
        if (newWidth > 0 && newHeight > 0) {
            core.shapePaint.shader = getGradient(view = view, core = core)
            view.invalidate()
        }
    }

    private fun getGradient(view: View, core: SquircleCore): LinearGradient? {
        if (core.gradientStartColor == DEFAULT_COLOR_VALUE && core.gradientEndColor == DEFAULT_COLOR_VALUE && core.gradientDrawable == null) {
            // No colors nor drawable known, no gradient available
            return null
        }

        core.gradientDrawable?.let { gradientDrawable ->
            return getGradientByDrawable(view = view, core = core, gradientDrawable = gradientDrawable)
        } ?: run {
            // Using manual colors instead of gradient drawable
            return getGradientByColor(view = view, core = core)
        }
    }

    private fun getGradientByColor(view: View, core: SquircleCore): LinearGradient {
        val direction = GradientDirection.getCoordinatesByDirection(view = view, direction = core.gradientDirection)

        return LinearGradient(
            direction.startX.toFloat(),
            direction.startY.toFloat(),
            direction.endX.toFloat(),
            direction.endY.toFloat(),
            core.gradientStartColor,
            core.gradientEndColor,
            Shader.TileMode.REPEAT
        )
    }

    private fun getGradientByDrawable(view: View, core: SquircleCore, gradientDrawable: GradientDrawable): LinearGradient {
        fun transparentColor() = ContextCompat.getColor(view.context, android.R.color.transparent)
        val coordinates: GradientDirection.GradientCoordinates = if (core.gradientDirection == GradientDirection.DEFAULT) {
            GradientDirection.getCoordinates(view = view, orientation = gradientDrawable.orientation)
        } else {
            GradientDirection.getCoordinatesByDirection(view = view, direction = core.gradientDirection)
        }

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
            coordinates.startX.toFloat(),
            coordinates.startY.toFloat(),
            coordinates.endX.toFloat(),
            coordinates.endY.toFloat(),
            startColor,
            stopColor,
            Shader.TileMode.REPEAT
        )
    }
}