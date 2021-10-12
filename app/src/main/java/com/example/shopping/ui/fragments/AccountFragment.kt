package com.example.shopping.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shopping.R
import com.example.shopping.databinding.FragmentAccountBinding
import com.example.shopping.ui.viewmodels.LoginViewModel
import com.example.shopping.utilies.Extension.replaceFragment
import com.example.shopping.utilies.Extension.showSnake
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*

@AndroidEntryPoint
class AccountFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private var pref: SharedPreferences? = null
    private var edt: SharedPreferences.Editor? = null
    private var viewModel: LoginViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        initViews()
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel!!.getDatum().observe(requireActivity(), {
            if (it.status) {
                loginSuccess(it.data.token)
            } else {
                showSnake(requireView(), it.message)
            }
        })

        return binding.root
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

        CoroutineScope(Dispatchers.Main).launch {
            viewModel!!.getLoginDetails(email, password)
        }
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