package app.juky.squircleview.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import app.juky.squircleview.data.SquircleCore
import app.juky.squircleview.utils.SquircleCanvas
import app.juky.squircleview.utils.SquircleGradient

class SquircleButton(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs), ISquircleView {
    private val core = SquircleCore(context, attrs, this)

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
}