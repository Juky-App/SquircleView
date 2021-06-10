package app.juky.squircleview.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.view.View
import kotlin.math.roundToInt

internal object SquircleImage {
    fun drawImage(view: View, backgroundImage: Bitmap?, canvas: Canvas, paint: Paint) {
        val image = backgroundImage ?: return

        // Preserve the save count, otherwise the image will be drawn over all content in the canvas
        val saveCount = canvas.save()

        val scale: Float
        var drawX = 0f
        var drawY = 0f

        if (image.width * view.height > view.width * image.height) {
            // Center image by X because the height matches the view
            scale = view.height.toFloat() / image.height.toFloat()
            drawX = (view.width - (image.width * scale)) * 0.5f
        } else {
            // Center image by Y because the width matches the view
            scale = view.width.toFloat() / image.width.toFloat()
            drawY = (view.height - (image.height * scale)) * 0.5f
        }

        // Scale the image and center it
        val matrix = Matrix().apply {
            setScale(scale, scale)
            postTranslate(drawX.roundToInt().toFloat(), drawY.roundToInt().toFloat())
        }

        with(canvas) {
            concat(matrix)

            // Draw the image
            drawBitmap(image, 0f, 0f, paint)

            // Restore save count so all previous canvas content will be shown
            restoreToCount(saveCount)
        }
    }
}