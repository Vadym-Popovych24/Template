package com.android.template.data.remote.interfaces


import io.reactivex.Single

interface RemoteFileWebservice {

    fun uploadAvatar(filePath: String): Single<String>

}