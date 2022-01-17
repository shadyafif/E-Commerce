package com.example.shopping.ui.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopping.data.model.productsDetails.ProductDatum
import com.example.shopping.data.repository.ProductCategoryRepo
import com.example.shopping.utilies.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProCategoryViewModel @Inject constructor(
    var repo: ProductCategoryRepo
) : ViewModel() {

    fun getProductList(): SingleLiveEvent<List<ProductDatum>> {
        return repo.getCategoryProducts()
    }


    fun getHomeProductsList(lang: String, categoryId: Int) {
        viewModelScope.launch {
            repo.getProductsListRepo(lang, categoryId)
        }
    }

}