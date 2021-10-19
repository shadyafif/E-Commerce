package com.example.shopping.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.Window
import androidx.lifecycle.ViewModelProvider
import com.example.shopping.R
import com.example.shopping.data.local.RoomDao
import com.example.shopping.databinding.FragmentBillingBinding
import com.example.shopping.ui.viewmodels.ProfileViewModel
import com.example.shopping.utilies.Extension.replaceFragment
import com.example.shopping.utilies.baseClasses.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BillingFragment : BaseFragment<FragmentBillingBinding>(FragmentBillingBinding::inflate),
    View.OnClickListener {
    private var totalCost: String? = null
    private var viewModel: ProfileViewModel? = null

    @Inject
    lateinit var roomDao: RoomDao

    companion object {
        fun billingFragment(total: String): BillingFragment {
            val billingFragment = BillingFragment()
            val bundle = Bundle()
            bundle.putString("total", total)
            billingFragment.arguments = bundle
            return billingFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assert(arguments != null)
        totalCost = arguments?.getString("total")
    }

    override fun FragmentBillingBinding.initialize() {
        initView()
        viewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val token = pref.getString("token", "")
        CoroutineScope(Dispatchers.IO).launch {
            viewModel!!.getProfileDetails(token!!)
        }
        viewModel!!.getDatum().observe(requireActivity(), {
            binding.etBillingName.text = it.data.name
            binding.etBillingEmail.text = it.data.email
            binding.etBillingPhone.text = it.data.phone
        })
    }

    private fun initView() {
        binding.tvBillingTotal.text = totalCost
        binding.btnBillingComplete.setOnClickListener(this)
        binding.rbBillingDelivery.setOnClickListener(this)
        binding.rbBillingVisa.setOnClickListener(this)
    }

    @SuppressLint("ResourceType", "UseCompatLoadingForDrawables")
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rb_billing_delivery -> {
                binding.ivTwo.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.payment_active,
                        null
                    )
                )
                binding.btnBillingComplete.isEnabled = true
                binding.rbBillingVisa.isChecked = false
                binding.rbBillingDelivery.isChecked = true
                binding.Constraint.visibility = View.GONE
                binding.tvCashTitle.visibility = View.VISIBLE
                binding.tvVisaTitle.visibility = View.GONE
            }

            R.id.rb_billing_Visa -> {
                binding.ivTwo.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.payment_active,
                        null
                    )
                )
                binding.btnBillingComplete.isEnabled = true
                binding.rbBillingDelivery.isChecked = false
                binding.rbBillingVisa.isChecked = true
                binding.Constraint.visibility = View.VISIBLE
                binding.tvCashTitle.visibility = View.GONE
                binding.tvVisaTitle.visibility = View.VISIBLE
            }

            R.id.btn_Billing_Complete -> {
                binding.ivTwo.setImageDrawable(resources.getDrawable(R.drawable.done_circle, null))
                val dialog = Dialog(requireContext())
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.ckeckout_layout)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
                object : CountDownTimer(5000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        dialog.dismiss()
                        CoroutineScope(Dispatchers.IO).launch {
                            roomDao.deleteAllCart()
                        }
                        val cartFragment = CartFragment()
                        replaceFragment(
                            cartFragment,
                            R.id.FragmentLoad,
                            requireActivity().supportFragmentManager.beginTransaction()
                        )
                    }
                }.start()
            }
        }
    }


}