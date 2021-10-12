package com.example.shopping.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopping.data.model.setting.SettingModel
import com.example.shopping.data.repository.SettingsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val repo: SettingsRepo) : ViewModel() {

    fun getDatumList(): MutableLiveData<SettingModel> {
        return repo.getSettings()
    }

    suspend fun getAllSettings(lang:String) {
        repo.getSettingsRepo(lang)
    }
}