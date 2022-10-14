package com.android.template.data.remote.impl

import com.rx2androidnetworking.Rx2AndroidNetworking
import com.android.template.data.models.ProfileSettings
import com.android.template.data.models.api.model.ProfileModel
import com.android.template.data.models.api.request.ChangePasswordRequest
import com.android.template.data.remote.interfaces.ProfileWebservice
import com.android.template.utils.AppConstants
import com.android.template.utils.setToken
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ProfileWebserviceImpl @Inject constructor(authenticator: Authenticator) :
    BaseWebserviceImpl(authenticator),
    ProfileWebservice {

    override fun getProfileInfo(profileId: String): Single<ProfileModel> =
        authorizedSingle { token ->
            Rx2AndroidNetworking.get(AppConstants.ENDPOINT_GET_PROFILE)
                .addPathParameter("profileId", profileId)
                .addHeaders("Content-Type", "application/json")
                .setToken(token)
                .build()
                .getObjectSingle(ProfileModel::class.java)
        }

    override fun updateProfile(profileSettings: ProfileSettings): Completable =
        authorizedCompletable { token ->
            Rx2AndroidNetworking.put(AppConstants.ENDPOINT_UPDATE_PROFILE)
                .setToken(token)
                .addApplicationJsonBody(profileSettings)
                .build()
                .stringCompletable
        }

    override fun changePassword(
        changePasswordRequest: ChangePasswordRequest
    ): Completable = authorizedCompletable { token ->
        Rx2AndroidNetworking.put(AppConstants.ENDPOINT_CHANGE_PASSWORD)
            .setToken(token)
            .addApplicationJsonBody(changePasswordRequest)
            .build()
            .stringCompletable
    }

}