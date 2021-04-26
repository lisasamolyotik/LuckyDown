package com.down.lucky.model

import android.animation.ObjectAnimator
import android.content.Context
import com.down.lucky.R

class Ball(context: Context, val toxic: Boolean) : androidx.appcompat.widget.AppCompatImageView(context) {
    val location = IntArray(2)
    var animator: ObjectAnimator? = null

    companion object {
        val toxicBalls = arrayOf(
            R.drawable.blue_ball_dead,
            R.drawable.green_ball_dead,
            R.drawable.purple_ball_dead,
            R.drawable.red_ball_dead,
            R.drawable.yellow_ball_dead
        )

        val safeBalls = arrayOf(
            R.drawable.blue_ball,
            R.drawable.green_ball,
            R.drawable.purple_ball,
            R.drawable.red_ball,
            R.drawable.yellow_ball
        )
    }
}