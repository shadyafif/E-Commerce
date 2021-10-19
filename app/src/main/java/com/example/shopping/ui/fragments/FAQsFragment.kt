package com.example.shopping.ui.fragments

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.shopping.R
import com.example.shopping.databinding.FragmentFaqsBinding
import com.example.shopping.ui.adapter.FaqsAdapter
import com.example.shopping.ui.viewmodels.FaqsViewModel
import com.example.shopping.utilies.Extension
import com.example.shopping.utilies.baseClasses.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FAQsFragment : BaseFragment<FragmentFaqsBinding>(FragmentFaqsBinding::inflate){

    private var adapter: FaqsAdapter? = null
    private lateinit var  viewModel : FaqsViewModel
    override fun FragmentFaqsBinding.initialize() {
        initViews()
        viewModel = ViewModelProvider(requireActivity()).get(FaqsViewModel::class.java)
        viewModel.getDatumList().observe(requireActivity(), {
            binding.pbFaqs.visibility = View.GONE
            adapter!!.setFaqsList(it)
        })
    }

    private fun initViews() {
        binding.toolbar.title=requireActivity().resources.getString(R.string.faqs)
        adapter = FaqsAdapter(requireActivity())
        binding.pbFaqs.visibility = View.VISIBLE
        Extension.initRecyclerView(requireContext(), binding.RecFaqs, adapter, 1)
    }

    override fun onResume() {
        super.onResume()
        val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val lang = pref.getString("appLanguage", "en")
        CoroutineScope(Dispatchers.IO).launch  {
            viewModel.getAllFaqs(lang!!)
        }
    }
}