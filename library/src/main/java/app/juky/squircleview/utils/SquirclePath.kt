package app.juky.squircleview.utils

import android.graphics.Path
import android.graphics.RectF
import kotlin.math.min

object SquirclePath {
    internal fun getRadiusByHeightOrWidth(height: Int, width: Int) = (min(height, width) * 0.67).toFloat()

    /**
     * Get the Path used to represent a Squircle
     *
     * @param rect RectF Rectangle used to determine start / end bounds
     * @param width Int Final width used to determine the radius
     * @param height Int Final height used to determine the radius
     * @return Path Squircle Path
     */
    fun getSquirclePath(rect: RectF, width: Int, height: Int): Path {
        val radius = getRadiusByHeightOrWidth(height, width)

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

        path.close()

        return path
    }
}