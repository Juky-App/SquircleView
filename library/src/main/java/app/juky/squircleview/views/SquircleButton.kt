package app.juky.squircleview.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import app.juky.squircleview.data.SquircleCore
import app.juky.squircleview.data.SquircleStyle
import app.juky.squircleview.utils.SquircleCanvas
import app.juky.squircleview.utils.SquircleGradient

class SquircleButton(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs), ISquircleView {
    private val core = SquircleCore(context = context, attrs = attrs, view = this)
    override val style = SquircleStyle(context = context, view = this, core = core)

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
}