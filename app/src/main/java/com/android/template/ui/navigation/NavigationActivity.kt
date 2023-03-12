package com.android.template.ui.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.android.template.R
import com.android.template.data.models.enums.NotificationType
import com.android.template.ui.base.BaseActivityWithMenuPublic
import com.android.template.ui.navigation.viewmodel.NavigationHeaderViewModel

class NavigationActivity : BaseActivityWithMenuPublic<NavigationHeaderViewModel>() {

    override val fragmentContainerId = R.id.fl_for_fragment_public

    override fun updateBottomNavigation(bottomNavigationVisibility: Int) {
        findViewById<View>(R.id.bottomNavigation)?.visibility = bottomNavigationVisibility
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenInitialized = true
        viewModel.moveToProfileCallback = {
            navController.navigate(R.id.profileFragment)
            closeDrawer()
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
        }
    }

    override fun onItemSelected(item: MenuItem): Boolean {
        selectItem(item.itemId)
        return true
    }

    private fun selectItem(itemId: Int) = when (itemId) {
        R.id.navigation_home -> navController.navigate(R.id.navigationHome)
        R.id.navigation_recommendations -> navController.navigate(R.id.navigationRecommendations)
        R.id.navigation_liked -> navController.navigate(R.id.navigationLiked)
        R.id.navigation_history -> navController.navigate(R.id.navigationHistory)
        R.id.navigation_menu_item_1 -> navController.navigate(R.id.navigationMenuItem1)
        R.id.navigation_menu_item_2 -> navController.navigate(R.id.navigationMenuItem2)
        R.id.navigation_menu_item_3 -> navController.navigate(R.id.navigationMenuItem3)
        R.id.navigation_menu_item_4 -> navController.navigate(R.id.navigationMenuItem4)
        R.id.navigation_coroutines -> navController.navigate(R.id.navigationMenuCoroutine)
        R.id.navigation_compose -> navController.navigate(R.id.navigationMenuCompose)

        R.id.navigation_settings -> navController.navigate(R.id.navigationSettings)

        else -> showStub()
    }

    override fun onDrawerOpened() {
        super.openDrawer()
    }


    override fun onStop() {
        super.onStop()
        screenInitialized = false
    }

    companion object {

        private const val EXTRA_NOTIFICATION_TYPE = "EXTRA_NOTIFICATION_TYPE"
        private const val EXTRA_ITEM_ID = "EXTRA_ITEM_ID"
        private const val EXTRA_BADGE = "EXTRA_BADGE"

        var screenInitialized = false

        fun newIntent(context: Context) = Intent(context, NavigationActivity::class.java)

        fun newIntent(
            context: Context, notificationType: NotificationType, itemId: String?,
            badge: Int? = null
        ) = newIntent(context).apply {
            putExtra(EXTRA_NOTIFICATION_TYPE, notificationType.ordinal)
            putExtra(EXTRA_ITEM_ID, itemId)
            badge?.let { putExtra(EXTRA_BADGE, it) }
        }
    }

}