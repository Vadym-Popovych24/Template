package com.android.template.data.remote.interfaces

import com.android.template.data.models.api.request.DeviceLoginRequest
import com.android.template.data.models.api.request.ServerLoginRequest
import com.android.template.data.models.api.response.LoginResponse
import io.reactivex.Completable
import io.reactivex.Single

interface LoginWebservice {
    fun loginApiCall(username: String, password: String): Single<LoginResponse>

    //fun removeDeviceId(deviceId: String): Completable

    fun loginDevice(request: DeviceLoginRequest, token: String): Completable

    fun signUp(serverLoginRequest: ServerLoginRequest): Completable

    fun requestResetPasswordCode(email: String): Completable

    fun sendResetPasswordCode(email: String, code: String): Completable

    fun resetPassword(email: String, code: String, password: String): Single<LoginResponse>
}