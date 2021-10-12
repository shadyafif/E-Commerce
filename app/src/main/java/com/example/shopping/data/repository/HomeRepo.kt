package com.example.shopping.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.shopping.data.model.bannerDetails.BannerDatum
import com.example.shopping.data.model.productsDetails.ProductDatum
import com.example.shopping.data.network.IRetrofitApi
import com.example.shopping.utilies.SingleLiveEvent
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepo @Inject constructor(private var api: IRetrofitApi) {

    var bannersList: MutableStateFlow<List<BannerDatum>> = MutableStateFlow(emptyList())
    var datumList: List<BannerDatum>? = null
    var productsList: MutableLiveData<List<ProductDatum>> = SingleLiveEvent()
    var productLst: List<ProductDatum>? = null


    fun getBanners(): MutableStateFlow<List<BannerDatum>> {
        return bannersList
    }

    suspend fun getBannersListRepo() {
        val response = api.getBanners().data
        datumList = response
        bannersList.emit(datumList!!)
    }

    fun getProducts(): MutableLiveData<List<ProductDatum>> {
        return productsList
    }

    suspend fun getProductsListRepo(lang:String) {
        val response = api.getProducts(lang).data
        productLst = response.data
        productsList.postValue(productLst)
    }
}