package com.example.shopping.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopping.data.model.faqsDetails.FaqsDatum
import com.example.shopping.databinding.FaqsLayoutBinding

class FaqsAdapter(var context: Context) : RecyclerView.Adapter<FaqsAdapter.ViewHolder>() {

    private var faqsList: List<FaqsDatum> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqsAdapter.ViewHolder {
        val li = LayoutInflater.from(context)
        return ViewHolder(FaqsLayoutBinding.inflate(li))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFaqsList(FaqsList:List<FaqsDatum>)
    {
        faqsList=FaqsList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: FaqsAdapter.ViewHolder, position: Int) {
        val currentItem = faqsList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return faqsList.size
    }

    inner class ViewHolder(private val binding: FaqsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(faqs: FaqsDatum) {
            binding.tvFaqsQuestion.text = faqs.question
            binding.tvFaqsAnswer.text = faqs.answer
        }

    }
}