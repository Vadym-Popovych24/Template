package com.android.template.ui.login.di

import com.android.template.ui.login.LoginFragment
import com.android.template.ui.login.reset.ResetConfirmationCodeFragment
import com.android.template.ui.login.reset.ResetEmailFragment
import com.android.template.ui.login.reset.ResetPasswordFragment
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
    abstract fun provideResetEmailFragment(): ResetEmailFragment

    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun provideResetConfirmationCodeFragment(): ResetConfirmationCodeFragment

    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun provideResetPasswordFragment(): ResetPasswordFragment
}