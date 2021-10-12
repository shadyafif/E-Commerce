package com.example.shopping.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.shopping.data.model.userDetails.UserModel
import com.example.shopping.data.network.IRetrofitApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepo @Inject constructor(private var api: IRetrofitApi) {
    private var loginData: MutableLiveData<UserModel> = MutableLiveData()
    var loginDetails: UserModel? = null

    fun getLogin(): MutableLiveData<UserModel> {
        return loginData
    }

    suspend fun getLoginRepo(email: String, password: String) {
        loginDetails = api.getLogin(email, password)
        loginData.value = loginDetails
    }


}