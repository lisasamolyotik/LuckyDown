package com.down.lucky.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.down.lucky.R
import com.down.lucky.databinding.MenuScreenBinding

class MenuFragment : Fragment(R.layout.menu_screen) {
    private var _binding: MenuScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MenuScreenBinding.inflate(inflater, container, false)
        initializeButtons()
        return binding.root
    }

    private fun initializeButtons() {
        binding.playButton.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace<LevelFragment>(R.id.container)
                addToBackStack(null)
            }
        }

        binding.rulesButton.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace<RulesFragment>(R.id.container)
                addToBackStack(null)
            }
        }

        binding.settingsButton.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace<SettingsFragment>(R.id.container)
                addToBackStack(null)
            }
        }
    }
}