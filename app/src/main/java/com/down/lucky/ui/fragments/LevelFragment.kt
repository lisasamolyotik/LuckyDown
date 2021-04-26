package com.down.lucky.ui.fragments

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.down.lucky.R
import com.down.lucky.databinding.LevelScreenBinding
import com.down.lucky.model.Ball
import com.down.lucky.model.Level
import com.down.lucky.ui.MainActivity
import com.down.lucky.util.DimensionConverter
import kotlin.random.Random

class LevelFragment : Fragment(R.layout.level_screen) {
    private var _binding: LevelScreenBinding? = null
    private val binding get() = _binding!!
    private var xDelta = 0f
    private var layoutChangedListener: ViewTreeObserver.OnGlobalLayoutListener? = null
    private val balls = mutableListOf<Ball>()
    private val basketLocation = IntArray(2)
    private var duration = 3000L
    private var basketPadding = 15
    private var basketWidth = 0
    private var basketHeight = 0
    var timer: CountDownTimer? = null
    private var time = Level.time
    private val level = Level()
    private var isGameActive = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LevelScreenBinding.inflate(inflater, container, false)
        Level.level = MainActivity.preferences?.getInt(LEVEL_KEY, 1) ?: 1
        moveBasket()
        initializeTexts()
        if (Level.fallingSpeed > 1) {
            duration -= Level.fallingSpeed * 300L
        }
        basketPadding = DimensionConverter.dpToPixels(basketPadding.toFloat(), requireContext())
        layoutChangedListener = ViewTreeObserver.OnGlobalLayoutListener {
            binding.basket.getLocationInWindow(basketLocation)
            basketWidth = binding.basket.width
            basketHeight = binding.basket.height
            generateBall()
            binding.layout.viewTreeObserver.removeOnGlobalLayoutListener(layoutChangedListener)
            layoutChangedListener = null
        }
        binding.layout.viewTreeObserver.addOnGlobalLayoutListener(layoutChangedListener)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        timer = object : CountDownTimer(time, 500) {
            override fun onTick(millisUntilFinished: Long) {
                time = millisUntilFinished
                binding.seconds.text = (time / 1000).toString()
                if (Level.generationSpeed == 1 && (time / 500) % 6 == 0L) {
                    generateBall()
                } else if (Level.generationSpeed == 2 && (time / 500) % 4 == 0L) {
                    generateBall()
                } else if (Level.generationSpeed == 3 && (time / 500) % 2 == 0L) {
                    generateBall()
                } else if (Level.generationSpeed == 4) {
                    generateBall()
                }
            }

            override fun onFinish() {
                openWinFragment()
                Level.nextLevel()
                val editor = MainActivity.preferences?.edit()
                editor?.putInt(LEVEL_KEY, Level.level)
                editor?.apply()
            }
        }
        (timer as CountDownTimer).start()
    }

    override fun onPause() {
        super.onPause()
        isGameActive = false
        stopAnimation()
    }

    private fun initializeTexts() {
        binding.lifesTextView.text = level.lifes.toString()
        binding.scoreTextView.text = getString(R.string.score, level.score)
        binding.speed.text = getString(R.string.speed, Level.generationSpeed)
        binding.levelTextView.text = Level.level.toString()
    }

    private fun generateBall() {
        if (isGameActive) {
            val toxic = Random.nextBoolean()
            val ball = Ball(requireActivity(), toxic)
            if (ball.toxic) {
                ball.setImageResource(Ball.toxicBalls.random())
            } else {
                ball.setImageResource(Ball.safeBalls.random())
            }
            ball.adjustViewBounds = true
            val params =
                RelativeLayout.LayoutParams(binding.ball.layoutParams as RelativeLayout.LayoutParams)
            params.marginStart = generateRandomMargin()
            ball.layoutParams = params
            binding.relativeLayout.addView(ball)
            ball.getLocationInWindow(ball.location)
            balls.add(ball)
            animateBall(ball)
        }
    }

    private fun animateBall(ball: Ball) {
        ball.animator =
            ObjectAnimator.ofFloat(ball, "translationY", binding.layout.height.toFloat())
        ball.animator?.interpolator = LinearInterpolator()
        ball.animator?.duration = duration
        ball.animator?.start()
        ball.animator?.addUpdateListener {
            ball.getLocationInWindow(ball.location)
            if (checkIfBallIsInsideBasket(ball.location) && isGameActive) {
                if (ball.toxic) {
                    ball.visibility = View.GONE
                    balls.remove(ball)
                    openLoseFragment()
                } else {
                    ball.animator?.cancel()
                    ball.visibility = View.GONE
                    balls.remove(ball)
                    level.score++
                    binding.scoreTextView.text = getString(R.string.score, level.score)
                }
            } else if (ball.location[1] > basketLocation[1] + basketHeight && isGameActive) {
                if (!ball.toxic) {
                    level.lifes--
                    binding.lifesTextView.text = level.lifes.toString()
                    checkLifes()
                }
                ball.animator?.cancel()
                balls.remove(ball)
                ball.visibility = View.GONE
            }
        }
    }

    private fun checkLifes() {
        if (level.lifes == 0) {
            openLoseFragment()
            stopAnimation()
        }
    }

    private fun checkIfBallIsInsideBasket(ballLocation: IntArray): Boolean {
        return ballLocation[1] >= basketLocation[1] &&
                ballLocation[0] >= basketLocation[0] &&
                ballLocation[0] <= basketLocation[0] + basketWidth - 2 * basketPadding
    }

    private fun generateRandomMargin(): Int {
        return Random.nextInt(15, binding.layout.width - binding.ball.width - 15)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun moveBasket() {
        binding.basket.setOnTouchListener { v, event ->
            if (isGameActive) {
                val x = event.rawX
                val lParams = v.layoutParams as ConstraintLayout.LayoutParams
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_DOWN -> {
                        xDelta = x - lParams.leftMargin
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val leftMargin = (x - xDelta).toInt()
                        if (leftMargin < binding.layout.width - binding.basket.width) {
                            lParams.leftMargin = leftMargin
                            v.layoutParams = lParams
                            v.getLocationInWindow(basketLocation)
                        }
                    }
                }
                true
            } else false
        }
    }

    private fun openWinFragment() {
        stopAnimation()
        isGameActive = false
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            add<YouWinFragment>(R.id.container)
            addToBackStack(null)
        }
    }

    private fun openLoseFragment() {
        stopAnimation()
        isGameActive = false
        timer?.cancel()
        stopAnimation()
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            add<TryAgainFragment>(R.id.container)
            addToBackStack(null)
        }
    }

    private fun stopAnimation() {
        for (ball in balls) {
            ball.animator?.cancel()
            ball.animator = null
        }
    }

    companion object {
        const val LEVEL_KEY = "level"

    }
}