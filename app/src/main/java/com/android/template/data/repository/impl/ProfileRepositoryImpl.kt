package com.android.template.data.repository.impl

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.android.template.data.local.interfaces.ProfileStorage
import com.android.template.data.models.ProfileSettings
import com.android.template.data.models.api.model.CoverPicture
import com.android.template.data.models.api.model.ProfileMenuModel
import com.android.template.data.models.api.model.ProfileModel
import com.android.template.data.models.api.request.ChangePasswordRequest
import com.android.template.data.models.db.ProfileAndAvatar
import com.android.template.data.prefs.PreferencesHelper
import com.android.template.data.remote.interfaces.LoginWebservice
import com.android.template.data.remote.interfaces.ProfileWebservice
import com.android.template.data.remote.interfaces.RemoteFileWebservice
import com.android.template.data.repository.interfaces.ProfileRepository
import io.reactivex.Completable
import io.reactivex.Single
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileWebservice: ProfileWebservice,
    private val storage: ProfileStorage,
    private val preferences: PreferencesHelper,
    private val remoteFileWebservice: RemoteFileWebservice,
    private val loginWebservice: LoginWebservice,
    private val cacheDir: File
) : BaseRepositoryImpl(), ProfileRepository {

    override fun getProfileAPI(): Unit {
        val id = preferences.getProfileId()
        var profile: ProfileModel? = null
        var altAttribute: String? = null
    }

    override fun getProfile(): LiveData<ProfileAndAvatar> {
        val id = preferences.getProfileId()
        return storage.getProfileById(id)
    }

    override fun getProfileSettings(): Single<ProfileSettings> =
        profileWebservice.getProfileInfo(preferences.getProfileId().toString()).map {
            preferences.setLanguageCode(it.culture)
            ProfileSettings(
                it.firstName,
                it.lastName,
                it.birthday ?: "",
                it.email,
                it.phoneNumber,
                it.userName,
                it.gender,
                it.culture,
                preferences.getProfileId()
            )
        }

    override fun updateProfile(profileSettings: ProfileSettings): Completable =
     //   profileWebservice.updateProfile(profileSettings).doOnComplete {
            Completable.fromAction {
                storage.saveProfile(profileSettings)
                preferences.setLanguageCode(profileSettings.culture)
                preferences.setUserName(profileSettings.userName)
                preferences.setEmail(profileSettings.email)
            }
         //   preferences.setLanguageCode(profileSettings.culture)
   //     }

    override fun changePassword(changePasswordRequest: ChangePasswordRequest): Completable =
        profileWebservice.changePassword(changePasswordRequest)

    override fun uploadAvatar(bitmap: Bitmap): Single<String> {
        val imageFile = File(cacheDir, "cached_avatar.jpg")
        return writeAvatarToCache(imageFile, bitmap).flatMap {
            remoteFileWebservice.uploadAvatar(it)
        }.map {
            preferences.setUserAvatar(it)
            imageFile.delete()
            it
        }.doOnSuccess {
            imageFile.delete()
        }.doOnError {
            imageFile.delete()
        }
    }

    override fun getProfileHeader(): Single<ProfileMenuModel> = Single.just(preferences).map {
        ProfileMenuModel(
            it.getEmail().toString(),
            it.getUserAvatar().toString(),
            it.getUserName().toString()
        )
    }

    private fun writeAvatarToCache(imageFile: File, bitmap: Bitmap): Single<String> =
        Single.just(bitmap).map {
            val os = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()
            imageFile.path
        }

    override fun logout(): Completable = Completable.complete()

/*    @OptIn(ExperimentalPagingApi::class)
    override fun contactRequestPagingData(): Pager<Int, ContactRequest> = Pager(
        config = PagingConfig(pageSize = AppConstants.GetContactRequestsCount),
        remoteMediator = RxPagingRemoteMediator(getSourceHandler()),
        pagingSourceFactory = { storage.getContactRequests() }
    )*/

/*    private fun getSourceHandler() = object : RxPagingRemoteMediator.SourceHandler<ContactRequest> {
        override fun loadNextBunch(pageNumber: Int): Single<List<ContactRequest>> {
            val request = GetContactsRequest().apply {
                page = pageNumber
                profileId = preferences.getProfileId().toString()
            }

            return profileWebservice.getContactRequests(request)
        }

    //    override fun clearDatabase() = storage.clearContactRequests()

        override fun insertAll(items: List<ContactRequest>) {
            // TODO need remove page==1 and load all pages but web don't have
            //  page and size parameters for this request!
//            if (getContactsRequest.page == 1) {
//                storage.saveContactRequests(it)
//            }

       //     storage.saveContactRequests(items)
        }
    }*/
}