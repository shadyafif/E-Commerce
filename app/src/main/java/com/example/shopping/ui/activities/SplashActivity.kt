package com.example.shopping.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.shopping.databinding.ActivitySplashAcivityBinding


class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashAcivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashAcivityBinding.inflate(layoutInflater)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 5000)
        setContentView(binding.root)
    }
}