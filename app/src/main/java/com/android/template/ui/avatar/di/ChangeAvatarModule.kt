package com.android.template.ui.avatar.di

import androidx.lifecycle.ViewModel
import com.android.template.di.qualifiers.ViewModelKey
import com.android.template.ui.avatar.viewmodel.ChangeImageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ChangeAvatarModule {

    @Binds
    @IntoMap
    @ViewModelKey(ChangeImageViewModel::class)
    fun provideChangeAvatarViewModel(changeAvatarVM: ChangeImageViewModel): ViewModel
}