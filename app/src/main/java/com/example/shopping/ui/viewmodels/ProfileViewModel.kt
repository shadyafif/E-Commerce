package com.example.shopping.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopping.data.model.profileDetails.ProfileModel
import com.example.shopping.data.repository.ProfileRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private var repo: ProfileRepo) : ViewModel() {
    fun getDatum(): MutableLiveData<ProfileModel> {
        return repo.getProfile()
    }

    suspend fun getProfileDetails(auth: String) {
        repo.userProfile(auth)
    }
}