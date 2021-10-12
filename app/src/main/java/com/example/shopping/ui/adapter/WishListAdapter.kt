package com.example.shopping.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopping.R
import com.example.shopping.data.model.productsDetails.ProductDatum
import com.example.shopping.databinding.LayoutWishListBinding
import com.example.shopping.utilies.Extension.loadImage
import com.example.shopping.utilies.IItemClickListener

class WishListAdapter(val context: Context, private var click: IItemClickListener) :
    RecyclerView.Adapter<WishListAdapter.ViewHolder>() {

    private var productsList: List<ProductDatum> = ArrayList()

    fun getFavNewsList(): List<ProductDatum> {
        return productsList
    }

    fun setFavNewsList(favProductsList: List<ProductDatum>) {
        productsList = favProductsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val li = LayoutInflater.from(context)
        return ViewHolder(
            LayoutWishListBinding.inflate(li), click
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentItem = productsList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    inner class ViewHolder(
        private val binding: LayoutWishListBinding,
        private val mOnItemClickListener: IItemClickListener
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        var mItemClickListener: IItemClickListener? = null

        @SuppressLint("SetTextI18n", "ResourceType")
        fun bind(products: ProductDatum) {
            binding.apply {
                binding.tvWishListTitle.text = products.name
                binding.tvWishListPrice.text = products.old_price.toString()
                if (products.images!!.isEmpty()) {
                    binding.ivWishListFav.loadImage(
                        context.resources.getString(R.drawable.nophoto),
                        context
                    )
                } else {
                    binding.ivWishListFav.loadImage(products.image!!, context)
                }
                mItemClickListener = mOnItemClickListener
                binding.ivCart.setOnClickListener(this@ViewHolder)
                binding.root.setOnClickListener(this@ViewHolder)

            }

        }

        override fun onClick(v: View) {
            mItemClickListener?.onItemClickListener(v, layoutPosition)
        }

    }
}