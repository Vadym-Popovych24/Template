package com.android.template.ui.profile

import android.os.Bundle
import android.view.View
import com.android.template.R
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.android.template.data.models.enums.ChangeImageType
import com.android.template.databinding.FragmentProfileBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.profile.viewmodel.ProfileViewModel
import com.android.template.utils.BindingUtils
import com.android.template.utils.custom.ImageViewerDialog
import com.android.template.utils.getStringFromResource
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
                BindingUtils.setUserImageUrl(imgAvatar, profile.profileEntity?.avatarPath)
                BindingUtils.setLoadImageUrlWithoutPlaceholder(imageCover, profile.profileEntity?.coverPath)
                tvFirstLastName.text = firstLastName
                tvEmail.text = profile.profileEntity?.email
                tvPhone.text = profile.profileEntity?.phoneNumber
                tvPhone.isVisible = profile.profileEntity?.phoneNumber.isNullOrEmpty().not()
                tvRequestToken.text = profile.profileEntity?.requestToken
                tvSessionId.text = profile.profileEntity?.sessionId
                tvUserNameMovie.text = profile.profileEntity?.userName
                imgAvatar.setOnClickListener {
                    ImageViewerDialog.newInstance(profile.profileEntity?.avatarPath?:"", R.string.profile.getStringFromResource)
                        .show(parentFragmentManager, "image_viewer_avatar")
                }
                imageCover.setOnClickListener {
                    profile.profileEntity?.coverPath?.let { coverPath ->
                        ImageViewerDialog.newInstance(coverPath, R.string.profile_cover.getStringFromResource)
                            .show(parentFragmentManager, "image_viewer_cover")
                    }
                }
            }
        }
    }

    private fun moveToUploadAvatar(imagePath: String) = imagePath.let {
        ProfileFragmentDirections.actionNavigationProfileToChangeImage(
            imagePath = imagePath,
            type = viewModel.getChangeAvatarType().code
        ).run {
            findNavController().navigate(this)
        }
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