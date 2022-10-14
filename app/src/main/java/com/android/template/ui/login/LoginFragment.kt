package com.android.template.ui.login

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.Observable
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.android.template.R
import com.android.template.databinding.FragmentLoginBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.login.viewmodel.LoginViewModel
import com.android.template.ui.navigation.NavigationActivity
import com.android.template.utils.setOnActionDoneCallback
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservableFields()
        initToolbar()

        password?.setOnActionDoneCallback {
            viewModel.loginClick()
        }

        signUp?.setOnClickListener {
            navController.navigate(R.id.signUpFragment)
        }

        forgotPassword?.setOnClickListener {
            navController.navigate(R.id.resetPasswordFragment)
        }

      val database = FirebaseDatabase
          .getInstance("https://irecommend-96e4f-default-rtdb.europe-west1.firebasedatabase.app")
          .reference.child("users")

      //  val myRef = database.reference

        database.child("1").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        }

    private fun subscribeToObservableFields() {
        viewModel.authCompleteCallback = {
            if (!onlyForRefreshToken()) {
                hideKeyboard()
                moveToActivity(NavigationActivity.newIntent(requireContext()))
            }
            requireActivity().finish()
        }
    }

    private fun initToolbar() {
        requireActivity().window.statusBarColor = Color.argb(255, 255, 255, 255)
    }

    private fun onlyForRefreshToken(): Boolean =
        arguments?.getBoolean(EXTRA_ONLY_REFRESH_TOKEN, false)!!

    companion object {
        val TAG: String = LoginFragment::class.java.simpleName
        private const val EXTRA_ONLY_REFRESH_TOKEN = "extra_only_refresh_token"

        fun initArgs(onlyRefreshToken: Boolean) = Bundle().apply {
            putBoolean(EXTRA_ONLY_REFRESH_TOKEN, onlyRefreshToken)
        }

        fun newInstance(onlyRefreshToken: Boolean): LoginFragment {
            val args = Bundle()
            val fragment = LoginFragment()
            args.putBoolean(EXTRA_ONLY_REFRESH_TOKEN, onlyRefreshToken)
            fragment.arguments = args
            return fragment
        }
    }
}