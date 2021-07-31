package app.juky.squircleview.data;

object Constants {
    const val DEFAULT_CORNER_SMOOTHING : Long = 100 // Use 100% corner smoothing by default, which trickles down to the 64% below
    internal const val MAX_DEFAULT_CORNER_SMOOTHING = 0.64f // Default (and max) Squircle corner smoothing is 64%
}
