package com.android.template.data.remote.interfaces

import com.android.template.data.models.api.model.AccountWithSession
import com.android.template.data.models.api.model.ProfileModel
import io.reactivex.rxjava3.core.Single

interface ProfileWebservice {
    fun getAccount(sessionId: String): Single<AccountWithSession>
    fun getProfileInfo(profileId: String): Single<ProfileModel>
}