package com.example.shopping.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopping.data.model.bannerDetails.BannerDatum
import com.example.shopping.data.model.productsDetails.ProductDatum
import com.example.shopping.data.repository.HomeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: HomeRepo) : ViewModel() {

    fun getDatumList(): MutableStateFlow<List<BannerDatum>> {
        return repo.getBanners()
    }

     fun getAllBanners() {
         viewModelScope.launch {
             repo.getBannersListRepo()
         }

    }

    fun getDatumProductList(): MutableLiveData<List<ProductDatum>> {
        return repo.getProducts()
    }

     fun getAllProducts(lang:String) {
        viewModelScope.launch {
            repo.getProductsListRepo(lang)
        }
    }
}