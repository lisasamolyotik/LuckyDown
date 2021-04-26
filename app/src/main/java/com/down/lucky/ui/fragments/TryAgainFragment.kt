package com.down.lucky.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.down.lucky.R
import com.down.lucky.databinding.TryAgainScreenBinding

class TryAgainFragment : Fragment(R.layout.try_again_screen) {
    private var _binding: TryAgainScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TryAgainScreenBinding.inflate(inflater, container, false)
        initializeButtons()
        return binding.root
    }

    private fun initializeButtons() {
        binding.homeButton.setOnClickListener {
            parentFragmentManager.popBackStack()
            parentFragmentManager.popBackStack()
        }
        binding.repeatButton.setOnClickListener {
            parentFragmentManager.popBackStack()
            parentFragmentManager.popBackStack()
            parentFragmentManager.commit {
                replace<LevelFragment>(R.id.container)
            }
        }
    }
}