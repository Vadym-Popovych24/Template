package com.android.template.data.remote.interfaces

import io.reactivex.rxjava3.core.Single

interface RemoteFileWebservice {

    fun uploadAvatar(filePath: String): Single<String>

}