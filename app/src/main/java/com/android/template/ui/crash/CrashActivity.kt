package com.android.template.ui.crash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.android.template.R
import dagger.android.support.DaggerAppCompatActivity

class CrashActivity : DaggerAppCompatActivity() {

        private val navController: NavController by lazy {
            Navigation.findNavController(this, R.id.fl_crash_fragment)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_crash)
            supportActionBar?.hide()

            if (savedInstanceState == null) {
                navController.navigate(
                    R.id.crashFragment
                )
            }
        }

    override fun onBackPressed() {
        finish()
    }

     companion object {

        fun newIntent(context: Context): Intent =
            Intent(context, CrashActivity::class.java)

    }

    }