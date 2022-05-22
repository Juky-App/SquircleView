package app.juky.squircleview.sample

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import app.juky.squircleview.sample.databinding.ActivityMainBinding
import app.juky.squircleview.utils.SquircleComposeShape.SquircleComposeShape
import app.juky.squircleview.utils.SquircleComposeShape.getSquirclePath
import app.juky.squircleview.utils.SquircleShape
import app.juky.squircleview.views.SquircleImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadImageDrawable()

        Handler(Looper.getMainLooper()).postDelayed({
            loadImageWithResourceLoading()
        }, 5000)

        // Slide the seeker to change the corner smoothing
        binding.seekerValue.text = binding.seeker.progress.toString()
        binding.seeker.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tester.shapeAppearanceModel = SquircleShape.getShapeAppearance(progress).build()
                binding.firstRowItem.style.setCornerSmoothing(progress)
                binding.secondRowItem.style.setCornerSmoothing(progress)
                binding.thirdRowItem.style.setCornerSmoothing(progress)
                binding.fourthRowItem.style.setCornerSmoothing(progress)
                binding.normalButton.style.setCornerSmoothing(progress)
                binding.normalNoGradientButton.style.setCornerSmoothing(progress)
                binding.imageButton.style.setCornerSmoothing(progress)
                binding.normalButtonWithImage.style.setCornerSmoothing(progress)
                binding.verticalButton.style.setCornerSmoothing(progress)
                binding.seekerValue.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // The methods down below are native ShapeDrawable and ShapeAppearance usages
        binding.buttonWithShapeDrawable.shapeAppearanceModel = SquircleShape.getShapeAppearance().build()
        binding.imageWithShapeAppearance.shapeAppearanceModel = SquircleShape.getShapeAppearance().build()
        binding.constraintLayoutWithShapeDrawable.background = SquircleShape.getShapeDrawable(binding.constraintLayoutWithShapeDrawable).apply {
            this.paint.apply {
                this.color = ContextCompat.getColor(this@MainActivity, R.color.teal_700)
            }
        }
        binding.constraintLayoutWithShapeDrawableCompose.setContent {
            val borderGradient = Brush.linearGradient(
                colors = listOf(
                    colorResource(id = R.color.teal_200),
                    colorResource(id = R.color.teal_700)
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .graphicsLayer(
                        shadowElevation = 8f,
                        shape = SquircleComposeShape(cornerSmoothing = 80),
                        clip = true
                    )
                    .background(color = colorResource(id = R.color.purple_200))
                    .drawBehind {
                        drawPath(
                            path = getSquirclePath(size, cornerSmoothing = 80),
                            brush = borderGradient,
                            style = Stroke(
                                width = 3.dp.toPx()
                            )
                        )
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.app_name))
            }
        }

        binding.normalButton.setOnClickListener {
            // Just demonstrating a ripple will only work with a click listener
        }

        Handler(Looper.getMainLooper()).postDelayed({
            // Just demonstrating a ripple will only work with a click listener
            binding.normalNoGradientButton.setOnClickListener {
                // Nothing
            }
        }, 5000)

        // Examples of changing the view properties
        // binding.normalButton.style.backgroundImage = ContextCompat.getDrawable(this, R.drawable.first_image)?.toBitmap()
        // binding.normalButton.style.backgroundColor = Color.RED
        // binding.normalButton.style.backgroundColorRes = R.color.teal_200
        // binding.normalButton.style.shadowElevation = 8f
        // binding.normalButton.style.shadowElevationColor = Color.GREEN
        // binding.normalButton.style.shadowElevationColorRes = R.color.teal_200
        // binding.normalButton.style.backgroundGradientDrawable = ContextCompat.getDrawable(this, R.drawable.gradient_second) as? GradientDrawable
        // binding.normalButton.style.backgroundGradientStartColor = Color.BLUE
        // binding.normalButton.style.backgroundGradientStartColorRes = R.color.purple_500
        // binding.normalButton.style.backgroundGradientEndColor = Color.YELLOW
        // binding.normalButton.style.backgroundGradientEndColorRes = R.color.teal_700
        // binding.normalButton.style.backgroundGradientDirection = GradientDirection.BOTTOM_TOP
        // binding.normalButton.style.borderColor = Color.CYAN
        // binding.normalButton.style.borderColorRes = R.color.purple_500
        // binding.normalButton.style.borderWidth = 20f
        // binding.normalButton.style.rippleEnabled = false
        // binding.normalButton.style.rippleDrawable = ContextCompat.getDrawable(this, R.drawable.ripple)

        // binding.normalButton.style.setBackgroundImage(ContextCompat.getDrawable(this, R.drawable.second_image))
        // binding.normalButton.style.setBackgroundImage(R.drawable.third_image)
        // binding.normalButton.style.setBackgroundGradientDrawable(R.drawable.gradient_second)
        // binding.normalButton.style.setBackgroundGradientDirection(200)
    }

    private fun loadImageDrawable() {
        Glide.with(applicationContext).load(R.drawable.first_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.imageButton)
    }

    private fun loadImageWithResourceLoading() {
        Glide.with(applicationContext).load(R.drawable.second_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(object : CustomViewTarget<SquircleImageView, Drawable>(binding.imageButton) {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    binding.imageButton.style.setBackgroundImage(resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    //
                }

                override fun onResourceCleared(placeholder: Drawable?) {
                    //
                }
            })
    }
}