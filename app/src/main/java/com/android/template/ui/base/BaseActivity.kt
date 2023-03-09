package com.android.template.ui.base

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.android.template.R
import com.android.template.utils.ViewModelProviderFactory
import com.android.template.utils.getGenericClassExtends
import com.android.template.utils.getLayoutId
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel> : DaggerAppCompatActivity() {
    val navController: NavController by lazy {
//        Navigation.findNavController(this, fragmentContainerId)
        val navHostFragment =
            supportFragmentManager.findFragmentById(fragmentContainerId) as NavHostFragment
        navHostFragment.navController
    }

    @Inject
    lateinit var mVMFactory: ViewModelProviderFactory

    lateinit var viewDataBinding: T
        private set

    lateinit var viewModel: V

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    open val bindingVariable: Int = com.android.template.BR.viewModel

    @get:IdRes
    open val fragmentContainerId: Int = R.id.fl_for_fragment

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
            if (!supportFragmentManager.popBackStackImmediate()) {
                finish()
            }
        }
    }

    /**
     * Called to do initial creation of a fragment.
     * <p>
     * Note that this can be called while the fragment's activity is still in the process of
     * being created. As such, you can not rely on things like the activity's content view
     * hierarchy being initialized at this point. If you want to do work once the activity itself
     * is created, see onActivityCreated(Bundle).
     *
     * @param savedInstanceState Bundle: If the fragment is being re-created from a previous saved state, this is the state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        performDataBinding()
        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)
    }

    /**
     * Perform android injection for activity for dagger
     */
    private fun performDependencyInjection() {
        AndroidInjection.inject(this)
        val viewModelClass = javaClass.getGenericClassExtends(BaseViewModel::class.java)
        viewModel = provideViewModel(
            if (viewModelClass != null) {
                viewModelClass as Class<V>
            } else {
                throw IllegalArgumentException()
            }
        )

        viewModel.messageCallback = { message ->
            if (!TextUtils.isEmpty(message)) {
                onError(message)
            }
        }
    }

    protected fun onError(message: String?) = showToast(message)

    protected fun showToast(message: String?) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun provideViewModel(clazz: Class<V>): V = ViewModelProvider(this, mVMFactory)[clazz]

    /**
     * Perform data binding for corresponding view
     */
    private fun performDataBinding() {
        javaClass.getGenericClassExtends(ViewDataBinding::class.java)?.getLayoutId()?.let {
            viewDataBinding = DataBindingUtil.setContentView(this, it)
            viewDataBinding.setVariable(bindingVariable, viewModel)
            viewDataBinding.executePendingBindings()
        }
    }

    protected fun showStub() = showFragment(Stub())


    private fun showFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        if (supportFragmentManager.fragments.size > 0) {
            fragmentTransaction.addToBackStack("stub")
        }

        fragmentTransaction.replace(fragmentContainerId, fragment, "stub")
            .commit()
    }

    protected fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        view.clearFocus()
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    protected fun initConfirmationAlert(
        @StringRes confirmationSubject: Int,
        confirmationCallback: () -> Unit
    ): AlertDialog = AlertDialog.Builder(this)
        .setTitle(confirmationSubject)
        .setPositiveButton(
            R.string.ok
        ) { _, _ -> confirmationCallback() }
        .setNegativeButton(R.string.cancel, null)
        .show()

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}