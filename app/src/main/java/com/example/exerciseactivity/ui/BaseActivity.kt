package com.example.exerciseactivity.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.example.exerciseactivity.R
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity<B : ViewBinding> : DaggerAppCompatActivity() {

    lateinit var binding: B

    abstract fun getViewBinding(): B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        binding = getViewBinding()
        setContentView(binding.root)

        if (findViewById<View>(R.id.uc_nav) != null) {
            val nav = findViewById<View>(R.id.uc_nav)
            val navTitle = nav.findViewById<TextView>(R.id.uc_txt_nav)
            navTitle.text = title.toString()
        }
    }
    open fun getStatusBarHeight(): Int {
        var statusBarHeight = 0
        val resourceId = resources.getIdentifier(
            getString(R.string.status_bar_height),
            getString(R.string.dimen),
            getString(R.string.android)
        )
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

}
