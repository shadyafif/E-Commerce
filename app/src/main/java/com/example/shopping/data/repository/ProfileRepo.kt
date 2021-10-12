package com.example.shopping.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.shopping.data.model.profileDetails.ProfileModel
import com.example.shopping.data.network.IRetrofitApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepo @Inject constructor(private var api: IRetrofitApi) {
    private var profileData: MutableLiveData<ProfileModel> = MutableLiveData()
    private var model: ProfileModel? = null
    fun getProfile(): MutableLiveData<ProfileModel> {
        return profileData
    }

    suspend fun userProfile(auth: String) {
        model = api.getProfile(auth)
        profileData.postValue( model)
    }
}