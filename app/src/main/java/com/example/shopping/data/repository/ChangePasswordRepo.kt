package com.example.shopping.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.shopping.data.model.changePasword.ChangePasswordModel
import com.example.shopping.data.network.IRetrofitApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChangePasswordRepo @Inject constructor(private var api: IRetrofitApi) {

    var changeModel: MutableLiveData<ChangePasswordModel> = MutableLiveData()
    var changePasswordModel: ChangePasswordModel? = null

    fun getChangePassword(): MutableLiveData<ChangePasswordModel> {
        return changeModel
    }

    suspend fun getPasswordChanged(
        authorization: String,
        current_password: String,
        new_password: String
    ) {
        changePasswordModel = api.changePassword(authorization, current_password, new_password)
        changeModel.postValue(changePasswordModel)
    }


}