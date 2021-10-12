package com.example.shopping.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.shopping.data.model.registerDetails.RegisterModel
import com.example.shopping.data.network.IRetrofitApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterRepo @Inject constructor(private var api: IRetrofitApi) {
    private var registerData: MutableLiveData<RegisterModel> = MutableLiveData()
    var registerDetails: RegisterModel? = null

    fun getRegister(): MutableLiveData<RegisterModel> {
        return registerData
    }

    suspend fun userRegister(
        name: RequestBody,
        phone: RequestBody,
        email: RequestBody,
        password: RequestBody,
        image: MultipartBody.Part
    ) {
        registerDetails = api.userRegister(name, phone, email, password, image)
        registerData.postValue(registerDetails)
    }
}