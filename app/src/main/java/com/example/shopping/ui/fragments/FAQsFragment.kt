package com.example.shopping.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shopping.R
import com.example.shopping.databinding.FragmentFaqsBinding
import com.example.shopping.ui.adapter.FaqsAdapter
import com.example.shopping.ui.viewmodels.FaqsViewModel
import com.example.shopping.utilies.Extension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FAQsFragment : Fragment() {
    private var _binding: FragmentFaqsBinding? = null
    private val binding get() = _binding!!
    private var adapter: FaqsAdapter? = null
    private lateinit var  viewModel : FaqsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaqsBinding.inflate(inflater, container, false)
        initViews()
        viewModel = ViewModelProvider(this).get(FaqsViewModel::class.java)

        viewModel.getDatumList().observe(requireActivity(), {
            binding.pbFaqs.visibility = View.GONE
            adapter!!.setFaqsList(it)
        })
        return binding.root
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