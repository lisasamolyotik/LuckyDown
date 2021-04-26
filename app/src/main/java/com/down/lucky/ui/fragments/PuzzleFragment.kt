package com.down.lucky.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.down.lucky.R
import com.down.lucky.databinding.PuzzleScreenBinding
import com.down.lucky.model.Puzzle
import com.down.lucky.util.PuzzleCreator

class PuzzleFragment : Fragment(R.layout.puzzle_screen) {
    private var _binding: PuzzleScreenBinding? = null
    val binding get() = _binding!!

    private var layoutChangeListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PuzzleScreenBinding.inflate(inflater, container, false)
        binding.puzzleImage.setImageResource(Puzzle.image)
        layoutChangeListener = ViewTreeObserver.OnGlobalLayoutListener {
            val puzzleCreator = PuzzleCreator(requireContext())
            val pieces = puzzleCreator.splitImage(binding.puzzleImage)
            for (piece in pieces) {
                binding.relativeLayout.addView(piece)
                val params = piece.layoutParams as RelativeLayout.LayoutParams
                params.marginStart = piece.xPos
                params.topMargin = piece.yPos
                piece.layoutParams = params
            }
            binding.relativeLayout.viewTreeObserver.removeOnGlobalLayoutListener(layoutChangeListener)
            layoutChangeListener = null
        }
        binding.relativeLayout.viewTreeObserver.addOnGlobalLayoutListener(layoutChangeListener)
        return binding.root
    }


}