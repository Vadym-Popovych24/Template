package com.android.template.ui.crash

import android.os.Bundle
import android.view.View
import com.android.template.databinding.FragmentCrashBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.crash.viewmodel.CrashViewModel
import com.android.template.ui.login.LoginActivity

class CrashFragment: BaseFragment<FragmentCrashBinding, CrashViewModel>(){

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       super.onViewCreated(view, savedInstanceState)

       binding.tvHere.setOnClickListener {
           viewModel.logOut {
               requireActivity().finish()
               startActivity(LoginActivity.newIntent(requireContext()))
           }
       }

   }

}