package com.example.shopping.ui.fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.shopping.R
import com.example.shopping.data.local.RoomDao
import com.example.shopping.data.model.cartDetails.CartDatum
import com.example.shopping.databinding.FragmentCartBinding
import com.example.shopping.ui.adapter.CartListAdapter
import com.example.shopping.ui.viewmodels.CartViewModel
import com.example.shopping.utilies.CartInterface
import com.example.shopping.utilies.Extension.init
import com.example.shopping.utilies.Extension.replaceFragment
import com.example.shopping.utilies.baseClasses.BaseFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>(FragmentCartBinding::inflate), CartInterface,
    View.OnClickListener {
    private lateinit var adapter: CartListAdapter
    private lateinit var viewModel: CartViewModel
    private var billTotal = 0

    @Inject
    lateinit var roomDao: RoomDao
    override fun FragmentCartBinding.initialize() {
        initViews()
        viewModel = ViewModelProvider(requireActivity())[CartViewModel::class.java]
        viewModel.getCartProducts().observe(requireActivity(), {
            adapter.setCartList(it)
            if (adapter.itemCount == 0) {
                binding.Constraint.visibility = View.GONE
                binding.ivEmptyCart.visibility = View.VISIBLE
                binding.tvEmptyCart.visibility = View.VISIBLE
            } else {
                binding.Constraint.visibility = View.VISIBLE
                binding.ivEmptyCart.visibility = View.GONE
                binding.tvEmptyCart.visibility = View.GONE
            }
        })

    }

    private fun initViews() {
        binding.toolbar.title = requireActivity().resources.getString(R.string.MyCart)
        adapter = CartListAdapter(requireContext(), this, roomDao)
        binding.RecCart.init(requireContext(), adapter, 1)
        binding.ivCartListDelete.setOnClickListener(this)
        binding.btnViewResults.setOnClickListener(this)
        CoroutineScope(Dispatchers.IO).launch {
            billTotal = roomDao.getSum()
            binding.tvBillTotal.text = billTotal.toString()
            binding.tvSubTotal.text = billTotal.toString()
        }

    }

    private fun setValue(value: Int) {
        binding.tvBillTotal.text = value.toString()
        binding.tvSubTotal.text = value.toString()
    }


    override fun onCartListener(
        view: View?,
        textQuantity: TextView?,
        textPrice: TextView?,
        Quantity: Int,
        itemId: Int
    ) {
        val cartData: CartDatum = adapter.getCartList()[itemId]
        val price: Int = cartData.price!!.toInt()
        when (requireView().id) {
            R.id.iv_Cart_Delete -> {
                CoroutineScope(Dispatchers.IO).launch {
                    roomDao.deleteCartProduct(cartData)
                }
                setValue(binding.tvBillTotal.text.toString().toInt() - Quantity * price)
            }
            R.id.iv_Cart_add -> {
                val Quan: Int = Quantity + 1
                textQuantity!!.text = Quan.toString()
                textPrice!!.text = (price * Quan).toString()
                cartData.quantity = textQuantity.text.toString().toInt()
                CoroutineScope(Dispatchers.IO).launch {
                    roomDao.updateQty(cartData)
                    billTotal = roomDao.getSum()

                }
                setValue(binding.tvBillTotal.text.toString().toInt() + price)
            }
            R.id.iv_Cart_subtract -> {
                if (Quantity == 1) {
                    textQuantity!!.text = "1"
                    textPrice!!.text = (price * Quantity).toString()
                    cartData.quantity = textQuantity.text.toString().toInt()
                    CoroutineScope(Dispatchers.IO).launch {
                        roomDao.updateQty(cartData)

                    }
                } else {
                    val quantity: Int = Quantity - 1
                    textQuantity!!.text = quantity.toString()
                    textPrice!!.text = (price * quantity).toString()
                    cartData.quantity = textQuantity.text.toString().toInt()
                    CoroutineScope(Dispatchers.IO).launch {
                        roomDao.updateQty(cartData)
                        billTotal = roomDao.getSum()

                    }
                    setValue(binding.tvBillTotal.text.toString().toInt() - price)
                }


            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_CartList_delete -> {
                showDialog()
            }
            R.id.btn_view_results -> {
                val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
                val login = pref.getBoolean("isLogin", false)
                if (login) {
                    val total = binding.tvBillTotal.text.toString().trim()
                    replaceFragment(
                        BillingFragment.billingFragment(total),
                        R.id.FragmentLoad,
                        requireActivity().supportFragmentManager.beginTransaction()
                    )
                } else {
                    val message = requireActivity().resources.getString(R.string.loginRequired)
                    val snackBar: Snackbar = Snackbar.make(
                        requireView(),
                        message, Snackbar.LENGTH_LONG
                    ).setAction(R.string.login) {
                        val accountFragment = AccountFragment()
                        replaceFragment(
                            accountFragment,
                            R.id.FragmentLoad,
                            requireActivity().supportFragmentManager.beginTransaction()
                        )
                    }

                    snackBar.show()
                }
            }
        }
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.deletecart)
        Objects.requireNonNull(dialog.window)
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val mDialogNo: FloatingActionButton = dialog.findViewById(R.id.frmNo)
        mDialogNo.setOnClickListener {
            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }


        val mDialogOk: FloatingActionButton = dialog.findViewById(R.id.frmOk)
        mDialogOk.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                roomDao.deleteAllCart()
            }
            dialog.cancel()
        }

        dialog.show()


    }
}

