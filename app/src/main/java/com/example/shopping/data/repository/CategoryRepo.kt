package com.example.shopping.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.shopping.data.model.categoriesDetails.CategoryDatum
import com.example.shopping.data.network.IRetrofitApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepo @Inject constructor(private var api: IRetrofitApi) {
    private var categoriesList: MutableLiveData<List<CategoryDatum>> =
        MutableLiveData<List<CategoryDatum>>()
    var datumList: List<CategoryDatum>? = null


    fun getCategories(): MutableLiveData<List<CategoryDatum>> {
        return categoriesList
    }

    suspend fun getCategoriesListRepo(lang:String) {
        val response = api.getCategories(lang).data
        datumList = response.data
        categoriesList.postValue(datumList)
    }
}