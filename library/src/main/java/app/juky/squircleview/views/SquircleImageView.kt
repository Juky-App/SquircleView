package app.juky.squircleview.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import app.juky.squircleview.data.SquircleCore
import app.juky.squircleview.data.SquircleStyle
import app.juky.squircleview.utils.SquircleCanvas
import app.juky.squircleview.utils.SquircleGradient
import java.io.FileNotFoundException

class SquircleImageView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs), ISquircleView {
    private val core = SquircleCore(context, attrs, this)
    override val style = SquircleStyle(context = context, view = this, core = core)

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

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        style.setupRipple()
    }

    // Support image library view loading
    override fun setBackgroundResource(@DrawableRes resId: Int) {
        style.setBackgroundImage(resId)
    }

    // Support image library view loading
    override fun setBackgroundDrawable(background: Drawable?) {
        background ?: return
        style.setBackgroundImage(background)
    }

    // Support image library view loading
    override fun setImageResource(@DrawableRes resId: Int) {
        style.setBackgroundImage(resId)
    }

    // Support image library view loading
    override fun setImageURI(uri: Uri?) {
        uri ?: return

        try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return
            style.setBackgroundImage(Drawable.createFromStream(inputStream, uri.toString()))
        } catch (e: FileNotFoundException) {
            // Failed to set URI as drawable
        }
    }

    // Support image library view loading
    override fun setImageDrawable(drawable: Drawable?) {
        drawable ?: return
        style ?: return
        style.setBackgroundImage(drawable)
    }

    // Support image library view loading
    override fun setImageBitmap(bm: Bitmap?) {
        bm ?: return
        core.backgroundImage = bm
        invalidate()
    }
}