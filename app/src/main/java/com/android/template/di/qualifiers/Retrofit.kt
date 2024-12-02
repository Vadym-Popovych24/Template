package com.android.template.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WebRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NewsRetrofit