package com.android.template.ui.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.fragment.app.commit
import com.android.template.R
import com.android.template.data.models.enums.NotificationType
import com.android.template.ui.base.BaseActivityWithMenuPublic
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.base.Stub
import com.android.template.ui.compose.ComposeFragment
import com.android.template.ui.coroutine.CoroutineFragment
import com.android.template.ui.bottom_menu4.BottomMenu4Fragment
import com.android.template.ui.menu1.MenuItem1Fragment
import com.android.template.ui.menu2.MenuItem2Fragment
import com.android.template.ui.menu3.MenuItem3Fragment
import com.android.template.ui.menu4.MenuItem4Fragment
import com.android.template.ui.navigation.viewmodel.NavigationHeaderViewModel
import com.android.template.ui.profile.ProfileFragment
import com.android.template.ui.popular.PopularFragment
import com.android.template.ui.bottom_menu3.BottomMenu3Fragment
import com.android.template.ui.bottom_menu2.BottomMenu2Fragment
import com.android.template.ui.settings.SettingsFragment


class NavigationActivity : BaseActivityWithMenuPublic<NavigationHeaderViewModel>() {

    override val fragmentContainerId = R.id.fragment_container
    private val bottomNavigation
        get() = viewDataBinding.includeBodyPublic.bottomNavigation

    @get:Synchronized
    @set:Synchronized
    private var activeFragment: Fragment? = null
    private var popularFragment: PopularFragment? = null
    private var bottomMenu2Fragment: BottomMenu2Fragment? = null
    private var bottomMenu3Fragment: BottomMenu3Fragment? = null
    private var bottomMenu4Fragment: BottomMenu4Fragment? = null
    private var isBottomFragmentsAdded = false

    override fun updateBottomNavigation(bottomNavigationVisibility: Int) {
        findViewById<View>(R.id.bottomNavigation)?.visibility = bottomNavigationVisibility
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        screenInitialized = true
        viewModel.moveToProfileCallback = {
            showFragment(ProfileFragment.newInstance())
            closeDrawer()
        }
        initBottomMenu()
        openFirstAvailableScreenInBottomMenu()
        handleOnBackPressed()

        viewModel.getProfileDataFromDB().observe(this) { profileAndAvatar ->
            viewModel.userName.set("${profileAndAvatar?.profileEntity?.firstName}  ${profileAndAvatar.profileEntity?.lastName}")
            viewModel.userEmail.set(profileAndAvatar?.profileEntity?.email)
            viewModel.userAvatar.set(profileAndAvatar?.profileEntity?.avatarPath)
        }
    }

    private fun initBottomMenu() {
        //bottom menu
        if (!isBottomFragmentsAdded) {
            isBottomFragmentsAdded = true
            //init bottom menu fragments
            initFragmentsOfBottomMenu()
            addFragmentsOfBottomMenu()
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
        R.id.bottom_menu_item_popular -> showPopularFragment()
        R.id.bottom_menu_item_2 -> showBottomMenu2Fragment()
        R.id.bottom_menu_item_3 -> showBottomMenu3Fragment()
        R.id.bottom_menu_item_4 -> showBottomMenu4Fragment()
        R.id.navigation_menu_item_1 -> showFragment(MenuItem1Fragment())
        R.id.navigation_menu_item_2 -> showFragment(MenuItem2Fragment())
        R.id.navigation_menu_item_3 -> showFragment(MenuItem3Fragment())
        R.id.navigation_menu_item_4 -> showFragment(MenuItem4Fragment())
        R.id.navigation_coroutines -> showFragment(CoroutineFragment())
        R.id.navigation_compose -> showFragment(ComposeFragment())
        R.id.navigation_settings -> showFragment(SettingsFragment())

        else -> showStub()
    }

    override fun showPopularFragment() {
        popularFragment?.let {
            showFragment(it)
        }
    }

    override fun showBottomMenu2Fragment() {
        bottomMenu2Fragment?.let {
            showFragment(it)
        }
    }

    override fun showBottomMenu3Fragment() {
        bottomMenu3Fragment?.let {
            showFragment(it)
        }
    }

    override fun showBottomMenu4Fragment() {
        bottomMenu4Fragment?.let {
            showFragment(it)
        }
    }

    private fun initFragmentsOfBottomMenu() {
        if (popularFragment == null) popularFragment = PopularFragment()
        if (bottomMenu2Fragment == null) bottomMenu2Fragment = BottomMenu2Fragment()
        if (bottomMenu3Fragment == null) bottomMenu3Fragment = BottomMenu3Fragment()
        if (bottomMenu4Fragment == null) bottomMenu4Fragment = BottomMenu4Fragment()
    }

    private fun addFragmentsOfBottomMenu() {
        addFragmentOfBottomMenu(popularFragment)
        addFragmentOfBottomMenu(bottomMenu2Fragment)
        addFragmentOfBottomMenu(bottomMenu3Fragment)
        addFragmentOfBottomMenu(bottomMenu4Fragment)
    }

    private fun addFragmentOfBottomMenu(fragment: Fragment?) {
        fragment?.let {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                supportFragmentManager.findFragmentByTag(it::class.java.name)?.let { remove(it) }
                add(fragmentContainerId, it, it::class.java.name)
                hide(it)
            }
        }
    }

    private fun handleOnBackPressed() {

        onBackPressedDispatcher.addCallback(this) {
            val currentFragment = activeFragment!!

            if (preventBackPressedFragments.contains(currentFragment::class.java.name)) {
                if (viewDataBinding.drawerLayoutPublic.isDrawerOpen(GravityCompat.START) || viewDataBinding.drawerLayoutPublic.isDrawerOpen(
                        GravityCompat.END
                    )
                ) {
                    closeDrawer()
                } else if (currentFragment::class.java.name == PopularFragment::class.java.name || currentFragment::class.java.name == Stub::class.java.name) {
                    finish()
                } else {
                    openFirstAvailableScreenInBottomMenu()
                }
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

    @Synchronized
    override fun showFragment(fragment: Fragment?) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            fragment?.let {
                activeFragment?.let { active -> hide(active) }

                if (it is PopularFragment || it is BottomMenu2Fragment || it is BottomMenu3Fragment || it is BottomMenu4Fragment) {
                    activeFragment = it
                    show(it)
                } else {
                    val existingFragment =
                        supportFragmentManager.findFragmentByTag(it::class.java.name)
                    existingFragment?.let { fragment -> remove(fragment) }
                    activeFragment = it
                    addToBackStack(it::class.java.name)
                    add(fragmentContainerId, it, it::class.java.name)
                }
                if (fragment is BaseFragment<*, *>) updateBottomNavigation(fragment.bottomNavigationVisibility)
            }
        }
    }

    override fun setCurrentFragmentOfBottomMenu(fragment: Fragment) {
        activeFragment = fragment
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

    companion object {

        private var preventBackPressedFragments = mutableListOf(
            PopularFragment::class.java.name,
            BottomMenu2Fragment::class.java.name,
            BottomMenu3Fragment::class.java.name,
            BottomMenu4Fragment::class.java.name
        )

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