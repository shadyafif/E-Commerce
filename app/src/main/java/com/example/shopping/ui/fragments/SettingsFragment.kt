package com.example.shopping.ui.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.shopping.data.model.setting.SettingModel
import com.example.shopping.databinding.FragmentSettingsBinding
import com.example.shopping.ui.viewmodels.SettingViewModel
import com.example.shopping.utilies.baseClasses.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {
    private lateinit var viewModel: SettingViewModel
    override fun FragmentSettingsBinding.initialize() {
        viewModel = ViewModelProvider(requireActivity()).get(SettingViewModel::class.java)
        val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val lang = pref.getString("appLanguage", "en")
        viewModel.getAllSettings(lang!!)
        viewModel.getDatumList().observe((requireActivity()), {
            fillData(it)
        })
    }


    private fun fillData(it: SettingModel) {
        binding.tvAbout.text = it.data.about
        binding.tvTerms.text = it.data.terms

    }


}