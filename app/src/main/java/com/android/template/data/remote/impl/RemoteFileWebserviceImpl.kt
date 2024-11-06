package com.android.template.data.remote.impl

import com.rx2androidnetworking.Rx2AndroidNetworking
import com.android.template.data.models.api.response.UploadAvatarResponse
import com.android.template.data.remote.interfaces.RemoteFileWebservice
import com.android.template.utils.AppConstants
import io.reactivex.Single
import okhttp3.OkHttpClient
import java.io.File
import javax.inject.Inject

class RemoteFileWebserviceImpl @Inject constructor(private val okHttpClient: OkHttpClient) : RemoteFileWebservice {

    override fun uploadAvatar(filePath: String): Single<String> =
        Rx2AndroidNetworking.upload(AppConstants.ENDPOINT_PROFILE_PICTURE)
            .addMultipartFile("picture", File(filePath))
            .setOkHttpClient(okHttpClient)
            .build()
            .getObjectSingle(UploadAvatarResponse::class.java)
            .map {
                it.imageUrl
            }

}