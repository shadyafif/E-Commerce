package com.example.shopping.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shopping.data.model.setting.SettingModel
import com.example.shopping.databinding.FragmentSettingsBinding
import com.example.shopping.ui.viewmodels.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val lang = pref.getString("appLanguage", "en")
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getAllSettings(lang!!)
        }
        viewModel.getDatumList().observe((requireActivity()), {
            fillData(it)
        })
        return binding.root
    }


    private fun fillData(it: SettingModel) {
        binding.tvAbout.text = it.data.about
        binding.tvTerms.text = it.data.terms

    }


}