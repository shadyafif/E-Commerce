package com.example.shopping.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.shopping.data.local.RoomDao
import com.example.shopping.data.model.cartDetails.CartDatum
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(val room: RoomDao, application: Application) :
    AndroidViewModel(
        application
    ) {
    var datumList: LiveData<List<CartDatum>>? = null
    fun getCartProducts(): LiveData<List<CartDatum>> {
        datumList = room.loadCartProducts()
        return datumList!!
    }
}