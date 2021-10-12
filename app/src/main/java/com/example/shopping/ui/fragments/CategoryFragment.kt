package com.example.shopping.ui.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shopping.R
import com.example.shopping.data.model.categoriesDetails.CategoryDatum
import com.example.shopping.databinding.FragmentCategoryBinding
import com.example.shopping.ui.adapter.CategoryAdapter
import com.example.shopping.ui.fragments.CategoryProductFragment.Companion.categoryProductFragment
import com.example.shopping.ui.viewmodels.CategoryViewModel
import com.example.shopping.utilies.Extension.initRecyclerView
import com.example.shopping.utilies.Extension.replaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CategoryFragment : Fragment(), CategoryAdapter.IItemClickListener {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private var adapter: CategoryAdapter? = null
    private var categoryDatum: CategoryDatum? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        initViews()
        val viewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val lang = pref.getString("appLanguage", "en")

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getAllCategories(lang!!)
        }

        viewModel.getDatumList().observe(requireActivity(), {
            binding.pbCategory.visibility = View.GONE
            adapter?.setCategoryList(it)

        })

        return binding.root
    }

    private fun initViews() {
        adapter = CategoryAdapter(requireActivity(), this)
        binding.pbCategory.visibility = View.VISIBLE
        initRecyclerView(requireContext(), binding.RecCategories, adapter, 1)
    }

    override fun onItemClickListener(item: CategoryDatum) {
        this.categoryDatum = item
        replaceFragment(
            categoryProductFragment(item),
            R.id.FragmentLoad,
            requireActivity().supportFragmentManager.beginTransaction()
        )


    }


}