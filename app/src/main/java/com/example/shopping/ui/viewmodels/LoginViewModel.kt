package com.example.shopping.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopping.data.model.userDetails.UserModel
import com.example.shopping.data.repository.LoginRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repo: LoginRepo) : ViewModel() {

    fun getDatum(): MutableLiveData<UserModel> {
        return repo.getLogin()
    }

    suspend fun getLoginDetails(email: String, password: String) {
        repo.getLoginRepo(email, password)
    }
}