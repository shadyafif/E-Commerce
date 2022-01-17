package com.example.shopping.ui.fragments


import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.shopping.R
import com.example.shopping.data.model.categoriesDetails.CategoryDatum
import com.example.shopping.databinding.FragmentCategoryBinding
import com.example.shopping.ui.adapter.CategoryAdapter
import com.example.shopping.ui.fragments.CategoryProductFragment.Companion.categoryProductFragment
import com.example.shopping.ui.viewmodels.CategoryViewModel
import com.example.shopping.utilies.Extension.init
import com.example.shopping.utilies.Extension.replaceFragment
import com.example.shopping.utilies.baseClasses.BaseFragment
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class CategoryFragment : BaseFragment<FragmentCategoryBinding>(FragmentCategoryBinding::inflate),
    CategoryAdapter.IItemClickListener {
    private var adapter: CategoryAdapter? = null
    private var categoryDatum: CategoryDatum? = null

    override fun FragmentCategoryBinding.initialize() {

        initViews()
        val viewModel = ViewModelProvider(requireActivity()).get(CategoryViewModel::class.java)
        val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val lang = pref.getString("appLanguage", "en")
        viewModel.getAllCategories(lang!!)
        viewModel.getDatumList().observe(requireActivity(), {
            binding.pbCategory.visibility = View.GONE
            adapter?.setCategoryList(it)

        })


    }

    private fun initViews() {
        adapter = CategoryAdapter(requireActivity(), this)
        binding.pbCategory.visibility = View.VISIBLE
        binding.RecCategories.init(requireContext(), adapter, 1)
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