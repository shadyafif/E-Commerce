package com.example.shopping.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.shopping.R
import com.example.shopping.data.local.RoomDao
import com.example.shopping.data.model.cartDetails.CartDatum
import com.example.shopping.data.model.productsDetails.ProductDatum
import com.example.shopping.databinding.FragmentProductDetailsBinding
import com.example.shopping.ui.adapter.SlidingAdapter
import com.example.shopping.utilies.Extension.showSnake
import com.example.shopping.utilies.baseClasses.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailsFragment :BaseFragment<FragmentProductDetailsBinding>(FragmentProductDetailsBinding::inflate), View.OnClickListener {
    private var productDatum: ProductDatum? = null
    private var orderNum: Int = 1
    private var currentPage = 0
    private var numPages = 0
    private var slidingImageAdapter: SlidingAdapter? = null
    private var urls: MutableList<String> = mutableListOf()
    var update: Runnable? = Runnable { }
    private var cartDatum: CartDatum? = null
    @Inject
    lateinit var roomDao: RoomDao

    companion object {
        fun productDetailsFragment(productDatum: ProductDatum?): ProductDetailsFragment {
            val productDetailsFragment = ProductDetailsFragment()
            val bundle = Bundle()
            bundle.putParcelable("product", productDatum)
            productDetailsFragment.arguments = bundle
            return productDetailsFragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assert(arguments != null)
        productDatum = arguments?.getParcelable("product")
    }

    override fun FragmentProductDetailsBinding.initialize() {
        initView()
    }

    private fun initView() {
        urls.clear()
        productDatum?.images!!.forEach {
            urls.add(it)
        }
        initSlider(urls)
        binding.tvDetailsName.text = productDatum?.name.toString()
        binding.tvDetailsPrice.text = productDatum?.price.toString()
        binding.tvDetailsDescription.text = productDatum?.description
        CoroutineScope(Dispatchers.IO).launch {
            val product = roomDao.fetchById(productDatum?.name)
            binding.ivDetailsFavorite.isChecked = product != null
        }

        binding.ivDetailsFavorite.setOnClickListener(this)
        binding.ivDetailsCart.setOnClickListener(this)
        binding.ivDetailsPlus.setOnClickListener(this)
        binding.ivDetailsMinus.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_Details_Favorite -> {
                if (binding.ivDetailsFavorite.isChecked) {
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
            }
            R.id.iv_Details_Cart -> {
                CoroutineScope(Dispatchers.IO).launch {
                    cartDatum = roomDao.fetchByCartName(productDatum!!.name)
                    if (cartDatum != null) {
                        val quantity: Int = cartDatum!!.quantity!!
                        cartDatum!!.quantity = quantity  +orderNum
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
                            orderNum
                        )
                        roomDao.insertCartProduct(cartData)

                        val cartMessage =
                            requireActivity().resources.getString(R.string.AddedSuccessfully)
                        showSnake(requireView(), cartMessage)
                    }
                }
            }
            R.id.iv_Details_Plus -> {
                orderNum++
                binding.tvDetailsQuantity.text = orderNum.toString()
            }
            R.id.iv_Details_Minus -> {
                if (orderNum == 1)
                    binding.tvDetailsQuantity.text = "1"
                else {
                    orderNum--
                    binding.tvDetailsQuantity.text = orderNum.toString()
                }
            }
        }
    }

    private fun initSlider(urls: List<String>) {
        slidingImageAdapter = SlidingAdapter(requireContext(), urls)
        binding.pager.adapter = slidingImageAdapter
        binding.dotsIndicator.visibility = View.VISIBLE
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
        }, 3000, 3000)
    }

}