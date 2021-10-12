package com.example.shopping.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shopping.R
import com.example.shopping.data.local.RoomDao
import com.example.shopping.databinding.FragmentBillingBinding
import com.example.shopping.ui.viewmodels.ProfileViewModel
import com.example.shopping.utilies.Extension.replaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class BillingFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentBillingBinding? = null
    private val binding get() = _binding!!
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBillingBinding.inflate(inflater, container, false)
        initView()
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
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

        return binding.root
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
                binding.ivTwo.setImageDrawable(resources.getDrawable(R.drawable.payment_active,null))
                binding.btnBillingComplete.isEnabled = true
                binding.rbBillingVisa.isChecked = false
                binding.rbBillingDelivery.isChecked = true
                binding.Constraint.visibility = View.GONE
                binding.tvCashTitle.visibility = View.VISIBLE
                binding.tvVisaTitle.visibility = View.GONE
            }

            R.id.rb_billing_Visa -> {
                binding.ivTwo.setImageDrawable(resources.getDrawable(R.drawable.payment_active,null))
                binding.btnBillingComplete.isEnabled = true
                binding.rbBillingDelivery.isChecked = false
                binding.rbBillingVisa.isChecked = true
                binding.Constraint.visibility = View.VISIBLE
                binding.tvCashTitle.visibility = View.GONE
                binding.tvVisaTitle.visibility = View.VISIBLE
            }

            R.id.btn_Billing_Complete -> {
                binding.ivTwo.setImageDrawable(resources.getDrawable(R.drawable.done_circle,null))
                val dialog = Dialog(requireContext())
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.ckeckout_layout)
                Objects.requireNonNull(dialog.window)
                    ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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