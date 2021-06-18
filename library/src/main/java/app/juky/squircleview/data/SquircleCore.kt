package app.juky.squircleview.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.widget.TextViewCompat.setTextAppearance
import app.juky.squircleview.R
import app.juky.squircleview.utils.SquircleGradient.DEFAULT_COLOR_VALUE
import app.juky.squircleview.utils.SquircleShadowProvider.getShadowProvider
import app.juky.squircleview.views.SquircleButton
import app.juky.squircleview.views.SquircleConstraintLayout
import app.juky.squircleview.views.SquircleImageView

internal open class SquircleCore(context: Context, attrs: AttributeSet?, view: View) {
    val shape = RectF()
    val shapePaint = Paint().apply {
        this.style = Paint.Style.FILL
        this.isAntiAlias = true
    }

    val borderPaint = Paint().apply {
        this.style = Paint.Style.STROKE
        this.isAntiAlias = true
    }

    var squirclePath = Path()

    var backgroundImage: Bitmap?
    var backgroundColor: Int

    var shadowElevation: Float
    var shadowElevationColor: Int

    var gradientDrawable: GradientDrawable?
    var gradientStartColor: Int
    var gradientEndColor: Int
    var gradientDirection: GradientDirection

    var borderColor: Int
    var borderWidth: Float

    var rippleEnabled: Boolean

    init {
        context.obtainStyledAttributes(attrs, R.styleable.SquircleView).apply {
            backgroundImage = getDrawable(R.styleable.SquircleView_squircle_background_image)?.toBitmap()
            backgroundColor = getColor(
                R.styleable.SquircleView_squircle_background_color,
                ContextCompat.getColor(context, android.R.color.black)
            )
            shadowElevation = getDimension(R.styleable.SquircleView_squircle_shadow_elevation, view.elevation)
            shadowElevationColor = getColor(
                R.styleable.SquircleView_squircle_shadow_elevation_color,
                ContextCompat.getColor(context, R.color.squircle_default_shadow_color)
            )
            gradientDrawable = getDrawable(R.styleable.SquircleView_squircle_gradient_drawable) as? GradientDrawable
            gradientStartColor = getColor(R.styleable.SquircleView_squircle_gradient_start_color, DEFAULT_COLOR_VALUE)
            gradientEndColor = getColor(R.styleable.SquircleView_squircle_gradient_end_color, DEFAULT_COLOR_VALUE)
            gradientDirection = GradientDirection.values()[getInt(R.styleable.SquircleView_squircle_gradient_direction, GradientDirection.DEFAULT.ordinal)]
            borderColor = getColor(R.styleable.SquircleView_squircle_border_color, DEFAULT_COLOR_VALUE)
            borderWidth = getDimension(R.styleable.SquircleView_squircle_border_width, 0f)
            rippleEnabled = getBoolean(R.styleable.SquircleView_squircle_ripple_enabled, view !is SquircleImageView)

            recycle()
        }

        loadDefaultStyle(context, view, attrs)
        loadSquircleStyle(context, view)
    }

    /**
     * Load the Squircle style using the attributes
     */
    private fun loadSquircleStyle(context: Context, view: View) {
        shapePaint.color = backgroundColor
        borderPaint.apply {
            this.strokeWidth = borderWidth
            this.color = borderColor
        }

        if (view is SquircleButton || view is SquircleImageView || view is SquircleConstraintLayout) {
            if (shadowElevation > 0f) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    view.outlineAmbientShadowColor = shadowElevationColor
                    view.outlineSpotShadowColor = shadowElevationColor
                    view.outlineProvider = getShadowProvider()
                }
            }

            // The SquircleImageView has it's own setBackground which will cause an cyclic loop
            if (view !is AppCompatImageView) {
                // Prevent default button style
                view.background = null
            }

            if (view is SquircleConstraintLayout) {
                if (!rippleEnabled) {
                    // FIXME it seems like the ConstraintLayout itself has a bug where, if no background nor foreground is set,
                    //  the view will appear with a width of 0 and height of 0, and never call the onDraw method
                    view.foreground = ContextCompat.getDrawable(context, R.drawable.transparent_foreground)
                }
            }

            view.elevation = shadowElevation
        }
    }

    /**
     * Programmatic implementation of the SquircleButtonStyle. The user needs to manually add `style="@style/SquircleButtonStyle`, which isn't ideal.
     * The `AppCompatTextView` and `AppCompatImageView` do not support `defStyleRes`, so we have to resort to doing it manually.
     */
    private fun loadDefaultStyle(context: Context, view: View, attrs: AttributeSet?) {
        if (view is SquircleButton || view is SquircleImageView || view is SquircleConstraintLayout) {
            view.isClickable = true
            view.isFocusable = true

            // Set ripple if enabled
            if (rippleEnabled) {
                val ripple = TypedValue().also { context.theme.resolveAttribute(android.R.attr.selectableItemBackground, it, true) }.resourceId
                view.foreground = ContextCompat.getDrawable(context, ripple)
            }

            if (view is SquircleButton) {
                view.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
                val text = TypedValue().also { context.theme.resolveAttribute(android.R.attr.textAppearanceButton, it, true) }.resourceId
                setTextAppearance(view, text)

                // Restore original text color which is overridden by the text appearance
                context.obtainStyledAttributes(attrs, R.styleable.Default).apply {
                    view.setTextColor(
                        getColor(
                            R.styleable.Default_android_textColor,
                            ContextCompat.getColor(context, R.color.black)
                        )
                    )
                    recycle()
                }
            }
        }
    }
}