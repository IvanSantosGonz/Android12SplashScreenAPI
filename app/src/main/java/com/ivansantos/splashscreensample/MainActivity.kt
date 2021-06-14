package com.ivansantos.splashscreensample

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnPreDrawListener
import android.view.animation.AnticipateInterpolator
import androidx.appcompat.app.AppCompatActivity

const val isReady = true
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check if the initial data is ready.
                    return if (isReady) {
                        // The content is ready; start drawing.
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        // The content is not ready; suspend.
                        false
                    }
                }
            }
        )

        splashScreen.setOnExitAnimationListener { view ->
            view.iconView?.let { icon ->
                // set an animator that goes from height to 0, it will use AnticipateInterpolator set at the bottom of this code
                val animator = ValueAnimator
                    .ofInt(icon.height, 0)
                    .setDuration(2000)
                //update the icon height and width every time the animator value change
                animator.addUpdateListener {
                    val value = it.animatedValue as Int
                    icon.layoutParams.width = value
                    icon.layoutParams.height = value
                    icon.requestLayout()
                    if (value == 0) {
                        view.remove()
                    }
                }
                val animationSet = AnimatorSet()
                animationSet.interpolator = AnticipateInterpolator()
                // Default tension of AnticipateInterpolator is 2,
                // this means that the animation will increase first the size of the icon a little bit and then decrease
                animationSet.play(animator)
                animationSet.start() // Launch the animation
            }
        }
    }
}