package com.android.template.data.remote.interfaces

import io.reactivex.Completable

interface RefreshTokenWebservice {

    fun refreshToken(refreshToken: String): Completable

}