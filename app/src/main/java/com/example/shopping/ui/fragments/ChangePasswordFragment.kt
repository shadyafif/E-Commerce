package com.example.shopping.ui.fragments

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.shopping.R
import com.example.shopping.databinding.FragmentChangePasswordBinding
import com.example.shopping.ui.viewmodels.ChangePasswordViewModel
import com.example.shopping.utilies.Extension.replaceFragment
import com.example.shopping.utilies.Extension.showSnake
import com.example.shopping.utilies.baseClasses.BaseFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangePasswordFragment :BaseFragment<FragmentChangePasswordBinding>(FragmentChangePasswordBinding::inflate) , View.OnClickListener {
    private var viewModel: ChangePasswordViewModel? = null
    override fun FragmentChangePasswordBinding.initialize() {
     initView()
        viewModel = ViewModelProvider(requireActivity()).get(ChangePasswordViewModel::class.java)
        viewModel!!.getDatum().observe(requireActivity(),
            {
                showSnake(requireView(), it.message)
            })
    }

    private fun initView() {
        binding.toolbar.title = requireActivity().resources.getString(R.string.change_Password)
        binding.btnChangePassword.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val login = pref.getBoolean("isLogin", false)
        val token = pref.getString("token", "")
        val currentPassword = binding.etCurrentPassword.text.toString().trim()
        val newPassword = binding.etNewPassword.text.toString().trim()
        showSnake(requireView(), login.toString())
        if (login) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel!!.getPassChanged(token!!, currentPassword, newPassword)
            }

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