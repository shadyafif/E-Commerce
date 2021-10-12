package com.example.shopping.ui.adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.shopping.R

class SlidingAdapter(private val context: Context, private var urls: List<String>) :
    PagerAdapter() {
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return urls.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val li = LayoutInflater.from(context)
        val imageLayout = li.inflate(R.layout.slidingimages_layout, view, false)!!
        val imageView = imageLayout
            .findViewById<View>(R.id.image) as ImageView
        Glide.with(context)
            .load(urls[position])
            .into(imageView)
        view.addView(imageLayout, 0)
        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}
    override fun saveState(): Parcelable? {
        return null
    }
}