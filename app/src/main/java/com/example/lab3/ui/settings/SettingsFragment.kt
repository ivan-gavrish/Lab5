package com.example.lab3.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lab3.BuildConfig
import com.example.lab3.R
import com.example.lab3.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = "Settings"
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        sharedPref = requireActivity().getSharedPreferences("colormatcher_settings", Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchHideButton.isChecked = sharedPref.getBoolean("hideButton", false)

        binding.switchHideButton.setOnClickListener {
            if (binding.switchHideButton.isChecked) {
                sharedPref.edit().putBoolean("hideButton", true).apply()
            } else {
                sharedPref.edit().putBoolean("hideButton", false).apply()
            }
        }

        binding.tvVersionNumber.text = getString(R.string.version_number, BuildConfig.VERSION_CODE)
    }
}