package com.example.shopping.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopping.data.model.userDetails.UserModel
import com.example.shopping.data.repository.LoginRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repo: LoginRepo) : ViewModel() {

    fun getDatum(): MutableLiveData<UserModel> {
        return repo.getLogin()
    }

    fun getLoginDetails(email: String, password: String) {
        viewModelScope.launch {
            repo.getLoginRepo(email, password)
        }

    }
}