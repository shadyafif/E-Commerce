package com.example.shopping.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.shopping.data.model.setting.SettingModel
import com.example.shopping.data.network.IRetrofitApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepo @Inject constructor(private var api: IRetrofitApi) {
    var settingsData: MutableLiveData<SettingModel> =
        MutableLiveData<SettingModel>()
    private lateinit var datum: SettingModel

    fun getSettings(): MutableLiveData<SettingModel> {
        return settingsData
    }

    suspend fun getSettingsRepo(lang:String) {
        datum = api.getSetting(lang)
        settingsData.postValue(datum)
    }

}