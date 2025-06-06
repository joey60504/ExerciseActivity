package com.example.exerciseactivity.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.exerciseactivity.databinding.ActivityMainBinding
import com.example.exerciseactivity.ui.BaseActivity
import com.example.exerciseactivity.ui.homepage.HomepageActivity
import kotlin.math.max

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setUpStatusBarDisplay()
        setUpListener()
    }

    private fun setUpListener() {
        binding.txtClickEnter.setOnClickListener {
            startActivity(Intent(this@MainActivity, HomepageActivity::class.java))
        }
    }
    @SuppressLint("SetTextI18n")
    private fun setUpStatusBarDisplay() {
        //StatusBar
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                max(ime.bottom, systemBars.bottom)
            )
            insets
        }
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
    }
}