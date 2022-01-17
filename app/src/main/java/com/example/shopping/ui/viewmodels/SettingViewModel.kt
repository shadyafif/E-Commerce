package com.example.shopping.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopping.data.model.setting.SettingModel
import com.example.shopping.data.repository.SettingsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val repo: SettingsRepo) : ViewModel() {

    fun getDatumList(): MutableLiveData<SettingModel> {
        return repo.getSettings()
    }

    fun getAllSettings(lang: String) {
        viewModelScope.launch {
            repo.getSettingsRepo(lang)
        }
    }
}