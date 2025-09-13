package com.android.template.ui.login

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.android.template.R
import dagger.android.support.DaggerAppCompatActivity

class LoginActivity : DaggerAppCompatActivity() {

    private val navController: NavController by lazy {
        Navigation.findNavController(this, R.id.fl_for_fragment)
    }

    var notificationCallback: ((Boolean) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        if (savedInstanceState == null) {
            navController.navigate(
                R.id.navigationLogin
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission()
        } else {
            notificationCallback?.invoke(true)
        }

        notificationCallback = { permissionGranted ->
            if (!permissionGranted) {
                // Deny notifications
            } else {
                // Allow notifications
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkNotificationPermission() {
        val permissionStatus = ContextCompat.checkSelfPermission(
            applicationContext,
            POST_NOTIFICATIONS_PERMISSION
        )
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            startNotificationPermissionRequest()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun startNotificationPermissionRequest() {
        requestNotificationPermissionLauncher.launch(POST_NOTIFICATIONS_PERMISSION)
    }

    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissionGranted ->
        notificationCallback?.invoke(permissionGranted)
    }

    companion object {

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private const val POST_NOTIFICATIONS_PERMISSION = Manifest.permission.POST_NOTIFICATIONS

        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }

    }
}