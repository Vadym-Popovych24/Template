package com.android.template.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.android.template.R
import dagger.android.support.DaggerAppCompatActivity

class LoginActivity : DaggerAppCompatActivity() {

    private val navController: NavController by lazy {
        Navigation.findNavController(this, R.id.fl_for_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        if (savedInstanceState == null) {
            navController.navigate(
                R.id.loginFragment,
                LoginFragment.initArgs(ACTION_REFRESH_TOKEN.equals(intent?.action))
            )
        }
    }

    companion object {

        private const val ACTION_REFRESH_TOKEN = "ACTION_REFRESH_TOKEN"

        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }

        fun newIntentToRefreshToken(context: Context) =
            newIntent(context).apply { action = ACTION_REFRESH_TOKEN }

    }
}