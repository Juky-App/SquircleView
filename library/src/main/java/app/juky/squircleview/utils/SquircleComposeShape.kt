package app.juky.squircleview.utils


import android.graphics.RectF
import androidx.annotation.IntRange
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import app.juky.squircleview.data.Constants.DEFAULT_CORNER_SMOOTHING
import app.juky.squircleview.utils.SquirclePath.getRadiusByHeightOrWidth

object SquircleComposeShape {
    fun getSquirclePath(
        size: Size,
        @IntRange(from = 0, to = DEFAULT_CORNER_SMOOTHING)
        cornerSmoothing: Int = DEFAULT_CORNER_SMOOTHING.toInt()
    ): Path {
        return getSquirclePath(
            rect = RectF(0f, 0f, size.width, size.height),
            width = size.width.toInt(),
            height = size.height.toInt(),
            cornerSmoothing = cornerSmoothing
        )
    }

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

        path.moveTo(x = startX, y = startY + radius)

        // Top left corner
        path.cubicTo(
            x1 = startX,
            y1 = startY,
            x2 = startX,
            y2 = startY,
            x3 = startX + radius,
            y3 = startY
        )

        // Top line
        path.lineTo(x = endX - radius, y = startY)

        // Top right corner
        path.cubicTo(
            x1 = endX,
            y1 = startY,
            x2 = endX,
            y2 = startY,
            x3 = endX,
            y3 = startY + radius,
        )

        // Right line
        path.lineTo(x = endX, y = endY - radius)

        // Bottom right corner
        path.cubicTo(
            x1 = endX,
            y1 = endY,
            x2 = endX,
            y2 = endY,
            x3 = endX - radius,
            y3 = endY,
        )

        // Bottom line
        path.lineTo(x = startX + radius, y = endY)

        // Bottom left corner
        path.cubicTo(
            x1 = startX,
            y1 = endY,
            x2 = startX,
            y2 = endY,
            x3 = startX,
            y3 = endY - radius,
        )

        path.lineTo(x = startX, y = (height / 2).toFloat())

        return path
    }

    class SquircleComposeShape(
        @IntRange(from = 0, to = DEFAULT_CORNER_SMOOTHING)
        val cornerSmoothing: Int = DEFAULT_CORNER_SMOOTHING.toInt()
    ) : Shape {
        override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
            return Outline.Generic(
                getSquirclePath(
                    rect = RectF(0f, 0f, size.width, size.height),
                    width = size.width.toInt(),
                    height = size.height.toInt(),
                    cornerSmoothing = cornerSmoothing
                )
            )
        }
    }
}