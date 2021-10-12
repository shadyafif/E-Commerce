package com.example.shopping.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopping.R
import com.example.shopping.data.local.RoomDao
import com.example.shopping.data.model.cartDetails.CartDatum
import com.example.shopping.databinding.LayoutCartBinding
import com.example.shopping.utilies.CartInterface
import com.example.shopping.utilies.Extension.loadImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class CartListAdapter @Inject constructor(
    val context: Context,
    var cartInterface: CartInterface,
    var roomDao: RoomDao

) :
    RecyclerView.Adapter<CartListAdapter.ViewHolder>() {
    var cartProductsList: List<CartDatum> = ArrayList()
    var totalItemPrice: Int = 0
    var billTotal: Int = 0
    var qty = 0
    var index = 0
    fun getCartList(): List<CartDatum> {
        return cartProductsList
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCartList(CartProductsList: List<CartDatum>) {
        cartProductsList = CartProductsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val li = LayoutInflater.from(context)
        return ViewHolder(
            LayoutCartBinding.inflate(li), cartInterface
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = cartProductsList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return cartProductsList.size
    }

    inner class ViewHolder(private val binding: LayoutCartBinding, var callback: CartInterface) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        var callBack: CartInterface? = null

        @SuppressLint("UseCompatLoadingForDrawables", "ResourceType")
        fun bind(products: CartDatum) {
            binding.apply {
                tvCartTitle.text = products.name
                tvCartPrice.text = (products.price).toString()
                tvQty.text = (products.quantity).toString()
                val url: String? = products.image
                if (url != null) {
                    ivCartProduct.loadImage(url, context)
                } else {
                    ivCartProduct.loadImage(
                        context.resources.getString(R.drawable.nophoto),
                        context
                    )
                }
                // Update total price based on quantity
                qty = tvQty.text.toString().toInt()
                val itemPrice = products.price!!.toInt()
                totalItemPrice = itemPrice * qty
                tvCartPrice.text = totalItemPrice.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    billTotal = roomDao.getSum()
                    callBack!!.setValue(billTotal)
                }
                callBack = callback
                binding.ivCartAdd.setOnClickListener(this@ViewHolder)
                binding.ivCartSubtract.setOnClickListener(this@ViewHolder)
                binding.ivCartDelete.setOnClickListener(this@ViewHolder)
            }

        }

        override fun onClick(v: View?) {
            val quan = binding.tvQty.text.toString().toInt()
            callBack!!.onCartListener(v, binding.tvQty, binding.tvCartPrice, quan, layoutPosition)
        }

    }
}