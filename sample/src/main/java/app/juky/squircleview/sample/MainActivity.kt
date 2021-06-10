package app.juky.squircleview.sample

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import app.juky.squircleview.sample.databinding.ActivityMainBinding
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
    }

    private fun loadImageDrawable() {
        Glide.with(applicationContext).load(R.drawable.first_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.imageButton)
    }

    private fun loadImageWithResourceLoading() {
        Glide.with( applicationContext).load(R.drawable.second_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(object : CustomViewTarget<SquircleImageView, Drawable>(binding.imageButton) {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    binding.imageButton.setImage(resource)
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