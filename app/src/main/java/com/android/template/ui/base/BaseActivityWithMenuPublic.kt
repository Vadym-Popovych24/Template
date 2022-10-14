package com.android.template.ui.base

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.android.template.R
import com.android.template.databinding.ActivityNavigationPublicBinding
import com.android.template.ui.navigation.NavigationActivityCallback
import kotlinx.android.synthetic.main.activity_navigation_public.view.*

abstract class BaseActivityWithMenuPublic<V : BaseViewModel> :
    BaseActivity<ActivityNavigationPublicBinding, V>(), NavigationActivityCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        findViewById<BottomNavigationView>(R.id.bottomNavigation).setOnNavigationItemSelectedListener {
            selectedBottomNavigationItems()
            onItemSelected(it)
        }

        if (savedInstanceState == null)
            findViewById<BottomNavigationView>(R.id.bottomNavigation).selectedItemId =
                R.id.navigation_recommendations

        val toggle = object : ActionBarDrawerToggle(
            this,
            viewDataBinding.root.drawer_layout_public,
            R.string.none,
            R.string.none
        ) {
            override fun onDrawerOpened(drawerView: View) {
                onDrawerOpened()
            }
        }

        toggle.drawerArrowDrawable.color = ActivityCompat.getColor(this, android.R.color.white)

        viewDataBinding.root.drawer_layout_public.addDrawerListener(toggle)

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

    private fun showDemoToast(text: String) {
        Toast.makeText(applicationContext, "You click to $text", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (viewDataBinding.root.drawer_layout_public.isDrawerOpen(GravityCompat.START)) {
            closeDrawer()
        } else navController.navigateUp() || super.onSupportNavigateUp()

    }

    override fun openDrawer() {
        viewDataBinding.root.drawer_layout_public.openDrawer(GravityCompat.START)
    }

    protected fun closeDrawer() {
        viewDataBinding.root.drawer_layout_public.closeDrawer(GravityCompat.START)
    }

    abstract fun onDrawerOpened()
}