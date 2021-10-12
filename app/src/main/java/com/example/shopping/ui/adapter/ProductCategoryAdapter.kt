package com.example.shopping.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopping.R
import com.example.shopping.data.local.RoomDao
import com.example.shopping.data.model.productsDetails.ProductDatum
import com.example.shopping.databinding.ProductCategoryLayoutBinding
import com.example.shopping.utilies.Extension.loadImage
import com.example.shopping.utilies.IItemClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.ArrayList

import javax.inject.Inject

class ProductCategoryAdapter @Inject constructor(
    var roomDao: RoomDao,
    val context: Context,
    private val onClick: IItemClickListener
) : RecyclerView.Adapter<ProductCategoryAdapter.ViewHolder>() {
    private  var productsList: List<ProductDatum> = ArrayList()

    fun getProductList(): List<ProductDatum?> {
        return productsList
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setProductList(ProductList: List<ProductDatum>) {
        productsList = ProductList
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductCategoryAdapter.ViewHolder {
        val li = LayoutInflater.from(context)
        return ViewHolder(
            ProductCategoryLayoutBinding.inflate(li), onClick
        )
    }

    override fun onBindViewHolder(holder: ProductCategoryAdapter.ViewHolder, position: Int) {
        var currentItem = productsList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    inner class ViewHolder(
        private val binding: ProductCategoryLayoutBinding,
        private val mOnItemClickListener: IItemClickListener
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        var mItemClickListener: IItemClickListener? = null

        @SuppressLint("ResourceType")
        fun bind(products: ProductDatum) {
            binding.apply {
                binding.txtCategoryProductName.text = products.name
                binding.txtCategoryProductPrice.text = products.old_price.toString()
                if (products.images!!.isEmpty()) {
                    binding.imgCategoryProduct.loadImage(
                        context.resources.getString(R.drawable.nophoto),
                        context
                    )
                } else {
                    binding.imgCategoryProduct.loadImage(products.image!!, context)
                }
                if (products.discount != 0) {
                    binding.tvDiscountPercent.text = products.discount.toString() + "%"
                    binding.txtCategoryProductRegularPrice.text = products.price.toString()
                    binding.txtCategoryProductPrice.paintFlags =
                        binding.txtCategoryProductPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    binding.tvDiscountPercent.visibility = View.INVISIBLE
                    binding.txtCategoryProductRegularPrice.visibility = View.INVISIBLE
                }
                mItemClickListener = mOnItemClickListener
                binding.imgCategoryProduct.setOnClickListener(this@ViewHolder)
                binding.imgCategoryCart.setOnClickListener(this@ViewHolder)
                binding.chkCategoryFavorite.setOnClickListener(this@ViewHolder)
                GlobalScope.launch(Dispatchers.IO)
                {
                    val product = roomDao.fetchById(products.name)
                    binding.chkCategoryFavorite.isChecked = product != null
                }
            }


        }

        override fun onClick(v: View?) {
            mItemClickListener?.onItemClickListener(v!!, layoutPosition)
        }

    }
}