package com.android.template.ui.base

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.android.template.R
import com.android.template.databinding.ActivityNavigationPublicBinding
import com.android.template.ui.navigation.NavigationActivityCallback

abstract class BaseActivityWithMenuPublic<V : BaseViewModel> :
    BaseActivity<ActivityNavigationPublicBinding, V>(), NavigationActivityCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        findViewById<BottomNavigationView>(R.id.bottomNavigation).setOnItemSelectedListener {
            selectedBottomNavigationItems()
            onItemSelected(it)
        }

        if (savedInstanceState == null)
            findViewById<BottomNavigationView>(R.id.bottomNavigation).selectedItemId =
                R.id.navigation_recommendations

        val toggle = object : ActionBarDrawerToggle(
            this,
            viewDataBinding.drawerLayoutPublic,
            R.string.none,
            R.string.none
        ) {
            override fun onDrawerOpened(drawerView: View) {
                onDrawerOpened()
            }
        }

        toggle.drawerArrowDrawable.color = ActivityCompat.getColor(this, android.R.color.white)

        viewDataBinding.drawerLayoutPublic.addDrawerListener(toggle)

        toggle.syncState()

        findViewById<NavigationView>(R.id.left_navigation).setNavigationItemSelectedListener {
            closeDrawer()
            unselectedBottomNavigationItems()
            onItemSelected(it)
        }
    }

    private fun selectedBottomNavigationItems() {
        viewDataBinding.includeBodyPublic.bottomNavigation.menu.setGroupCheckable(0, true, true)
    }

    private fun unselectedBottomNavigationItems() {
        viewDataBinding.includeBodyPublic.bottomNavigation.menu.setGroupCheckable(0, false, true)
    }

    abstract fun onItemSelected(item: MenuItem): Boolean

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            openDrawer()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (viewDataBinding.drawerLayoutPublic.isDrawerOpen(GravityCompat.START)) {
            closeDrawer()
        } else navController.navigateUp() || super.onSupportNavigateUp()

    }

    override fun openDrawer() {
        viewDataBinding.drawerLayoutPublic.openDrawer(GravityCompat.START)
    }

    protected fun closeDrawer() {
        viewDataBinding.drawerLayoutPublic.closeDrawer(GravityCompat.START)
    }

    abstract fun onDrawerOpened()
}