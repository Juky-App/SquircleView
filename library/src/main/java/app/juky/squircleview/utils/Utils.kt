package app.juky.squircleview.utils

import android.graphics.Rect
import android.text.method.TransformationMethod
import android.view.View


class TextAllCapsTransformationMethod : TransformationMethod {
    override fun getTransformation(source: CharSequence, view: View): CharSequence = when {
        source.isEmpty() -> source
        else -> source.toString().uppercase(view.context.resources.configuration.locale)
    }

    override fun onFocusChanged(view: View?, sourceText: CharSequence?, focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        // Only needs to be overridden
    }
}