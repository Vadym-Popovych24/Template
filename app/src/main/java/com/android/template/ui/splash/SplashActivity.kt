package com.android.template.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.databinding.Observable
import com.android.template.R
import com.android.template.data.models.enums.LoggedInMode
import com.android.template.databinding.ActivitySplashBinding
import com.android.template.ui.base.BaseActivity
import com.android.template.ui.login.LoginActivity
import com.android.template.ui.navigation.NavigationActivity
import com.android.template.ui.splash.viewmodel.SplashViewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {

    override val fragmentContainerId = R.id.fl_for_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Open main activity if ViewModel is inactive.


        viewModel.loggedInMode.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        openActivity()
                    }, 2000
                )
            }
        })
        viewModel.decideNextActivity()

        intent?.extras?.apply {
            keySet().forEach {
                Log.d("myLogs", "Splash $it = [${getString(it)}]")
            }
        }
    }

    private fun openActivity() {
        var intent: Intent? = null
        when (viewModel.loggedInMode.get()) {
            LoggedInMode.LOGGED_IN_MODE_SERVER.getType() -> {
                intent = NavigationActivity.newIntent(this@SplashActivity)
            }
            LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType() -> intent =
                LoginActivity.newIntent(this@SplashActivity)
        }

        startActivity(intent)
        finish()
    }
}