package com.example.shopping.ui.fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.shopping.R
import com.example.shopping.data.local.RoomDao
import com.example.shopping.data.model.cartDetails.CartDatum
import com.example.shopping.data.model.productsDetails.ProductDatum
import com.example.shopping.databinding.FragmentMainBinding
import com.example.shopping.ui.adapter.ProductAdapter
import com.example.shopping.ui.adapter.SlidingAdapter
import com.example.shopping.ui.fragments.ProductDetailsFragment.Companion.productDetailsFragment
import com.example.shopping.ui.viewmodels.HomeViewModel
import com.example.shopping.utilies.Extension.init
import com.example.shopping.utilies.Extension.replaceFragment
import com.example.shopping.utilies.Extension.showSnake
import com.example.shopping.utilies.IItemClickListener
import com.example.shopping.utilies.baseClasses.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate),
    IItemClickListener {
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: ProductAdapter
    private var currentPage = 0
    private var numPages = 0
    private var slidingImageAdapter: SlidingAdapter? = null
    private var urls: MutableList<String> = mutableListOf()
    var update: Runnable? = Runnable { }
    var productDatum: ProductDatum? = null
    private var cartDatum: CartDatum? = null

    @Inject
    lateinit var roomDao: RoomDao
    override fun FragmentMainBinding.initialize() {
        initViews()
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val lang = pref.getString("appLanguage", "en")
            viewModel.getAllProducts(lang!!)
            viewModel.getAllBanners()
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getDatumList().collect {
                urls.clear()
                it.forEach { item ->
                    urls.add(item.image)
                }
                initSlider(urls)
            }
        }


        binding.svNewsSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getDatumProductList().observe(requireActivity(), {
            binding.pbHome.visibility = View.GONE
            binding.svNewsSearch.visibility = View.VISIBLE
            binding.dotsIndicator.visibility = View.VISIBLE
            adapter.setProductList(it)
        })
    }

    private fun initViews() {
        binding.pbHome.visibility = View.VISIBLE
        binding.dotsIndicator.visibility = View.GONE
        adapter = ProductAdapter(roomDao, requireContext(), this)
        binding.RecHomeProduct.init(requireContext(), adapter, 2)

    }



    override fun onPause() {
        super.onPause()
        Handler(Looper.getMainLooper()).removeCallbacks(update!!)
    }

    private fun initSlider(urls: List<String>) {
        slidingImageAdapter = SlidingAdapter(requireContext(), urls)
        binding.pager.adapter = slidingImageAdapter
        binding.dotsIndicator.setViewPager(binding.pager)
        numPages = urls.size
        update = Runnable {
            if (currentPage == numPages) {
                currentPage = 0
            }
            binding.pager.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                Handler(Looper.getMainLooper()).post(update!!)
            }
        }, 5000, 5000)
    }

    override fun onItemClickListener(view: View, itemId: Int) {
        productDatum = adapter.getProductList()[itemId]
        when (view.id) {
            R.id.iv_Category_Product_details -> showDialog()
        }
    }

    private fun showDialog() {

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.home_product_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val mDialogFavorite: CheckBox = dialog.findViewById(R.id.iv_favorite_dialog)
        CoroutineScope(Dispatchers.IO).launch {
            val product = roomDao.fetchById(productDatum!!.name)
            mDialogFavorite.isChecked = product != null
        }
        mDialogFavorite.setOnClickListener {
            if (mDialogFavorite.isChecked) {
                CoroutineScope(Dispatchers.IO).launch {
                    roomDao.insertProductFav(productDatum!!)
                    val message: String = context?.getString(R.string.checkChecked)!!
                    showSnake(requireView(), message)
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    roomDao.deleteProductFav(productDatum!!)
                    val message: String = context?.getString(R.string.checkNotChecked)!!
                    showSnake(requireView(), message)
                }
            }
            dialog.dismiss()
        }

            val mDialogCart: ImageView = dialog.findViewById(R.id.iv_favorite_cart)
            mDialogCart.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    cartDatum = roomDao.fetchByCartName(productDatum!!.name)
                    if (cartDatum != null) {
                        val quantity: Int = cartDatum!!.quantity!!
                        cartDatum!!.quantity = quantity + 1
                        roomDao.updateQty(cartDatum)
                        val cartMessage =
                            requireActivity().resources.getString(R.string.AddedSuccessfully)
                        showSnake(requireView(), cartMessage)
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
                        showSnake(requireView(), cartMessage)
                    }

                    dialog.dismiss()
                }
                dialog.dismiss()
            }
            val mDialogDetails: ImageView = dialog.findViewById(R.id.iv_favorite_details)
            mDialogDetails.setOnClickListener {
                replaceFragment(
                    productDetailsFragment(productDatum),
                    R.id.FragmentLoad,
                    requireActivity().supportFragmentManager.beginTransaction()
                )
                dialog.dismiss()
            }

            dialog.show()
        }

    }
