package com.example.shopping.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shopping.R
import com.example.shopping.data.local.RoomDao
import com.example.shopping.data.model.productsDetails.ProductDatum
import com.example.shopping.databinding.FragmentWishListBinding
import com.example.shopping.ui.adapter.WishListAdapter
import com.example.shopping.ui.viewmodels.WishListViewModel
import com.example.shopping.utilies.Extension.initRecyclerView
import com.example.shopping.utilies.Extension.replaceFragment
import com.example.shopping.utilies.IItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WishListFragment : Fragment(), IItemClickListener, View.OnClickListener {
    private var _binding: FragmentWishListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: WishListViewModel
    private lateinit var adapter: WishListAdapter
    private var productDatum: ProductDatum? = null

    @Inject
    lateinit var roomDao: RoomDao
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishListBinding.inflate(inflater, container, false)
        adapter = WishListAdapter(requireContext(), this)
        initViews()
        viewModel = ViewModelProvider(this).get(WishListViewModel::class.java)
        with(viewModel) {
            getFavProducts().observe(viewLifecycleOwner,  {
                if (it.isEmpty()) {
                    binding.ivEmptyList.visibility = View.VISIBLE
                    binding.tvEmptyListIsEmpty.visibility = View.VISIBLE
                    binding.tvEmptyListExplore.visibility = View.VISIBLE
                    binding.RecWishList.visibility = View.INVISIBLE
                } else {
                    binding.ivEmptyList.visibility = View.GONE
                    binding.tvEmptyListIsEmpty.visibility = View.GONE
                    binding.tvEmptyListExplore.visibility = View.GONE
                    binding.RecWishList.visibility = View.VISIBLE
                    adapter.setFavNewsList(it)
                }


            })
        }

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition
                CoroutineScope(Dispatchers.Main).launch {
                    roomDao.deleteProductFav(adapter.getFavNewsList()[position])
                }

            }
        }).attachToRecyclerView(binding.RecWishList)
        return binding.root
    }

    private fun initViews() {
        binding.toolbar.title = requireActivity().resources.getString(R.string.WishList)
        initRecyclerView(requireContext(), binding.RecWishList, adapter, 1)
        binding.ivWishListDelete.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v!!.id != R.id.iv_WishList_delete) return
        showDialog()
    }

    override fun onItemClickListener(view: View, itemId: Int) {
        productDatum = adapter.getFavNewsList()[itemId]
        replaceFragment(
            ProductDetailsFragment.productDetailsFragment(productDatum),
            R.id.FragmentLoad,
            requireActivity().supportFragmentManager.beginTransaction()
        )
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.wish_list_dailog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val mDialogNo: FloatingActionButton = dialog.findViewById(R.id.frmNo)
        mDialogNo.setOnClickListener {
            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        val mDialogOk: FloatingActionButton = dialog.findViewById(R.id.frmOk)
        mDialogOk.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                roomDao.deleteAll()
            }
            dialog.cancel()
        }

        dialog.show()
    }


}