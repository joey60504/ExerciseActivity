package com.example.exerciseactivity.ui.homepage

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.exerciseactivity.R
import com.example.exerciseactivity.databinding.ActivityHomepageBinding
import com.example.exerciseactivity.di.withFactory
import com.example.exerciseactivity.ui.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import kotlin.math.max

class HomepageActivity : BaseActivity<ActivityHomepageBinding>() {

    @Inject
    lateinit var viewModelFactory: HomepageViewModel.Factory
    private val viewModel: HomepageViewModel by viewModels {
        withFactory(
            viewModelFactory, intent.extras
        )
    }
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun getViewBinding(): ActivityHomepageBinding {
        return ActivityHomepageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setUpNavController()
        setUpPermissionLauncher()
        setUpStatusBarDisplay()
        setUpListener()
        setUpViewModelObserve()
    }

    private fun setUpNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.layout_container) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.navigationView, navController)
    }

    private fun setUpPermissionLauncher() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                viewModel.onDownloadClick()
            } else {
                Toast.makeText(this, "權限未取得，無法下載", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpStatusBarDisplay() {
        binding.ucNav.ucImgNavLeft.visibility = View.VISIBLE
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
            val statusHeight = getStatusBarHeight()
            binding.layoutConstraintlayout.layoutParams =
                (binding.layoutConstraintlayout.layoutParams as ViewGroup.MarginLayoutParams).apply {
                    setMargins(0, statusHeight, 0, 0)
                }
            insets
        }
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
    }

    private fun setUpListener() {
        binding.ucNav.ucImgNavLeft.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.ucNav.ucImgNavRight.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                //API 29以上不需請求權限
                viewModel.onDownloadClick()
            } else {
                checkStoragePermission()
            }
        }

        binding.homepageParkFragment.setOnClickListener {
            val navController = findNavController(R.id.layout_container)
            navController.navigate(R.id.homepageParkFragment)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        binding.homepageMapFragment.setOnClickListener {
            val navController = findNavController(R.id.layout_container)
            navController.navigate(R.id.homepageMapFragment)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.homepageTransportFragment.setOnClickListener {
            val navController = findNavController(R.id.layout_container)
            navController.navigate(R.id.homepageTransportFragment)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun setUpViewModelObserve() {
        viewModel.displayUcNavRight.observe(this) {
            if (it) {
                binding.ucNav.ucImgNavRight.visibility = View.VISIBLE
            } else {
                binding.ucNav.ucImgNavRight.visibility = View.GONE
            }
        }
        viewModel.displayDownload.observe(this) { url ->
            lifecycleScope.launch {
                try {
                    downloadImageToDownloads(url)
                    Toast.makeText(
                        this@HomepageActivity,
                        "下載成功！",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@HomepageActivity,
                        "下載失敗：${e.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private suspend fun downloadImageToDownloads(url: String) {
        withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                throw Exception("下載失敗：${response.code}")
            }
            val inputStream = response.body?.byteStream()
                ?: throw Exception("讀取失敗")
            val fileName = "taipei_image_${System.currentTimeMillis()}.jpg"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
                val resolver = contentResolver
                val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    ?: throw Exception("無法建立檔案URI")
                resolver.openOutputStream(uri).use { outputStream ->
                    inputStream.copyTo(outputStream!!)
                }
                values.clear()
                values.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(uri, values, null, null)
            } else {
                val downloadsDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val imageFile = java.io.File(downloadsDir, fileName)
                imageFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
    }

    private fun checkStoragePermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED -> {
                viewModel.onDownloadClick()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) -> {
                AlertDialog.Builder(this)
                    .setMessage("需要儲存權限來下載圖片")
                    .setPositiveButton("我知道了") { _: DialogInterface, _: Int ->
                        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                    .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                    .show()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }
}