package app.juky.squircleview.utils

import android.graphics.Canvas
import android.view.View
import app.juky.squircleview.data.SquircleCore

internal object SquircleCanvas {
    fun draw(view: View, canvas: Canvas, core: SquircleCore) {
        // Create background shape and squircle path
        core.shape.set(core.shadowElevation, core.shadowElevation, view.width.toFloat() - core.shadowElevation, view.height.toFloat() - core.shadowElevation)
        core.squirclePath.set(SquirclePath.getSquirclePath(core.shape, view.width, view.height))

        // Clip the background shape with the squircle path and draw it
        canvas.clipPath(core.squirclePath)

        // Draw the squircle
        canvas.drawPath(core.squirclePath, core.shapePaint)

        // Draw the background image
        SquircleImage.drawImage(view, core.backgroundImage, canvas, core.shapePaint)

        if (core.borderWidth > 0) {
            // Draw the squircle border
            canvas.drawPath(core.squirclePath, core.borderPaint)
        }
    }
}