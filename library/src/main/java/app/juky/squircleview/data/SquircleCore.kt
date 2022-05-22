package app.juky.squircleview.data

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import app.juky.squircleview.R
import app.juky.squircleview.data.Constants.DEFAULT_CORNER_SMOOTHING
import app.juky.squircleview.utils.SquircleGradient.DEFAULT_COLOR_VALUE
import app.juky.squircleview.utils.SquircleShadowProvider.getShadowProvider
import app.juky.squircleview.utils.TextAllCapsTransformationMethod
import app.juky.squircleview.utils.getDefaultRippleDrawable
import app.juky.squircleview.utils.getTransparentRippleDrawable
import app.juky.squircleview.views.SquircleButton
import app.juky.squircleview.views.SquircleConstraintLayout
import app.juky.squircleview.views.SquircleImageView

class SquircleCore(context: Context, attrs: AttributeSet?, view: View) {
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

    var shadowElevation: Float
    @ColorInt var shadowElevationColor: Int

    var backgroundImage: Bitmap?
    @ColorInt var backgroundColor: Int

    var backgroundGradientDrawable: GradientDrawable?
    @ColorInt var backgroundGradientStartColor: Int
    @ColorInt var backgroundGradientEndColor: Int
    var backgroundGradientDirection: GradientDirection

    @ColorInt var borderColor: Int
    var borderWidth: Float

    var rippleEnabled: Boolean
    var rippleDrawable: Drawable?

    @IntRange(from = 0, to = DEFAULT_CORNER_SMOOTHING)
    var cornerSmoothing: Int

