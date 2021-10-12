package com.example.shopping.data.repository


import com.example.shopping.data.model.productsDetails.ProductDatum
import com.example.shopping.data.network.IRetrofitApi
import com.example.shopping.utilies.SingleLiveEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductCategoryRepo @Inject constructor(private var api: IRetrofitApi) {
    private var productsList: SingleLiveEvent<List<ProductDatum>> = SingleLiveEvent()

    private var productLst: List<ProductDatum>? = null


    fun getCategoryProducts(): SingleLiveEvent<List<ProductDatum>> {
        return productsList

    }


    suspend fun getProductsListRepo(lang:String,category_id: Int) {
        productLst = api.getProductsCategory(lang,category_id).data.data
        productsList.postValue(productLst!!)


    }


}