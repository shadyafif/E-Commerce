package com.example.shopping.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shopping.R
import com.example.shopping.databinding.FragmentInfoBinding
import com.example.shopping.ui.activities.MainActivity
import com.example.shopping.utilies.ActionBottom
import com.example.shopping.utilies.Extension.replaceFragment
import com.example.shopping.utilies.Extension.setLang
import com.example.shopping.utilies.dialogItemClick


class InfoFragment : Fragment(), View.OnClickListener, dialogItemClick {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!
    private var pref: SharedPreferences? = null
    private var edt: SharedPreferences.Editor? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        binding.txtInfoUpdateAccount.setOnClickListener(this)
        binding.txtInfoWishLish.setOnClickListener(this)
        binding.txtInfoLanguage.setOnClickListener(this)
        binding.txtInfoSettings.setOnClickListener(this)
        binding.txtInfoShare.setOnClickListener(this)
        binding.txtInfoFaqs.setOnClickListener(this)
        binding.txtLogOut.setOnClickListener(this)
        return binding.root
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.txt_Info_UpdateAccount -> {

            }
            R.id.txt_Info_WishLish -> {
                val wishListFragment = WishListFragment()
                replaceFragment(
                    wishListFragment, R.id.FragmentLoad,
                    requireActivity().supportFragmentManager.beginTransaction()
                )

            }
            R.id.txt_Info_Language -> {
                val customBottomSheetDialogFragment = ActionBottom.newInstance(this)
                customBottomSheetDialogFragment.show(
                    requireActivity().supportFragmentManager,
                    ActionBottom.Tag
                )
            }
            R.id.txt_Info_Settings -> {
                val settingsFragment = SettingsFragment()
                replaceFragment(
                    settingsFragment, R.id.FragmentLoad,
                    requireActivity().supportFragmentManager.beginTransaction()
                )
            }
            R.id.txt_Info_Share -> {

            }
            R.id.txt_Info_Faqs -> {
                val faqsFragment = FAQsFragment()
                replaceFragment(
                    faqsFragment, R.id.FragmentLoad,
                    requireActivity().supportFragmentManager.beginTransaction()
                )


            }
            R.id.txt_log_out -> {

            }

        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onItemClickListener(view: View) {
        pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        edt = pref!!.edit()
        val lang = pref!!.getString("appLanguage", "en")
        if (view.id == R.id.btn_Change_language) {
            if (lang == "ar") {
                edt!!.putString("appLanguage", "en")
                edt!!.apply()
                setLang(requireContext(), "en")
            } else {
                edt!!.putString("appLanguage", "ar")
                edt!!.apply()
                setLang(requireContext(), "ar")
            }
            startActivity(Intent(requireContext(), MainActivity::class.java))

        }
    }


}