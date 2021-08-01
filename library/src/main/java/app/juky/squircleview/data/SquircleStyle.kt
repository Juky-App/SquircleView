package app.juky.squircleview.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import app.juky.squircleview.utils.SquircleGradient
import app.juky.squircleview.utils.SquircleShadowProvider.getShadowProvider
import app.juky.squircleview.utils.getDefaultRippleDrawable
import app.juky.squircleview.utils.getTransparentRippleDrawable

/**
 * Because there are multiple Squircle components (Button, ImageView, ConstraintLayout) there is no possibility
 * to have 1 super class which will implement all variables. In order to support programmatically changing the
 * attributes, this style class has been created which will directly communicate with the View and the Core to
 * update the styles where necessary.
 */
class SquircleStyle(val context: Context, val view: View, internal val core: SquircleCore) {
    /**
     * Set the background image
     */
    var backgroundImage: Bitmap?
        get() = core.backgroundImage
        set(value) {
            core.backgroundImage = value
            view.invalidate()
        }

    /**
     * Set the background color by color int
     */
    var backgroundColor: Int
        get() = core.backgroundColor
        set(@ColorInt color) {
            core.backgroundColor = color
            core.shapePaint.color = core.backgroundColor
            view.invalidate()
        }

    /**
     * Set the background color by resource id
     */
    var backgroundColorRes: Int
        get() = core.backgroundColor
        set(@ColorRes resId) {
            backgroundColor = ContextCompat.getColor(view.context, resId)
        }

    /**
     * Set the elevation of the shadow
     */
    var shadowElevation: Float
        get() = core.shadowElevation
        set(value) {
            core.shadowElevation = value
            view.elevation = shadowElevation
            view.invalidate()
        }

    /**
     * Set the shadow color of the elevation by color int
     */
    var shadowElevationColor: Int
        get() = core.shadowElevationColor
        set(@ColorInt color) {
            core.shadowElevationColor = color

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                view.outlineAmbientShadowColor = core.shadowElevationColor
                view.outlineSpotShadowColor = core.shadowElevationColor
                view.outlineProvider = getShadowProvider()
            }

