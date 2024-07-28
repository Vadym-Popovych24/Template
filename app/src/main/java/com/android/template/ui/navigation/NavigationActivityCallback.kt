package com.android.template.ui.navigation

import androidx.fragment.app.Fragment

interface NavigationActivityCallback {

    fun openDrawer()

    fun updateBottomNavigation(bottomNavigationVisibility: Int)

    fun setSelectedMenuItem(menuItemId: Int)

    fun popBackStackByFragmentName(fragmentName: String)

    fun setCurrentFragmentOfBottomMenu(fragment: Fragment)

    fun showFragment(fragment: Fragment?)

    fun showRecommendationFragment()

    fun showLikedFragment()

    fun showHistoryFragment()

    fun showHomeFragment()
}