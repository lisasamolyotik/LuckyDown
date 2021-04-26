package com.down.lucky.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.down.lucky.R
import com.down.lucky.databinding.SettingsScreenBinding
import com.down.lucky.ui.MainActivity

class SettingsFragment : Fragment(R.layout.settings_screen) {
    private var _binding: SettingsScreenBinding? = null
    private val binding get() = _binding!!
    private var music = true
    private var vibro = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsScreenBinding.inflate(inflater, container, false)
        music = MainActivity.preferences?.getBoolean(MainActivity.MUSIC, true) == true
        vibro = MainActivity.preferences?.getBoolean(MainActivity.VIBRO, true) == true
        changeMusicImage()
        changeVibroImage()

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.musicButton.setOnClickListener {
            music = !music
            val editor = MainActivity.preferences?.edit()
            editor?.putBoolean(MainActivity.MUSIC, music)
            editor?.apply()
            changeMusicImage()
        }

        binding.vibroButton.setOnClickListener {
            vibro = !vibro
            val editor = MainActivity.preferences?.edit()
            editor?.putBoolean(MainActivity.VIBRO, vibro)
            editor?.apply()
            changeVibroImage()
        }

        return binding.root
    }

    private fun changeMusicImage() {
        if (music) {
            binding.musicButton.setImageResource(R.drawable.sound_on)
        } else {
            binding.musicButton.setImageResource(R.drawable.sound_off)
        }
    }

    private fun changeVibroImage() {
        if (vibro) {
            binding.vibroButton.setImageResource(R.drawable.vibro_on)
        } else {
            binding.vibroButton.setImageResource(R.drawable.vobro_off)
        }
    }
}