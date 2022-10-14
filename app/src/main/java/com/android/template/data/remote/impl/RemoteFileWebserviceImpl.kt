package com.android.template.data.remote.impl

import com.rx2androidnetworking.Rx2AndroidNetworking
import com.android.template.data.models.api.response.UploadAvatarResponse
import com.android.template.data.remote.interfaces.RemoteFileWebservice
import com.android.template.utils.AppConstants
import com.android.template.utils.setToken
import io.reactivex.Single
import okhttp3.OkHttpClient
import java.io.File
import javax.inject.Inject

class RemoteFileWebserviceImpl @Inject constructor(
    private val okHttpClient: OkHttpClient,
    authenticator: Authenticator
) :
    BaseWebserviceImpl(authenticator), RemoteFileWebservice {

    override fun uploadAvatar(filePath: String): Single<String> = authorizedSingle { token ->
        Rx2AndroidNetworking.upload(AppConstants.ENDPOINT_PROFILE_PICTURE)
            .setToken(token)
            .addMultipartFile("picture", File(filePath))
            .setOkHttpClient(okHttpClient)
            .build()
            .getObjectSingle(UploadAvatarResponse::class.java)
            .map {
                it.imageUrl
            }
    }


}