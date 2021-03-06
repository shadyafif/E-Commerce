package com.example.shopping.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopping.data.model.registerDetails.RegisterModel
import com.example.shopping.data.repository.RegisterRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private var repo: RegisterRepo) : ViewModel() {

    fun getDatum(): MutableLiveData<RegisterModel> {
        return repo.getRegister()
    }

    fun getRegisterDetails(
        name: RequestBody,
        phone: RequestBody,
        email: RequestBody,
        password: RequestBody,
        image: MultipartBody.Part
    ) {
        viewModelScope.launch {
            repo.userRegister(name, phone, email, password, image)
        }
    }

}