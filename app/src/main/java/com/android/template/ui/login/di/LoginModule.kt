package com.android.template.ui.login.di

import androidx.lifecycle.ViewModel
import com.android.template.di.qualifiers.ViewModelKey
import com.android.template.ui.login.reset.viewmodel.PasswordResetViewModel
import com.android.template.ui.login.signup.viewmodel.SignUpViewModel
import com.android.template.ui.login.viewmodel.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface LoginModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun provideLoginVM(loginVM: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    fun provideSignUpVM(signUpVM: SignUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PasswordResetViewModel::class)
    fun providePasswordResetVM(passwordResetVM: PasswordResetViewModel): ViewModel
}