            view.invalidate()
        }

    /**
     * Set the shadow color of the elevation by resource id
     */
    var shadowElevationColorRes: Int
        get() = core.shadowElevationColor
        set(@ColorRes resId) {
            shadowElevationColor = ContextCompat.getColor(view.context, resId)
        }

    /**
     * Set a drawable gradient as background. Note: Currently, only Gradient Drawables which have
     * a shape around it are supported, though you could just use a shape without specifying anything.
     *
     * ```
     * <?xml version="1.0" encoding="utf-8"?>
     * <shape xmlns:android="http://schemas.android.com/apk/res/android">
     *     <gradient
     *         android:endColor="#EC5396"
     *         android:startColor="#FFCB71"
     *         android:type="linear" />
     * </shape>
     * ```
     */
    var gradientDrawable: GradientDrawable?
        get() = core.gradientDrawable
        set(drawable) {
            core.gradientDrawable = drawable
            view.invalidate()
        }

    /**
     * Set the gradient start color by color int
     */
    var gradientStartColor: Int
        get() = core.gradientStartColor
        set(@ColorInt color) {
            core.gradientStartColor = color
            view.invalidate()
        }

    /**
     * Set the gradient start color by resource ID. Note: this CANNOT be an android.R.color.* value, because
     * it won't work with a LinearGradient.
     */
    var gradientStartColorRes: Int
        get() = core.gradientStartColor
        set(@ColorRes resId) {
            gradientStartColor = ContextCompat.getColor(view.context, resId)
        }

    /**
     * Set the gradient end color by color int
     */
    var gradientEndColor: Int
        get() = core.gradientEndColor
        set(@ColorInt color) {
            core.gradientEndColor = color
            view.invalidate()
        }

    /**
     * Set the gradient end color by resource ID. Note: this CANNOT be an android.R.color.* value, because
     * it won't work with a LinearGradient.
     */
    var gradientEndColorRes: Int
        get() = core.gradientEndColor
        set(@ColorRes resId) {
            gradientEndColor = ContextCompat.getColor(view.context, resId)
        }

    /**
     * Set the gradient direction using a GradientDirection
     */
    var gradientDirection: GradientDirection
        get() = core.gradientDirection
        set(direction) {
            core.gradientDirection = direction
            SquircleGradient.onViewSizeChanged(view.width, view.height, view, core)
        }

    /**
     * Set the border color by color int
     */
    var borderColor: Int
        get() = core.borderColor
        set(@ColorInt color) {
            core.borderColor = color

            core.borderPaint.apply {
                this.color = core.borderColor
            }

            view.invalidate()
        }

    /**
     * Set the border color by resource ID
     */
    var borderColorRes: Int
        get() = core.borderColor
        set(@ColorRes resId) {
            borderColor = ContextCompat.getColor(view.context, resId)
        }

    /**
     * Set the width of the border
     */
    var borderWidth: Float
        get() = core.borderWidth
        set(width) {
            core.borderWidth = width

            core.borderPaint.apply {
                this.strokeWidth = core.borderWidth
            }

            view.invalidate()
        }

    /**
     * Enable or disable the ripple effect
     */
    var rippleEnabled: Boolean
        get() = core.rippleEnabled
        set(enabled) {
            core.rippleEnabled = enabled

            if (rippleEnabled && view.hasOnClickListeners()) {
                view.foreground = rippleDrawable ?: context.getDefaultRippleDrawable()
            } else {
                view.foreground = context.getTransparentRippleDrawable()
            }

            view.invalidate()
        }

    /**
     * Set a drawable as ripple foreground
     *
     * ```
     * <ripple xmlns:android="http://schemas.android.com/apk/res/android"
     *     android:color="#2C1D1F">
     *     <item android:id="@android:id/mask">
     *         <shape android:shape="rectangle">
     *             <solid android:color="#2C1D1F" />
     *         </shape>
     *     </item>
     * </ripple>
     * ```
     */
    var rippleDrawable: Drawable?
        get() = core.rippleDrawable
        set(drawable) {
            core.rippleDrawable = drawable
            if (view.hasOnClickListeners()) view.foreground = core.rippleDrawable
            view.invalidate()
        }

    /**
     * Retrieve the configured corner smoothing percentage
     */
    fun getCornerSmoothing() = core.cornerSmoothing

    /**
     * Change the default corner smoothing if you want to deflect from the original Squircle. Default Squircle value is 67%
     */
    fun setCornerSmoothing(@IntRange(from = 0, to = Constants.DEFAULT_CORNER_SMOOTHING) cornerSmoothing: Int) {
        core.cornerSmoothing = cornerSmoothing
        view.invalidate()
    }

    /**
     * Set the background image using a drawable
     *
     * @param drawable Drawable? Background image drawable
     */
    fun setBackgroundImage(drawable: Drawable?) {
        core.backgroundImage = drawable?.toBitmap()
        view.invalidate()
    }

    /**
     * Set background image with a drawable resource id
     *
     * @param resId Int Drawable resource ID
     */
    fun setBackgroundImage(@DrawableRes resId: Int) {
        setBackgroundImage(ContextCompat.getDrawable(context, resId))
    }

    /**
     * Set the gradient as background using a gradient resource id. Note: Currently, only Gradient Drawables which have
     * a shape around it are supported, though you could just use a shape without specifying anything.
     *
     * ```
     * <?xml version="1.0" encoding="utf-8"?>
     * <shape xmlns:android="http://schemas.android.com/apk/res/android">
     *     <gradient
     *         android:endColor="#EC5396"
     *         android:startColor="#FFCB71"
     *         android:type="linear" />
     * </shape>
     * ```
     * @param resId Int Resource id of gradient
     */
    fun setGradientDrawable(@DrawableRes resId: Int) {
        core.gradientDrawable = ContextCompat.getDrawable(context, resId) as? GradientDrawable
        view.invalidate()
    }

    /**
     * Set the gradient direction using an angle from 0 to 360
     *
     * @param angle Int Angle from 0 to 360 degrees
     */
    fun setGradientDirection(angle: Int) {
        core.gradientDirection = GradientDirection.getByAngle(angle)
        SquircleGradient.onViewSizeChanged(view.width, view.height, view, core)
    }

    /**
     * Setup a ripple, which will usually happen after a click listener has been set afterwards
     */
    internal fun setupRipple() {
        // Trigger setter which will invalidate the view
        rippleEnabled = core.rippleEnabled
    }
}