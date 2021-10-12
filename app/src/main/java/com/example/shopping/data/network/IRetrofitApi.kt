package com.example.shopping.data.network

import com.example.shopping.data.model.bannerDetails.BannerModel
import com.example.shopping.data.model.categoriesDetails.MainModel
import com.example.shopping.data.model.changePasword.ChangePasswordModel
import com.example.shopping.data.model.faqsDetails.FaqsModel
import com.example.shopping.data.model.productsDetails.ProductModel
import com.example.shopping.data.model.profileDetails.ProfileModel
import com.example.shopping.data.model.registerDetails.RegisterModel
import com.example.shopping.data.model.setting.SettingModel
import com.example.shopping.data.model.userDetails.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface IRetrofitApi {
    //Categories
    @GET("categories")
    suspend fun getCategories(@Header("lang") lang: String): MainModel

    //banners
    @GET("banners")
    suspend fun getBanners(): BannerModel

    //Products
    @GET("products")
    suspend fun getProducts(@Header("lang") lang: String): ProductModel

    @GET("products")
    suspend fun getProductsCategory(
        @Header("lang") lang: String,
        @Query("category_id") category_id: Int
    ): ProductModel


    //settings
    @GET("settings")
    suspend fun getSetting(@Header("lang") lang: String): SettingModel

    //User Details
    @POST("login")
    suspend fun getLogin(
        @Query("email") email: String,
        @Query("password") password: String
    ): UserModel

    @Multipart
    @POST("register")
    suspend fun userRegister(
        @Part("name") name: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part File: MultipartBody.Part
    ): RegisterModel


    @POST("change-password")
    suspend fun changePassword(
        @Header("Authorization") authorization: String,
        @Query("current_password") current_password: String,
        @Query("new_password") new_password: String
    ): ChangePasswordModel

    @GET("profile")
    suspend fun getProfile(
        @Header("Authorization") authorization: String
    ): ProfileModel

    //Faqs Details
    @GET("faqs")
    suspend fun getFaqs(@Header("lang") lang: String): FaqsModel

}