package com.example.shopping.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.shopping.R
import com.example.shopping.databinding.FragmentAccountBinding
import com.example.shopping.ui.viewmodels.LoginViewModel
import com.example.shopping.utilies.Extension.replaceFragment
import com.example.shopping.utilies.Extension.showSnake
import com.example.shopping.utilies.baseClasses.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AccountFragment : BaseFragment<FragmentAccountBinding>(FragmentAccountBinding::inflate),
    View.OnClickListener {
    private var pref: SharedPreferences? = null
    private var edt: SharedPreferences.Editor? = null
    private var viewModel: LoginViewModel? = null
    override fun FragmentAccountBinding.initialize() {
        initViews()
        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        viewModel!!.getDatum().observe(requireActivity(), {
            if (it.status) {
                loginSuccess(it.data.token)
            } else {
                showSnake(requireView(), it.message)
            }
        })
    }

    private fun initViews() {
        pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        edt = pref!!.edit()
        binding.btnLogin.setOnClickListener(this)
        binding.tvSignUp.setOnClickListener(this)
        binding.tvForgetPassword.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            (R.id.btn_login) -> {
                userLogin()
            }
            (R.id.tv_SignUp) -> {
                val signUpFragment = SignUpFragment()
                replaceFragment(
                    signUpFragment,
                    R.id.FragmentLoad,
                    requireActivity().supportFragmentManager.beginTransaction()
                )
            }
            (R.id.tv_forgetPassword) -> {
                val changePasswordFragment = ChangePasswordFragment()
                replaceFragment(
                    changePasswordFragment,
                    R.id.FragmentLoad,
                    requireActivity().supportFragmentManager.beginTransaction()
                )
            }
        }
    }

    private fun userLogin() {
        val email: String =
            Objects.requireNonNull(binding.etLoginEmail.text).toString().trim { it <= ' ' }
        val password: String =
            Objects.requireNonNull(binding.etLoginPassword.text).toString().trim { it <= ' ' }
        viewModel!!.getLoginDetails(email, password)
    }

    private fun loginSuccess(token: String) {
        edt!!.putString("token", token)
        edt!!.putBoolean("isLogin", true)
        edt!!.apply()
        val homeFragment = MainFragment()
        replaceFragment(
            homeFragment,
            R.id.FragmentLoad,
            requireActivity().supportFragmentManager.beginTransaction()
        )
    }


}