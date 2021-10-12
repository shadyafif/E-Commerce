package com.example.shopping.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.shopping.data.local.RoomDao
import com.example.shopping.data.model.productsDetails.ProductDatum
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WishListViewModel @Inject constructor(val room: RoomDao, application: Application) :
    AndroidViewModel(
        application
    ) {

    var datumList: LiveData<List<ProductDatum>>? = null
    fun getFavProducts(): LiveData<List<ProductDatum>> {
        datumList = room.loadProducts()
        return datumList!!
    }

}