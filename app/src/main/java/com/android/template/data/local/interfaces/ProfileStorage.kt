package com.android.template.data.local.interfaces

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagingSource
import com.android.template.data.models.ProfileSettings
import com.android.template.data.models.api.model.ProfileModel
import com.android.template.data.models.db.ContactRequest
import com.android.template.data.models.db.ProfileAndAvatar
import io.reactivex.Completable

interface ProfileStorage {
    fun saveProfile(model: ProfileSettings)
    fun getProfileById(profileId: Int): LiveData<ProfileAndAvatar>
    /*fun saveContactRequests(contactRequests: List<ContactRequest>)
    fun getContactRequests(): PagingSource<Int, ContactRequest>
    fun clearContactRequests()
    fun updateStatus(contactRequestId: Int, status: Int): Completable
    fun saveCompanyImg(companyId: Int, companyImg: String): Completable*/
}