package com.example.shopping.ui.viewmodels


import androidx.lifecycle.ViewModel
import com.example.shopping.data.model.productsDetails.ProductDatum
import com.example.shopping.data.repository.ProductCategoryRepo
import com.example.shopping.utilies.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProCategoryViewModel @Inject constructor(var repo: ProductCategoryRepo
) : ViewModel() {

    fun getProductList(): SingleLiveEvent<List<ProductDatum>> {
        return repo.getCategoryProducts()
    }



    suspend fun getHomeProductsList(lang:String,categoryId: Int) {
        repo.getProductsListRepo(lang,categoryId)
    }

}