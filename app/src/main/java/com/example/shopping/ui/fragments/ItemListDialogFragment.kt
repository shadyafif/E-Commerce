package com.example.shopping.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shopping.databinding.FragmentItemListDialogListDialogBinding
import com.example.shopping.utilies.dialogItemClick
import com.google.android.material.bottomsheet.BottomSheetDialogFragment




class ItemListDialogFragment(var dialogItemClick: dialogItemClick) : BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: FragmentItemListDialogListDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentItemListDialogListDialogBinding.inflate(inflater, container, false)
        binding.btnChangeLanguage.setOnClickListener(this)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        dialogItemClick.onItemClickListener(v!!)
        dismiss()
    }
}