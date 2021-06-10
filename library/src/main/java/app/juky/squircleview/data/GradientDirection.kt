package app.juky.squircleview.data

import android.graphics.drawable.GradientDrawable
import android.view.View
import kotlin.math.abs

internal enum class GradientDirection {
    TOP_BOTTOM,
    TOP_RIGHT_BOTTOM_LEFT,
    RIGHT_LEFT,
    BOTTOM_RIGHT_TOP_LEFT,
    BOTTOM_TOP,
    BOTTOM_LEFT_TOP_RIGHT,
    LEFT_RIGHT,
    TOP_LEFT_BOTTOM_RIGHT;

    companion object {
        private const val OFFSET = 22.5

        /**
         * Get the GradientCoordinates using a given angle, which is equivalent to the way Android does
         * it under the hood.
         *
         * @param view View View used to determine the startX/startY and endX/endY coordinates
         * @param angle Int Angle of the gradient ranging from 0 to 360
         * @return GradientCoordinates Direction of the gradient
         */
        fun getCoordinates(view: View, angle: Int): GradientCoordinates {
            // Target all pieces of a full circle, each angle will be 45 degrees so it corresponds with the predefined directions
            return getCoordinatesByDirection(
                view = view,
                direction = when (abs(angle % 360).toDouble()) {
                    in (0.0)..(0 + OFFSET) -> LEFT_RIGHT
                    in (0 + OFFSET)..(45 + OFFSET) -> BOTTOM_LEFT_TOP_RIGHT
                    in (45 + OFFSET)..(90 + OFFSET) -> BOTTOM_TOP
                    in (90 + OFFSET)..(135 + OFFSET) -> BOTTOM_RIGHT_TOP_LEFT
                    in (135 + OFFSET)..(180 + OFFSET) -> RIGHT_LEFT
                    in (180 + OFFSET)..(225 + OFFSET) -> TOP_RIGHT_BOTTOM_LEFT
                    in (225 + OFFSET)..(270 + OFFSET) -> TOP_BOTTOM
                    in (270 + OFFSET)..(315 + OFFSET) -> TOP_LEFT_BOTTOM_RIGHT
                    else -> TOP_LEFT_BOTTOM_RIGHT
                }
            )
        }

        /**
         * Get GradientCoordinates using the GradientDrawable Orientation
         *
         * @param view View View used to determine the startX/startY and endX/endY coordinates
         * @param orientation Orientation Orientation of the GradientDrawable
         * @return GradientCoordinates Direction of the gradient
         */
        fun getCoordinates(view: View, orientation: GradientDrawable.Orientation): GradientCoordinates {
            return getCoordinatesByDirection(view, getDirectionByOrientation(orientation))
        }

        /**
         * Get GradientDirection using a given direction
         *
         * @param view View View used to determine the startX/startY and endX/endY coordinates
         * @param direction GradientDirections Direction used to get the gradient coordinates
         * @return GradientCoordinates
         */
        fun getCoordinatesByDirection(view: View, direction: GradientDirection): GradientCoordinates = when (direction) {
            TOP_BOTTOM -> GradientCoordinates(
                startX = view.width / 2,
                startY = 0,
                endX = view.width / 2,
                endY = view.height
            )
            TOP_RIGHT_BOTTOM_LEFT -> GradientCoordinates(
                startX = view.width,
                startY = 0,
                endX = 0,
                endY = view.height
            )
            RIGHT_LEFT -> GradientCoordinates(
                startX = view.width,
                startY = view.height / 2,
                endX = 0,
                endY = view.height / 2
            )
            BOTTOM_RIGHT_TOP_LEFT -> GradientCoordinates(
                startX = view.width,
                startY = view.height,
                endX = 0,
                endY = 0
            )
            BOTTOM_TOP -> GradientCoordinates(
                startX = view.width / 2,
                startY = view.height,
                endX = view.width / 2,
                endY = 0
            )
            BOTTOM_LEFT_TOP_RIGHT -> GradientCoordinates(
                startX = 0,
                startY = view.height,
                endX = view.width,
                endY = 0
            )
            LEFT_RIGHT -> GradientCoordinates(
                startX = 0,
                startY = view.height / 2,
                endX = view.width,
                endY = view.height / 2
            )
            TOP_LEFT_BOTTOM_RIGHT -> GradientCoordinates(
                startX = 0,
                startY = 0,
                endX = view.width,
                endY = view.height
            )
        }

        /**
         * Get the direction by the given GradientDrawable Orientation
         *
         * @param orientation Orientation Orientation of the GradientDrawable
         * @return GradientDirection Direction of the gradient
         */
        private fun getDirectionByOrientation(orientation: GradientDrawable.Orientation): GradientDirection {
            return when (orientation) {
                GradientDrawable.Orientation.TOP_BOTTOM -> TOP_BOTTOM
                GradientDrawable.Orientation.TR_BL -> TOP_RIGHT_BOTTOM_LEFT
                GradientDrawable.Orientation.RIGHT_LEFT -> RIGHT_LEFT
                GradientDrawable.Orientation.BR_TL -> BOTTOM_RIGHT_TOP_LEFT
                GradientDrawable.Orientation.BOTTOM_TOP -> BOTTOM_TOP
                GradientDrawable.Orientation.BL_TR -> BOTTOM_LEFT_TOP_RIGHT
                GradientDrawable.Orientation.LEFT_RIGHT -> LEFT_RIGHT
                GradientDrawable.Orientation.TL_BR -> TOP_LEFT_BOTTOM_RIGHT
            }
        }
    }

    class GradientCoordinates(
        val startX: Int,
        val startY: Int,
        val endX: Int,
        val endY: Int
    )
}