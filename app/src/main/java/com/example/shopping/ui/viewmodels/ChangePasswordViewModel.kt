package com.example.shopping.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopping.data.model.changePasword.ChangePasswordModel
import com.example.shopping.data.repository.ChangePasswordRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(private val repo: ChangePasswordRepo) :
    ViewModel() {

    fun getDatum(): MutableLiveData<ChangePasswordModel> {
        return repo.getChangePassword()
    }

     fun getPassChanged(
        authorization: String,
        current_password: String,
        new_password: String
    ) {
        viewModelScope.launch {
            repo.getPasswordChanged(authorization, current_password, new_password)
        }

    }
}