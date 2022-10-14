package com.android.template.ui.profile.avatar

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.android.template.R
import com.android.template.databinding.FragmentChangeAvatarBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.profile.avatar.viewmodel.ChangeAvatarViewModel
import kotlinx.android.synthetic.main.fragment_change_avatar.*
import java.io.File

class ChangeAvatarFragment : BaseFragment<FragmentChangeAvatarBinding, ChangeAvatarViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.initUpNavigation()
        toolbar?.inflateMenu(R.menu.menu_confirm_blue)
        toolbar?.setOnMenuItemClickListener {
            if (it.itemId == R.id.confirm) {
                confirmCropAndUploadAvatar()
                return@setOnMenuItemClickListener true
            }
            false
        }
        arguments?.getString(EXTRA_IMAGE_PATH)?.let {
            cropImageView?.loadAsCompletable(Uri.fromFile(File(it)))?.let { completable ->
                viewModel.makeRx(completable)
            }
        }
    }

    private fun confirmCropAndUploadAvatar() = cropImageView?.cropAsSingle()?.let {
        val resumeId = arguments?.getString(EXTRA_RESUME_ID)
        if (resumeId.isNullOrEmpty()) {
            viewModel.changeAvatar(it) {
                viewModel.updateProfileDate()
                navigateUp()
            }
        }else{
          //  viewModel.changeResumeAvatar(it, resumeId) {
                navigateUp()
        //    }
        }
    }

    companion object {

        private const val EXTRA_IMAGE_PATH = "EXTRA_IMAGE_PATH"
        private const val EXTRA_RESUME_ID = "EXTRA_RESUME_ID"

        fun initArgumentsBundle(imagePath: String, resumeId: String? = null) = Bundle().apply {
            putString(EXTRA_IMAGE_PATH, imagePath)
            putString(EXTRA_RESUME_ID, resumeId)
        }

    }

}