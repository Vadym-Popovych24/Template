package com.android.template.ui.login.di

import com.android.template.ui.login.LoginFragment
import com.android.template.ui.login.reset.PasswordResetFragment
import com.android.template.ui.login.signup.SignUpFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class LoginProvider {
    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun provideLoginFragment(): LoginFragment

    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun provideSignUpFragment(): SignUpFragment

    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun providePasswordResetFragment(): PasswordResetFragment
}