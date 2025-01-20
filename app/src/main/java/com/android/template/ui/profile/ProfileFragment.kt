package com.android.template.ui.profile

import android.os.Bundle
import android.view.View
import com.android.template.R
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.android.template.data.models.api.model.FullImage
import com.android.template.data.models.enums.ChangeImageType
import com.android.template.databinding.FragmentProfileBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.avatar.ChangeImageFragment
import com.android.template.ui.profile.viewmodel.ProfileViewModel
import com.android.template.utils.BindingUtils
import com.android.template.utils.getStringFromResource
import com.android.template.utils.custom.openViewer
import com.android.template.utils.writeFileContent

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.initUpNavigation()

        binding.imgChangeAvatar.setOnClickListener {
            viewModel.setChangeAvatarType(ChangeImageType.AVATAR)
            openImagePicker()
        }
        binding.imgChangeCover.setOnClickListener {
            viewModel.setChangeAvatarType(ChangeImageType.COVER)
            openImagePicker()
        }
        viewModel.getProfile().observe(viewLifecycleOwner) { profile ->
            binding.apply {
                val firstLastName = "${profile.profileEntity?.firstName} ${profile.profileEntity?.lastName}"
                BindingUtils.setLoadImageUrl(imgAvatar, profile.profileEntity?.avatarPath)
                BindingUtils.setLoadImageUrlWithoutPlaceholder(imageCover, profile.profileEntity?.coverPath)
                tvFirstLastName.text = firstLastName
                tvEmail.text = profile.profileEntity?.email
                tvPhone.text = profile.profileEntity?.phoneNumber
                tvPhone.isVisible = profile.profileEntity?.phoneNumber.isNullOrEmpty().not()
                tvRequestToken.text = profile.profileEntity?.requestToken
                tvSessionId.text = profile.profileEntity?.sessionId
                tvUserNameMovie.text = profile.profileEntity?.userName
                imgAvatar.setOnClickListener {
                    openViewer(
                        listOf(FullImage(profile.profileEntity?.avatarPath?:"", R.string.profile.getStringFromResource)),
                        imgAvatar,
                        0
                    )
                }
                imageCover.setOnClickListener {
                    profile.profileEntity?.coverPath?.let { coverPath ->
                        openViewer(
                            listOf(FullImage(coverPath, R.string.profile.getStringFromResource)),
                            imgAvatar,
                            0
                        )
                    }
                }
            }
        }
    }

    private fun moveToUploadAvatar(imagePath: String) = imagePath.let {
        showFragment(ChangeImageFragment.initArgumentsBundle(imagePath, viewModel.getChangeAvatarType().code))
    }

    private val permissionLauncherSingle = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ){ uri ->

        uri?.writeFileContent(requireContext())?.let {
            moveToUploadAvatar(it)
        }
    }

    private fun openImagePicker() {
        permissionLauncherSingle.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    companion object {

        fun newInstance() = ProfileFragment()
    }
}