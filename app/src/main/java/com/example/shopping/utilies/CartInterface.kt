package com.example.shopping.utilies

import android.view.View
import android.widget.TextView

interface CartInterface {
    fun onCartListener(
        view: View?,
        textQuantity: TextView?,
        textPrice: TextView?,
        Quantity: Int,
        itemId: Int
    )
}