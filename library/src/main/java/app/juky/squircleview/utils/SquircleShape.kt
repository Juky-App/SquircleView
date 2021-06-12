package app.juky.squircleview.utils

import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape
import android.view.View
import com.google.android.material.shape.CornerSize
import com.google.android.material.shape.CutCornerTreatment
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.ShapePath

/**
 * This class provides a ShapePath, ShapeAppearance and ShapeDrawable which can be used
 * instead of the custom view, allowing you to use the default shapes in Android.
 */
object SquircleShape {
    fun getSquirclePath(rect: RectF, width: Int, height: Int): ShapePath {
        val radius = SquirclePath.getRadiusByHeightOrWidth(height, width)

        val startX = rect.left
        val endX = rect.right
        val startY = rect.top
        val endY = rect.bottom

        val path = ShapePath()

        // Top left corner
        path.cubicToPoint(
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
        path.cubicToPoint(
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
        path.cubicToPoint(
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
        path.cubicToPoint(
            startX,
            endY,
            startX,
            endY,
            startX,
            endY - radius,
        )

        return path
    }

    fun getShapeAppearance(): ShapeAppearanceModel.Builder =
        ShapeAppearanceModel.builder().setAllCorners(SquircleCornerTreatment())

    fun getShapeDrawable(view: View): ShapeDrawable {
        val path = SquirclePath.getSquirclePath(
            rect = RectF(0f, 0f, view.width.toFloat(), view.height.toFloat()),
            width = view.width,
            height = view.height
        )

        return object : ShapeDrawable(PathShape(path, view.width.toFloat(), view.height.toFloat())) {
            override fun onBoundsChange(bounds: Rect) {
                super.onBoundsChange(bounds)
                shape = PathShape(
                    SquirclePath.getSquirclePath(
                        RectF(0f, 0f, bounds.width().toFloat(), bounds.height().toFloat()),
                        width = bounds.width(),
                        height = bounds.height()
                    ),
                    bounds.width().toFloat(),
                    bounds.height().toFloat()
                )
            }
        }
    }

    /**
     * CornerTreatment used to apply a treatment to the corners of buttons, images, constraintlayouts, etc.
     */
    class SquircleCornerTreatment : CutCornerTreatment() {
        override fun getCornerPath(shapePath: ShapePath, angle: Float, interpolation: Float, bounds: RectF, size: CornerSize) {
            val startX = 0f
            val startY = 0f
            val radius = SquirclePath.getRadiusByHeightOrWidth(bounds.height().toInt(), bounds.width().toInt())

            shapePath.lineTo(startX, startY + radius)
            shapePath.reset(0f, radius)

            shapePath.cubicToPoint(
                startX,
                startY,
                startX,
                startY,
                startX + radius,
                startY
            )
        }
    }
}