package com.example.shopping.utilies

import com.example.shopping.ui.fragments.ItemListDialogFragment


class ActionBottom()  {
    companion object
    {
        const val Tag = "dialog "
        fun newInstance(mListener:dialogItemClick) : ItemListDialogFragment
        {
            return ItemListDialogFragment(mListener)
        }

    }
}