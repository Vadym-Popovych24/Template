package com.android.template.di.component

import android.content.Context
import com.android.template.TemplateApp
import com.android.template.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules =
    [AndroidInjectionModule::class,
        ActivityModule::class,
        ServiceModule::class,
        GeneralModule::class,
        ManagerModule::class,
        RepositoryModule::class,
        WebServiceModule::class,
        StorageModule::class]
)
interface AppComponent {
    fun inject(app: TemplateApp)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: TemplateApp): Builder

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}