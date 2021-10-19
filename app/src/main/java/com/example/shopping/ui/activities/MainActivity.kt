package com.example.shopping.ui.activities

import am.networkconnectivity.NetworkConnectivity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.shopping.R
import com.example.shopping.ui.fragments.*
import com.example.shopping.utilies.CurvedBottomNavigationView
import com.example.shopping.utilies.Extension.addFragment
import com.example.shopping.utilies.Extension.replaceFragment
import com.example.shopping.utilies.Extension.setLang
import com.example.shopping.utilies.Extension.showSnake
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener {
    private var parentView: ConstraintLayout? = null
    private var pref: SharedPreferences? = null
    private var lang: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        val curvedBottomNavigationView: CurvedBottomNavigationView =
            findViewById(R.id.curve_bottom_navigation_view)
        curvedBottomNavigationView.inflateMenu(R.menu.bottom_nav_menu)
        curvedBottomNavigationView.getChildAt(0)
        curvedBottomNavigationView.selectedItemId = R.id.navigation_home
        curvedBottomNavigationView.setOnNavigationItemSelectedListener(this)

        val mainFragment = MainFragment()
        addFragment(
            mainFragment,
            R.id.FragmentLoad,
            false,
            supportFragmentManager.beginTransaction()
        )
        val fab = findViewById<FloatingActionButton>(R.id.fab_button)
        fab.setOnClickListener(this)
        parentView = findViewById(R.id.view_parentView)

        NetworkConnectivity(this).observe(this, { onNetworkConnectionChanged(it) })
    }

    private fun initViews() {
        pref = this.getPreferences(Context.MODE_PRIVATE)
        lang = pref!!.getString("appLanguage", "en")
        if (lang == "en") {
            setLang(this, "en")
        } else {
            setLang(this, "ar")
        }
    }

    private fun onNetworkConnectionChanged(status: NetworkConnectivity.NetworkStatus?) {
        when (status) {
            NetworkConnectivity.NetworkStatus.OnConnected -> {
            }
            NetworkConnectivity.NetworkStatus.OnWaiting -> {
                showSnake(parentView!!, this.getString(R.string.isWaiting))
            }
            NetworkConnectivity.NetworkStatus.OnLost -> {
                showSnake(parentView!!, this.getString(R.string.isLost))
            }
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_category -> {
                val categoryFragment = CategoryFragment()
                replaceFragment(
                    categoryFragment,
                    R.id.FragmentLoad,
                    supportFragmentManager.beginTransaction()
                )

            }
            R.id.navigation_cart -> {
                val cartFragment = CartFragment()
                replaceFragment(
                    cartFragment,
                    R.id.FragmentLoad,
                    supportFragmentManager.beginTransaction()
                )

            }
            R.id.navigation_account -> {
                val accountFragment = AccountFragment()
                replaceFragment(
                    accountFragment,
                    R.id.FragmentLoad,
                    supportFragmentManager.beginTransaction()
                )
            }
            R.id.navigation_info -> {
                val infoFragment = InfoFragment()
                replaceFragment(
                    infoFragment,
                    R.id.FragmentLoad,
                    supportFragmentManager.beginTransaction()
                )
            }

        }

        return true
    }

    override fun onClick(v: View?) {

        val mainFragment = MainFragment()
        replaceFragment(
            mainFragment,
            R.id.FragmentLoad,
            supportFragmentManager.beginTransaction()
        )

    }


}
