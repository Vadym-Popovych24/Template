package com.android.template.ui.profile.di

import androidx.lifecycle.ViewModel
import com.android.template.di.qualifiers.ViewModelKey
import com.android.template.ui.profile.avatar.viewmodel.ChangeAvatarViewModel
import com.android.template.ui.profile.viewmodel.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
interface ProfileModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    fun provideVM(profileVM: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangeAvatarViewModel::class)
    fun provideChangeAvatarViewModel(changeAvatarVM: ChangeAvatarViewModel): ViewModel
}