package com.android.template.data.remote.impl

import com.android.template.data.remote.interfaces.RemoteFileWebservice
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RemoteFileWebserviceImpl @Inject constructor() : RemoteFileWebservice {

    override fun uploadAvatar(filePath: String): Single<String> =
        TODO("Not yet implemented")
}