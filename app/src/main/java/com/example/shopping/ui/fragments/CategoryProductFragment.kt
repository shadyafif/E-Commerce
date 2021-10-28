package com.example.shopping.ui.fragments


import android.content.Context
import android.os.Bundle

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.CheckBox
import androidx.lifecycle.ViewModelProvider
import com.example.shopping.R
import com.example.shopping.data.local.RoomDao
import com.example.shopping.data.model.cartDetails.CartDatum
import com.example.shopping.data.model.categoriesDetails.CategoryDatum
import com.example.shopping.data.model.productsDetails.ProductDatum
import com.example.shopping.databinding.FragmentCategoryProductBinding
import com.example.shopping.ui.adapter.ProductCategoryAdapter
import com.example.shopping.ui.viewmodels.ProCategoryViewModel
import com.example.shopping.utilies.Extension
import com.example.shopping.utilies.Extension.init
import com.example.shopping.utilies.Extension.replaceFragment
import com.example.shopping.utilies.IItemClickListener
import com.example.shopping.utilies.baseClasses.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class CategoryProductFragment :
    BaseFragment<FragmentCategoryProductBinding>(FragmentCategoryProductBinding::inflate),
    IItemClickListener {
    private lateinit var category: CategoryDatum
    private var adapter: ProductCategoryAdapter? = null
    private var productDatum: ProductDatum? = null
    private var cartDatum: CartDatum? = null

    @Inject
    lateinit var roomDao: RoomDao

    companion object {
        fun categoryProductFragment(categoryDatum: CategoryDatum?): CategoryProductFragment {
            val categoryProductFragment = CategoryProductFragment()
            val bundle = Bundle()
            bundle.putParcelable("category", categoryDatum)
            categoryProductFragment.arguments = bundle
            return categoryProductFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assert(arguments != null)
        category = arguments?.getParcelable("category")!!
    }

    override fun FragmentCategoryProductBinding.initialize() {
        initViews()
        val viewModel = ViewModelProvider(requireActivity()).get(ProCategoryViewModel::class.java)
        val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val lang = pref.getString("lang", "en")

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getHomeProductsList(lang!!, category.id)

        }
        viewModel.getProductList().observe(this@CategoryProductFragment, {
            binding.pbProductCategory.visibility = GONE
            adapter!!.setProductList(it)

        })

    }

    private fun initViews() {
        binding.toolbar.title = category.name
        binding.pbProductCategory.visibility = VISIBLE
        adapter = ProductCategoryAdapter(roomDao, requireContext(), this)
        binding.RecProductCategory.init(requireContext(), adapter, 2)
    }


    override fun onItemClickListener(view: View, itemId: Int) {
        productDatum = adapter!!.getProductList()[itemId]
        when (view.id) {
            R.id.chk_Category_Favorite -> {
                val checkBox = view.findViewById<CheckBox>(R.id.chk_Category_Favorite)
                if (checkBox.isChecked) {
                    CoroutineScope(Dispatchers.IO).launch {
                        roomDao.insertProductFav(productDatum!!)
                        val message: String = context?.getString(R.string.checkChecked)!!
                        Extension.showSnake(requireView(), message)
                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        roomDao.deleteProductFav(productDatum!!)
                        val message: String = context?.getString(R.string.checkNotChecked)!!
                        Extension.showSnake(requireView(), message)
                    }
                }


            }
            R.id.img_Category_Product -> {
                replaceFragment(
                    ProductDetailsFragment.productDetailsFragment(productDatum),
                    R.id.FragmentLoad,
                    requireActivity().supportFragmentManager.beginTransaction()
                )
            }
            R.id.img_Category_Cart -> {
                productDatum = adapter!!.getProductList()[itemId]
                CoroutineScope(Dispatchers.IO).launch {
                    cartDatum = roomDao.fetchByCartName(productDatum!!.name)
                    if (cartDatum != null) {
                        val quantity: Int = cartDatum!!.quantity!!
                        cartDatum!!.quantity = quantity + 1
                        roomDao.updateQty(cartDatum)
                        val cartMessage =
                            requireActivity().resources.getString(R.string.AddedSuccessfully)
                        Extension.showSnake(requireView(), cartMessage)
                    } else {
                        val cartData = CartDatum(
                            productDatum!!.id,
                            productDatum!!.description,
                            productDatum!!.discount,
                            productDatum!!.image,
                            productDatum!!.images,
                            productDatum!!.in_cart,
                            productDatum!!.in_favorites,
                            productDatum!!.name,
                            productDatum!!.old_price,
                            productDatum!!.price,
                            1
                        )
                        roomDao.insertCartProduct(cartData)

                        val cartMessage =
                            requireActivity().resources.getString(R.string.AddedSuccessfully)
                        Extension.showSnake(requireView(), cartMessage)
                    }
                }

            }
        }
    }


}