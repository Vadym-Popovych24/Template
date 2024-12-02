package com.android.template.ui.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.android.template.R
import com.android.template.ui.crash.CrashActivity
import com.android.template.ui.navigation.NavigationActivity
import com.android.template.ui.navigation.NavigationActivityCallback
import com.android.template.utils.ViewModelProviderFactory
import com.android.template.utils.getGenericClassExtends
import com.android.template.utils.getLayoutId
import com.android.template.utils.initNavigationIcon
import dagger.android.support.AndroidSupportInjection
import net.cachapa.expandablelayout.ExpandableLayout
import javax.inject.Inject

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment() {

    val navController: NavController by lazy { findNavController() }

    var viewWasRestored = false

    @Inject
    lateinit var mVMFactory: ViewModelProviderFactory

    var baseActivity: BaseActivity<*, *>? = null

    open val bottomNavigationVisibility = View.GONE

    protected var viewDataBinding: T? = null

    protected val binding: T
        get() = viewDataBinding
            ?: throw IllegalStateException("Trying to access the binding outside of the view lifecycle.")

    lateinit var viewModel: V

    private val isLoadingDataCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            if (baseActivity != null)
                baseActivity!!.viewModel.isLoading.set(viewModel.isLoading.get())
        }
    }

    /**
     * The expression language allows us to write expressions that connect variables to the views
     * in the layout. The Data Binding Library automatically generates the classes required to bind
     * the views in the layout with our data objects.
     *
     *
     * Override this method to set binding variable.
     *
     * @return variable id
     */
    open val bindingVariable: Int = com.android.template.BR.viewModel


    /**
     * Called when a fragment is first attached to its activity. onCreate(Bundle) will be called after this.
     *
     * @param context of activity.
     */
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            val activity = context as BaseActivity<*, *>?
            this.baseActivity = activity!!
        }
    }


    /**
     * Called to do initial creation of a fragment.
     *
     *
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
        setHasOptionsMenu(false)
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any
     * views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's UI
     * should be attached to. The fragment should not add the view
     * itself, but this can be used to generate the LayoutParams of
     * the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a
     * previous saved state as given here.
     * @return view objects
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = requireActivity()

        if (activity is NavigationActivityCallback) {
            activity.updateBottomNavigation(bottomNavigationVisibility)
        }

        initViewModel()

        if (viewDataBinding == null) {
            javaClass.getGenericClassExtends(ViewDataBinding::class.java)?.getLayoutId()?.let {
                viewDataBinding = DataBindingUtil.inflate(inflater, it, container, false)
                subscribeToObservableFields()
                viewWasRestored = false
            }
        } else {
            viewWasRestored = true
        }

        return viewDataBinding?.root
    }

    private fun initViewModel() {
        val viewModelClass = javaClass.getGenericClassExtends(BaseViewModel::class.java)

        viewModel = provideViewModel(
            if (viewModelClass != null) {
                viewModelClass as Class<V>
            } else {
                throw IllegalArgumentException()
            }
        )

        viewModel.messageCallback = { message ->
           showToast(message)
        }
    }

    private fun provideViewModel(clazz: Class<V>): V {
        return ViewModelProvider(requireActivity(), mVMFactory).get(clazz)
    }

    /**
     * Subscribe to observable viewmodel items
     */
    private fun subscribeToObservableFields() {
        viewModel.isLoading.addOnPropertyChangedCallback(isLoadingDataCallback)
    }

    protected fun showFragment(fragment: Fragment) {
        val activity = requireActivity()
        if (activity is NavigationActivityCallback) {
            activity.showFragment(fragment)
        }
    }


    /**
     * Called when the fragment is no longer attached to its activity. This is called after onDestroy().
     */
    override fun onDetach() {
        super.onDetach()
        baseActivity = null
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        activity?.let {
            if (it is NavigationActivityCallback) {
                if (!hidden) {
                    it.updateBottomNavigation(bottomNavigationVisibility)
                    it.setCurrentFragmentOfBottomMenu(this)
                }
            }
        }
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned,
     * but before any saved state has been restored in to the view.
     *
     * @param view               The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle)
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     * saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.setVariable(bindingVariable, viewModel)
        viewDataBinding?.executePendingBindings()
    }

    /**
     * Method to do fragment injection.
     */
    private fun performDependencyInjection() {
        AndroidSupportInjection.inject(this)
    }

    protected fun moveToActivity(intent: Intent) {
        requireActivity().run { startActivity(intent) }
    }

    protected fun showToast(message: String?) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }

    protected fun showToast(@StringRes message: Int) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_LONG).show()
        }
    }

    protected fun moveToCrash() {
        startActivity(CrashActivity.newIntent(requireContext()))
    }

    protected fun Toolbar.initDefaultNavigation() =
        initNavigationIcon(R.drawable.ic_toolbar_menu) {
            openDrawer()
        }

    protected fun Toolbar.initUpNavigation() = initNavigationIcon(
        R.drawable.ic_arrow_back_black
    ) {
        navigateUp()
    }

    protected fun Toolbar.releaseNavigationIcon(): Unit {
        navigationIcon = null
    }

    private fun openDrawer() {
        if (activity is NavigationActivityCallback) {
            (activity as NavigationActivityCallback).openDrawer()
        }
    }

    protected fun hasPermissions(permissions: Array<out String>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    protected fun isShowPermissionRequestRationale(permissions: Array<out String>): Boolean {
        permissions.forEach {
            val result = shouldShowRequestPermissionRationale(it)
            if (!result) {
                return false
            }
        }
        return true
    }

    protected fun hideKeyboard() {
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity?.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        view.clearFocus()
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    protected fun initExpandableLayout(
        expandableLayout: ExpandableLayout?, toggleButton: View?,
        expandableIcon: ImageView?
    ) {
        toggleButton?.setOnClickListener {
            expandableLayout?.toggle(true)
        }
        expandableLayout?.setOnExpansionUpdateListener { _, _ ->
            expandableIcon?.setImageResource(
                if (expandableLayout.isExpanded) {
                    R.drawable.ic_collapse
                } else {
                    R.drawable.ic_expand
                }
            )
        }
    }

    protected fun moveToMainActivity() {
        requireActivity().finish()
        hideKeyboard()
        moveToActivity(NavigationActivity.newIntent(requireContext()))
    }

    open fun navigateUp() {
        hideKeyboard()
        popBackStack()
    }

    private fun popBackStack() {
        activity?.onBackPressed()
    }

    protected fun initConfirmationAlert(
        context: Context, @StringRes confirmationSubject: Int,
        confirmationCallback: () -> Unit
    ): AlertDialog = AlertDialog.Builder(context)
        .setTitle(confirmationSubject)
        .setPositiveButton(
            R.string.ok
        ) { _, _ -> confirmationCallback() }
        .setNegativeButton(R.string.cancel, null)
        .show()

    fun setFragmentResult(key: String) = setFragmentResult(key, Bundle())

}