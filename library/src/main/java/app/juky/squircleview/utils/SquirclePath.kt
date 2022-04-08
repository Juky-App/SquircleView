package app.juky.squircleview.utils

import android.graphics.Path
import android.graphics.RectF
import androidx.annotation.IntRange
import app.juky.squircleview.data.Constants.DEFAULT_CORNER_SMOOTHING
import app.juky.squircleview.data.Constants.MAX_DEFAULT_CORNER_SMOOTHING
import kotlin.math.min

object SquirclePath {
    fun getRadiusByHeightOrWidth(
        height: Int,
        width: Int,
        @IntRange(from = 0, to = DEFAULT_CORNER_SMOOTHING)
        cornerSmoothing: Int = DEFAULT_CORNER_SMOOTHING.toInt()
    ): Float {
        // Percentage cannot exceed 100%, convert it to a decimal percentage (e.g. 0.6)
        val smoothingPercentage = min(cornerSmoothing, 100) / 100.0
        return (min(height, width) * (smoothingPercentage * MAX_DEFAULT_CORNER_SMOOTHING).toFloat())
    }

    /**
     * Get the Path used to represent a Squircle
     *
     * @param rect RectF Rectangle used to determine start / end bounds
     * @param width Int Final width used to determine the radius
     * @param height Int Final height used to determine the radius
     * @return Path Squircle Path
     */
    fun getSquirclePath(
        rect: RectF,
        width: Int,
        height: Int,
        @IntRange(from = 0, to = DEFAULT_CORNER_SMOOTHING)
        cornerSmoothing: Int = DEFAULT_CORNER_SMOOTHING.toInt()
    ): Path {
        val radius = getRadiusByHeightOrWidth(height, width, cornerSmoothing)

        val startX = rect.left
        val endX = rect.right
        val startY = rect.top
        val endY = rect.bottom

        val path = Path()

        // Start position
        path.moveTo(startX, startY + radius)

        // Top left corner
        path.cubicTo(
            startX,
            startY,
            startX,
            startY,
            startX + radius,
            startY
        )

        // Top line
        path.lineTo(endX - radius, startY)

        // Top right corner
        path.cubicTo(
            endX,
            startY,
            endX,
            startY,
            endX,
            startY + radius,
        )

        // Right line
        path.lineTo(endX, endY - radius)

        // Bottom right corner
        path.cubicTo(
            endX,
            endY,
            endX,
            endY,
            endX - radius,
            endY,
        )

        // Bottom line
        path.lineTo(startX + radius, endY)

        // Bottom left corner
        path.cubicTo(
            startX,
            endY,
            startX,
            endY,
            startX,
            endY - radius,
        )

        // Left line
        path.lineTo(startX, endY + radius)

        path.close()

        return path
    }
}