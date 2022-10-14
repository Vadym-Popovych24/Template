package com.android.template.ui.profile.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import com.nguyenhoanglam.imagepicker.model.Image
import com.android.template.data.models.db.ProfileAndAvatar
import com.android.template.manager.interfaces.ProfileManager
import com.android.template.ui.base.BaseViewModel
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val profileManager: ProfileManager
) : BaseViewModel() {
    val userName = ObservableField<String>()
    val userEmail = ObservableField<String>()
    val userAvatar = ObservableField<String>()
    val userCover = ObservableField<String>()
    val userPosition = ObservableField<String>()

    fun getProfile(): LiveData<ProfileAndAvatar> {
        makeRx(profileManager.getProfileAPI()){
        }
        return profileManager.getProfile()
    }

    fun changeCover(image: Image) {} //= makeRx(profileManager.uploadCover(image.path)){
     //   userCover.set(it)
 //   }

    fun initData(data: ProfileAndAvatar?) {
      /*  data?.let {
            userName.set("${it.post?.firstName} ${it.post?.lastName}")
            userEmail.set(it.post?.email)
            userPosition.set(it.post?.position)
            userAvatar.set(it.postPictures?.lastOrNull()?.picture)
            userCover.set(it.post?.coverPicture)
        }*/
    }

}