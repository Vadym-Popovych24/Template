package com.android.template.ui.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.navigation.fragment.NavHostFragment
import com.android.template.R
import com.android.template.data.models.enums.NotificationType
import com.android.template.ui.base.BaseActivityWithMenuPublic
import com.android.template.ui.base.Stub
import com.android.template.ui.bottom_menu4.BottomMenu4Fragment
import com.android.template.ui.navigation.viewmodel.NavigationHeaderViewModel
import com.android.template.ui.popular.PopularFragment
import com.android.template.ui.bottom_menu3.BottomMenu3Fragment
import com.android.template.ui.bottom_menu2.BottomMenu2Fragment
import com.android.template.ui.popular.details.MovieDetailsFragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class NavigationActivity : BaseActivityWithMenuPublic<NavigationHeaderViewModel>() {

    override val fragmentContainerId = R.id.fragment_container
    private val bottomNavigation
        get() = viewDataBinding.includeBodyPublic.bottomNavigation

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(fragmentContainerId) as NavHostFragment
    }

    private val menus = listOf<Int>(R.menu.bottom_menu, R.menu.bottom_menu_2, R.menu.bottom_menu_3, R.menu.bottom_menu_4)

    private var menuVariant = 1

    override fun updateBottomNavigation(bottomNavigationVisibility: Int) {
        findViewById<View>(R.id.bottomNavigation)?.visibility = bottomNavigationVisibility
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setMenuOrder()
        screenInitialized = true
        openFirstAvailableScreenInBottomMenu()
        if (intent.extras != null && intent.extras!!.getString(EXTRA_TOPIC) != null) {
            navigateByTopic(intent.extras!!)
        }

        viewModel.moveToProfileCallback = {
            navController.navigate(
                R.id.navigationProfile
            )
            closeDrawer()
        }

        handleOnBackPressed()

        viewModel.getProfileDataFromDB().observe(this) { profileAndAvatar ->
            viewModel.userName.set("${profileAndAvatar?.profileEntity?.firstName}  ${profileAndAvatar.profileEntity?.lastName}")
            viewModel.userEmail.set(profileAndAvatar?.profileEntity?.email)
            viewModel.userAvatar.set(profileAndAvatar?.profileEntity?.avatarPath)
        }

        // Add navigation listener to handle bottom menu selection
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigationPopular -> setSelectedMenuItem(R.id.bottom_menu_item_popular)
                R.id.navigationBottomMenu2 -> setSelectedMenuItem(R.id.bottom_menu_item_2)
                R.id.navigationBottomMenu3 -> setSelectedMenuItem(R.id.bottom_menu_item_3)
                R.id.navigationBottomMenu4 -> setSelectedMenuItem(R.id.bottom_menu_item_4)
            }
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    override fun onItemSelected(item: MenuItem): Boolean {
        selectItem(item.itemId)
        return true
    }

    private fun selectItem(itemId: Int) = when (itemId) {
        R.id.bottom_menu_item_popular -> navController.navigate(R.id.navigationPopular)
        R.id.bottom_menu_item_2 -> navController.navigate(R.id.navigationBottomMenu2)
        R.id.bottom_menu_item_3 -> navController.navigate(R.id.navigationBottomMenu3)
        R.id.bottom_menu_item_4 -> navController.navigate(R.id.navigationBottomMenu4)
        R.id.navigation_menu_item_1 -> navController.navigate(R.id.navigationMenuItem1)
        R.id.navigation_menu_item_2 -> navController.navigate(R.id.navigationMenuItem2)
        R.id.navigation_menu_item_3 -> navController.navigate(R.id.navigationMenuItem3)
        R.id.navigation_menu_item_4 -> navController.navigate(R.id.navigationMenuItem4)
        R.id.navigation_coroutines -> navController.navigate(R.id.navigationMenuCoroutine)
        R.id.navigation_compose -> navController.navigate(R.id.navigationMenuCompose)
        R.id.navigation_settings ->  navController.navigate(R.id.navigationSettings)

        else -> showStub()
    }

    private fun handleOnBackPressed() {

        onBackPressedDispatcher.addCallback(this) {
            val currentFragment = navHostFragment.childFragmentManager.fragments.firstOrNull() ?: return@addCallback

            if (preventBackPressedFragments.contains(currentFragment::class.java.name)) {
                if (viewDataBinding.drawerLayoutPublic.isDrawerOpen(GravityCompat.START) ||
                    viewDataBinding.drawerLayoutPublic.isDrawerOpen(GravityCompat.END)
                ) {
                    closeDrawer()
                } else if (currentFragment::class.java.name == PopularFragment::class.java.name || currentFragment::class.java.name == Stub::class.java.name ||
                    menuVariant != 0 && currentFragment::class.java.name == BottomMenu2Fragment::class.java.name) {
                    finish()
                } else {
                    openFirstAvailableScreenInBottomMenu()
                }
            } else if (viewDataBinding.drawerLayoutPublic.isDrawerOpen(GravityCompat.START) ||
                viewDataBinding.drawerLayoutPublic.isDrawerOpen(GravityCompat.END)) {
                closeDrawer()
            } else if (currentFragment::class.java.name == PopularFragment::class.java.name || currentFragment::class.java.name == Stub::class.java.name) {
                finish()
            } else if (viewModel.isLoading.get() && currentFragment::class.java.name == Stub::class.java.name) {
                /**
                 * ignore because need keep open fragment when going progress
                 */
            } else {
                supportFragmentManager.popBackStack()
            }

        }
    }

    private fun openFirstAvailableScreenInBottomMenu() {
        hideAllLoading()
        val firstAvailableItem =
            bottomNavigation.menu.children.firstOrNull { it.isVisible } ?: return
        selectItem(firstAvailableItem.itemId)
    }

    private fun hideAllLoading() {
        viewDataBinding.contentLoadingProgressBar.isVisible = false
        viewDataBinding.drawerLayoutPublic.isVisible = true
    }

    override fun popBackStackByFragmentName(fragmentName: String) {
        supportFragmentManager.popBackStack(
            fragmentName, POP_BACK_STACK_INCLUSIVE
        )
    }

    override fun setSelectedMenuItem(menuItemId: Int) {
        bottomNavigation.menu.findItem(menuItemId).apply {
            isCheckable = true
            isChecked = true
        }
    }

    private fun navigateByTopic(extras: Bundle) {
        val topic = extras.getString(EXTRA_TOPIC)
        val itemId = extras.getString(EXTRA_ID)
        hideAllLoading()
        when (topic) {

            NotificationType.MOVIE_DETAILS.toString() -> {

                    itemId?.let {
                        navController.navigate(
                            R.id.navigationMovieDetails,
                            MovieDetailsFragment.newInstance(it)
                        )
                    }
            }

            else -> {
                Log.e(TAG, String.format("Unknown TOPIC %s", topic))

            }
        }
    }

    override fun setCurrentFragmentOfBottomMenu(fragment: Fragment) {
        when (fragment) {
            is PopularFragment -> {
                setSelectedMenuItem(R.id.bottom_menu_item_popular)
            }

            is BottomMenu2Fragment -> {
                setSelectedMenuItem(R.id.bottom_menu_item_2)
            }

            is BottomMenu3Fragment -> {
                setSelectedMenuItem(R.id.bottom_menu_item_3)
            }

            is BottomMenu4Fragment -> {
                setSelectedMenuItem(R.id.bottom_menu_item_4)
            }
        }
    }

    override fun onDrawerOpened() {
        super.openDrawer()
    }

    override fun onStop() {
        super.onStop()
        screenInitialized = false
    }

    override fun onResume() {
        super.onResume()

        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.fetchAndActivate().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val updated = task.result
                Log.d(REMOTE_CONFIG_TAG, "Config params updated: $updated")
                if (updated) {
                    setMenuOrder()
                }
            } else {
                Log.d(REMOTE_CONFIG_TAG, "Config params not updated!")
            }
        }

    }

    private fun setMenuOrder() {
        menuVariant = Firebase.remoteConfig.getLong("menu_variant").toInt() - 1
        if (menuVariant !in 0..3) menuVariant = 0
        bottomNavigation.menu.clear()
        bottomNavigation.inflateMenu(menus[menuVariant])
    }

    companion object {
        private const val REMOTE_CONFIG_TAG = "Remote config:"
        private val TAG: String = NavigationActivity::class.java.simpleName
        private var preventBackPressedFragments = mutableListOf(
            PopularFragment::class.java.name,
            BottomMenu2Fragment::class.java.name,
            BottomMenu3Fragment::class.java.name,
            BottomMenu4Fragment::class.java.name
        )

        private const val EXTRA_ID = "EXTRA_ID"
        private const val EXTRA_BADGE = "EXTRA_BADGE"
        const val EXTRA_TOPIC = "EXTRA_TOPIC"

        var screenInitialized = false

        fun newIntent(context: Context) = Intent(context, NavigationActivity::class.java)

        fun newIntent(context: Context, extras: Bundle?, badge: Int? = null) =
            newIntent(context).apply {

                // Handle actions for push notification
                if (extras != null) {
                    val topic = extras.getString(EXTRA_TOPIC)?.uppercase()
                    putExtra(EXTRA_TOPIC, topic)

                    when (topic) {
                        NotificationType.MOVIE_DETAILS.toString() -> {
                            putExtra(
                                EXTRA_ID,
                                extras.getString(EXTRA_ID)
                            )
                        }

                    }

                }
                badge?.let { putExtra(EXTRA_BADGE, it) }

            }
    }

}