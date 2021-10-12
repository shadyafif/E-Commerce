package com.example.shopping.utilies

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shopping.R
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.*


object Extension {
    fun replaceFragment(fragment: Fragment?, id: Int, fragmentTransaction: FragmentTransaction) {
        fragmentTransaction.replace(id, fragment!!)
        fragmentTransaction.addToBackStack(fragment.javaClass.name)
        fragmentTransaction.commit()

    }

    fun addFragment(fragment: Fragment?, id: Int,addToBackStack:Boolean, fragmentTransaction: FragmentTransaction) {
        fragmentTransaction.add(id, fragment!!)
        if (addToBackStack)
            fragmentTransaction.addToBackStack(fragment.javaClass.name)
        fragmentTransaction.commit()
    }




    fun View.visible(isVisible: Boolean) {
        visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun showSnake(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }


    fun ImageView.loadImage(url: String, context: Context) {
        Glide.with(context)
            .load(url)
            .apply(RequestOptions().placeholder(R.drawable.progress_animation))
            .into(this)
    }

    fun initRecyclerView(
        context: Context?,
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>?,
        column: Int
    ) {
        val layoutManager = GridLayoutManager(context, column)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    fun convertToRequestBody(part: String?): RequestBody {
        return RequestBody.create(MediaType.parse("multipart/form-data"), part)
    }

    @SuppressLint("ObsoleteSdkInt")
    fun setLang(context:Context,language: String) {
        val config = context.resources.configuration
        val locale = Locale(language)
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            config.setLocale(locale)
        else
            config.setLocale(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            context.createConfigurationContext(config)
        context.resources.updateConfiguration(config,context.resources.displayMetrics)
    }


}