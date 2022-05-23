package app.juky.squircleview.utils

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import app.juky.squircleview.data.GradientDirection
import app.juky.squircleview.data.SquircleCore

internal object SquircleGradient {
    const val DEFAULT_COLOR_VALUE = -1

    // Draw in onSizeChanged to reduce recalculations on every draw
    fun onViewSizeChanged(newWidth: Int, newHeight: Int, view: View, core: SquircleCore) {
        if (newWidth > 0 && newHeight > 0) {
            core.shapePaint.shader = getGradient(
                view = view,
                direction = core.backgroundGradientDirection,
                startColor = core.backgroundGradientStartColor,
                endColor = core.backgroundGradientEndColor,
                drawable = core.backgroundGradientDrawable
            )
            core.borderPaint.shader = getGradient(
                view = view,
                direction = core.borderGradientDirection,
                startColor = core.borderGradientStartColor,
                endColor = core.borderGradientEndColor,
                drawable = core.borderGradientDrawable
            )

            view.invalidate()
        }
    }

    private fun getGradient(
        view: View,
        direction: GradientDirection,
        @ColorInt startColor: Int,
        @ColorInt endColor: Int,
        drawable: GradientDrawable?
    ): LinearGradient? {
        if (startColor == DEFAULT_COLOR_VALUE && endColor == DEFAULT_COLOR_VALUE && drawable == null) {
            // No colors nor drawable known, no gradient available
            return null
        }

        drawable?.let { gradientDrawable ->
            return getGradientByDrawable(view = view, direction = direction, gradientDrawable = gradientDrawable)
        } ?: run {
            // Using manual colors instead of gradient drawable
            return getGradientByColor(view = view, direction = direction, startColor = startColor, endColor = endColor)
        }
    }

    private fun getGradientByColor(view: View, direction: GradientDirection, @ColorInt startColor: Int, @ColorInt endColor: Int): LinearGradient {
        val gradientDirection = GradientDirection.getCoordinatesByDirection(view = view, direction = direction)

        return LinearGradient(
            gradientDirection.startX.toFloat(),
            gradientDirection.startY.toFloat(),
            gradientDirection.endX.toFloat(),
            gradientDirection.endY.toFloat(),
            startColor,
            endColor,
            Shader.TileMode.REPEAT
        )
    }

    private fun getGradientByDrawable(view: View, direction: GradientDirection, gradientDrawable: GradientDrawable): LinearGradient {
        fun transparentColor() = ContextCompat.getColor(view.context, android.R.color.transparent)

        val coordinates: GradientDirection.GradientCoordinates = if (direction == GradientDirection.DEFAULT) {
            GradientDirection.getCoordinates(view = view, orientation = gradientDrawable.orientation)
        } else {
            GradientDirection.getCoordinatesByDirection(view = view, direction = direction)
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