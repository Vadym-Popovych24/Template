package com.android.template.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.databinding.Observable
import com.android.template.BR
import com.android.template.R
import com.android.template.data.models.enums.LoggedInMode
import com.android.template.data.models.enums.NotificationType
import com.android.template.databinding.ActivitySplashBinding
import com.android.template.service.NotificationService
import com.android.template.ui.base.BaseActivity
import com.android.template.ui.login.LoginActivity
import com.android.template.ui.navigation.NavigationActivity
import com.android.template.ui.navigation.NavigationActivity.Companion.EXTRA_ITEM_ID
import com.android.template.ui.splash.viewmodel.SplashViewModel

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
                Log.d("myLogs", "Splash $it = [${get(it)}]")
            }
        }
    }

    private fun openActivity() {
        var intent: Intent? = null
        when (viewModel.loggedInMode.get()) {
            LoggedInMode.LOGGED_IN_MODE_SERVER.getType() -> {
          //      intent = createNavigationIntent()
            }
            LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType() -> intent =
                LoginActivity.newIntent(this@SplashActivity)
        }

        startActivity(intent)
        finish()
    }

/*    private fun createNavigationIntent() = NavigationActivity.newIntent(this@SplashActivity).apply {
        intent?.extras?.getString(NotificationService.EXTRA_TOPIC)
            ?.let {
                NotificationType.findByTopic(it) }
            ?.let {
                putExtra(NavigationActivity.EXTRA_NOTIFICATION_TYPE, it.ordinal)
                intent?.extras?.getString(NotificationService.EXTRA_BADGE, "0")?.let { badge ->
                    putExtra(NotificationService.EXTRA_BADGE, badge.toInt())
                }

                when (it) {
                    NotificationType.JOB_ALERT -> putExtra(
                        EXTRA_ITEM_ID,
                        intent?.extras?.getString(NotificationService.EXTRA_JOB_ALERT_ID)
                    )
                    NotificationType.CONTACTS_REQUEST_APPROVED, NotificationType.CONTACTS_REQUEST -> putExtra(
                        EXTRA_ITEM_ID,
                        intent?.extras?.getString(NotificationService.EXTRA_RESUME_ID)
                    )
                    NotificationType.NEW_JOBS, NotificationType.JOB_APPLICATION -> putExtra(
                        EXTRA_ITEM_ID,
                        intent?.extras?.getString(NotificationService.EXTRA_JOB_ID)
                    )
                    NotificationType.NEW_MESSAGES -> putExtra(
                        EXTRA_ITEM_ID,
                        intent?.extras?.getString(NotificationService.EXTRA_CONVERSATION_ID)
                    )
                }
            }
    }*/
}