    init {
        context.obtainStyledAttributes(attrs, R.styleable.SquircleView).apply {
            shadowElevation = getDimension(R.styleable.SquircleView_squircle_shadow_elevation, view.elevation)
            shadowElevationColor = getColor(
                R.styleable.SquircleView_squircle_shadow_elevation_color,
                ContextCompat.getColor(context, R.color.squircle_default_shadow_color)
            )

            backgroundImage = getDrawable(R.styleable.SquircleView_squircle_background_image)?.toBitmap()
            backgroundColor = getColor(
                R.styleable.SquircleView_squircle_background_color,
                ContextCompat.getColor(context, android.R.color.black)
            )
            backgroundGradientDrawable = getDrawable(R.styleable.SquircleView_squircle_background_gradient_drawable) as? GradientDrawable
            backgroundGradientStartColor = getColor(R.styleable.SquircleView_squircle_background_gradient_start_color, DEFAULT_COLOR_VALUE)
            backgroundGradientEndColor = getColor(R.styleable.SquircleView_squircle_background_gradient_end_color, DEFAULT_COLOR_VALUE)
            backgroundGradientDirection = GradientDirection.values()[getInt(R.styleable.SquircleView_squircle_background_gradient_direction, GradientDirection.DEFAULT.ordinal)]

            borderColor = getColor(R.styleable.SquircleView_squircle_border_color, DEFAULT_COLOR_VALUE)
            borderWidth = getDimension(R.styleable.SquircleView_squircle_border_width, 0f)
            rippleEnabled = getBoolean(R.styleable.SquircleView_squircle_ripple_enabled, view !is SquircleImageView)
            rippleDrawable = getDrawable(R.styleable.SquircleView_squircle_ripple_drawable)
            cornerSmoothing = getInteger(R.styleable.SquircleView_squircle_corner_smoothing_percentage, DEFAULT_CORNER_SMOOTHING.toInt())

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
                if (rippleEnabled) {
                    if (rippleDrawable != null && view.hasOnClickListeners()) {
                        view.foreground = rippleDrawable
                    }
                } else {
                    // FIXME it seems like the ConstraintLayout itself has a bug where, if no background nor foreground is set,
                    //  the view will appear with a width of 0 and height of 0, and never call the onDraw method
                    view.foreground = context.getTransparentRippleDrawable()
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
            val isButton = view is SquircleButton

            try {
                view.isClickable = attrs?.getAttributeBooleanValue(android.R.attr.clickable, isButton) ?: isButton
                view.isFocusable = attrs?.getAttributeBooleanValue(android.R.attr.focusable, isButton) ?: isButton
            } catch (e: IndexOutOfBoundsException) {
                // Android Studio preview fails when retrieving this value
            }

            // Set ripple if enabled
            if (rippleEnabled && view.hasOnClickListeners()) {
                view.foreground = rippleDrawable ?: context.getDefaultRippleDrawable()
            } else {
                view.foreground = context.getTransparentRippleDrawable()
            }

            if (view is SquircleButton) {
                view.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
                loadButtonTextAppearance(context, view, attrs)
            }
        }
    }

    /**
     * Setting the text appearance programmatically unfortunately overrides all user-defined styles. Therefore,
     * the decision has been made to apply the attributes in the style programmatically
     *
     * Original code:
     * val text = TypedValue().also { context.theme.resolveAttribute(android.R.attr.textAppearanceButton, it, true) }.resourceId
     * setTextAppearance(view, text)
     *
     * Retrieved from com.google.android.material:material:1.4.0/res/values/values.xml:
     * android.R.attr.textAppearanceButton (which as has `Base.TextAppearance.MaterialComponents.Button` as parent):
     * <style name="Base.TextAppearance.MaterialComponents.Button" parent="TextAppearance.AppCompat.Button">
     *     <!-- Fake Roboto Medium. -->
     *     <item name="fontFamily">sans-serif-medium</item>
     *     <item name="android:fontFamily">sans-serif-medium</item>
     *     <item name="android:textStyle">bold</item>
     *     <item name="android:textAllCaps">true</item>
     *     <item name="android:textSize">14sp</item>
     *     <item name="android:letterSpacing">0.0892857143</item>
     * </style>
     *
     */
    private fun loadButtonTextAppearance(context: Context, view: SquircleButton, attrs: AttributeSet?) {
        // Restore original text color which is overridden by the text appearance
        context.obtainStyledAttributes(attrs, R.styleable.Default).apply {
            // Set the fontFamily and textStyle, which defaults to sans-serif-medium
            val fontFamily = getString(R.styleable.Default_android_fontFamily) ?: BUTTON_TEXT_APPEARANCE_DEFAULT_FONT_FAMILY
            view.typeface = Typeface.create(
                fontFamily, getInt(R.styleable.Default_android_textStyle, BUTTON_TEXT_APPEARANCE_DEFAULT_TEXT_STYLE)
            )

            // Set the text size, which defaults to 14sp
            val textSize = getDimensionPixelSize(R.styleable.Default_android_textSize, -1)
            if (textSize == -1) {
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, BUTTON_TEXT_APPEARANCE_DEFAULT_TEXT_SIZE_IN_SP)
            } else {
                view.textSize = textSize.toFloat()
            }

            // Set the textAllCaps
            if (getBoolean(R.styleable.Default_android_textAllCaps, BUTTON_TEXT_APPEARANCE_DEFAULT_ALL_CAPS)) {
                view.transformationMethod = TextAllCapsTransformationMethod()
            } else {
                view.transformationMethod = null
            }

            // Set the letter spacing, which has a weird default value
            view.letterSpacing = getFloat(R.styleable.Default_android_letterSpacing, BUTTON_TEXT_APPEARANCE_DEFAULT_LETTER_SPACING)

            recycle()
        }
    }

    companion object {
        const val BUTTON_TEXT_APPEARANCE_DEFAULT_FONT_FAMILY = "sans-serif-medium"
        const val BUTTON_TEXT_APPEARANCE_DEFAULT_TEXT_STYLE = Typeface.NORMAL
        const val BUTTON_TEXT_APPEARANCE_DEFAULT_TEXT_SIZE_IN_SP = 14f
        const val BUTTON_TEXT_APPEARANCE_DEFAULT_ALL_CAPS = true
        const val BUTTON_TEXT_APPEARANCE_DEFAULT_LETTER_SPACING = 0.0892857143f
    }
}