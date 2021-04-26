package com.down.lucky.ui.fragments

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.down.lucky.R
import com.down.lucky.databinding.YouWinScreenBinding
import com.down.lucky.model.Puzzle

class YouWinFragment : Fragment(R.layout.you_win_screen) {
    private var _binding: YouWinScreenBinding? = null
    private val binding get() = _binding!!
    private var animator: ObjectAnimator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = YouWinScreenBinding.inflate(inflater, container, false)
        initializeButtons()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        animateAlert()
    }

    override fun onDestroy() {
        super.onDestroy()
        animator?.cancel()
        animator = null
    }

    private fun initializeButtons() {
        binding.homeButton.setOnClickListener {
            Puzzle.nextPiece()
            parentFragmentManager.popBackStack()
            parentFragmentManager.popBackStack()
        }
        binding.repeatButton.setOnClickListener {
            Puzzle.nextPiece()
            parentFragmentManager.popBackStack()
            parentFragmentManager.popBackStack()
            parentFragmentManager.commit {
                replace<LevelFragment>(R.id.container)
            }
        }
        binding.alert.setOnClickListener {
            parentFragmentManager.commit {
                add<PuzzleFragment>(R.id.container)
                addToBackStack(null)
            }
        }
    }

    private fun animateAlert() {
        animator = ObjectAnimator.ofPropertyValuesHolder(
            binding.alert, PropertyValuesHolder.ofFloat("scaleX", 1.2f),
            PropertyValuesHolder.ofFloat("scaleY", 1.2f)
        )
        animator?.duration = 1000L
        animator?.interpolator = FastOutSlowInInterpolator()
        animator?.repeatCount = ObjectAnimator.INFINITE
        animator?.repeatMode = ObjectAnimator.REVERSE
        animator?.start()
    }
}