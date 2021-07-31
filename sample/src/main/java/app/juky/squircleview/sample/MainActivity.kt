package app.juky.squircleview.sample

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import app.juky.squircleview.sample.databinding.ActivityMainBinding
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

        // Examples of changing the view properties
        // binding.normalButton.style.backgroundImage = ContextCompat.getDrawable(this, R.drawable.first_image)?.toBitmap()
        // binding.normalButton.style.backgroundColor = Color.RED
        // binding.normalButton.style.backgroundColorRes = R.color.teal_200
        // binding.normalButton.style.shadowElevation = 8f
        // binding.normalButton.style.shadowElevationColor = Color.GREEN
        // binding.normalButton.style.shadowElevationColorRes = R.color.teal_200
        // binding.normalButton.style.gradientDrawable = ContextCompat.getDrawable(this, R.drawable.gradient_second) as? GradientDrawable
        // binding.normalButton.style.gradientStartColor = Color.BLUE
        // binding.normalButton.style.gradientStartColorRes = R.color.purple_500
        // binding.normalButton.style.gradientEndColor = Color.YELLOW
        // binding.normalButton.style.gradientEndColorRes = R.color.teal_700
        // binding.normalButton.style.gradientDirection = GradientDirection.BOTTOM_TOP
        // binding.normalButton.style.borderColor = Color.CYAN
        // binding.normalButton.style.borderColorRes = R.color.purple_500
        // binding.normalButton.style.borderWidth = 20f
        // binding.normalButton.style.rippleEnabled = false

        // binding.normalButton.style.setBackgroundImage(ContextCompat.getDrawable(this, R.drawable.second_image))
        // binding.normalButton.style.setBackgroundImage(R.drawable.third_image)
        // binding.normalButton.style.setGradientDrawable(R.drawable.gradient_second)
        // binding.normalButton.style.setGradientDirection(200)
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