package app.juky.squircleview.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import app.juky.squircleview.data.SquircleCore
import app.juky.squircleview.utils.SquircleCanvas
import app.juky.squircleview.utils.SquircleGradient
import java.io.FileNotFoundException

class SquircleImageView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs), ISquircleView {
    private val core = SquircleCore(context, attrs, this)

    init {
        // Default button style background should be hidden. This cannot be done in the
        // SquircleCore due to cyclic issues
        background = null
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        SquircleCanvas.draw(this, canvas, core)
        super.onDraw(canvas)
    }

    override fun onSizeChanged(newWidth: Int, newHeight: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight)
        SquircleGradient.onViewSizeChanged(newWidth, newHeight, this, core)
    }

    override fun setImage(drawable: Drawable?) {
        core.backgroundImage = drawable?.toBitmap()
        invalidate()
    }

    override fun setImage(@DrawableRes resId: Int) {
        setImage(ContextCompat.getDrawable(context, resId))
    }

    // Support image library view loading
    override fun setBackgroundResource(@DrawableRes resId: Int) {
        setImage(resId)
    }

    // Support image library view loading
    override fun setBackgroundDrawable(background: Drawable?) {
        setImage(background)
    }

    // Support image library view loading
    override fun setImageResource(@DrawableRes resId: Int) {
        setImage(resId)
    }

    // Support image library view loading
    override fun setImageURI(uri: Uri?) {
        try {
            uri ?: return
            val inputStream = context.contentResolver.openInputStream(uri) ?: return
            setImage(Drawable.createFromStream(inputStream, uri.toString()))
        } catch (e: FileNotFoundException) {
            // Failed to set URI as drawable
        }
    }

    // Support image library view loading
    override fun setImageDrawable(drawable: Drawable?) {
        setImage(drawable)
    }

    // Support image library view loading
    override fun setImageBitmap(bm: Bitmap?) {
        core.backgroundImage = bm
        invalidate()
    }
}