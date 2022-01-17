package com.example.shopping.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopping.data.model.categoriesDetails.CategoryDatum
import com.example.shopping.databinding.CategoryLayoutBinding
import com.example.shopping.utilies.Extension.loadImage

import com.example.shopping.utilies.IItemClickListener
import java.util.*


class CategoryAdapter(val context: Context, private val onClick: IItemClickListener) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private var categoryList: List<CategoryDatum> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setCategoryList(CategoryList: List<CategoryDatum>) {
        categoryList = CategoryList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val li = LayoutInflater.from(context)
        return ViewHolder(
            onClick,
            CategoryLayoutBinding.inflate(li)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentItem = categoryList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class ViewHolder(
        private val mOnItemClickListener: IItemClickListener,
        private val binding: CategoryLayoutBinding
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        var mItemClickListener: IItemClickListener? = null
        fun bind(categories: CategoryDatum) {
            binding.apply {
                ivCategory.loadImage(categories.image, context)
                tvCategory.text = categories.name
                mItemClickListener = mOnItemClickListener
                ivCategory.setOnClickListener(this@ViewHolder)
            }

        }

        override fun onClick(v: View) {
            val item = categoryList[bindingAdapterPosition]
            mItemClickListener?.onItemClickListener(item)
        }
    }

    interface IItemClickListener {
        fun onItemClickListener(item:CategoryDatum)
    }


}
