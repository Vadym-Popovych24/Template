package com.android.template.ui.avatar

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.navigation.fragment.navArgs
import com.android.template.R
import com.android.template.data.models.enums.ChangeImageType
import com.android.template.databinding.FragmentChangeAvatarBinding
import com.android.template.ui.avatar.viewmodel.ChangeImageViewModel
import com.android.template.ui.base.BaseFragment
import com.android.template.utils.bitmapToUri
import com.canhub.cropper.CropImageView
import java.io.File
import java.io.FileOutputStream
import kotlin.getValue

class ChangeImageFragment : BaseFragment<FragmentChangeAvatarBinding, ChangeImageViewModel>() {

    private val args by navArgs<ChangeImageFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imagePath: String = args.imagePath
        val imageType: ChangeImageType = ChangeImageType.getChangeImageType(args.type)
        viewModel.setChangeAvatarType(imageType)
        when (imageType) {
            ChangeImageType.AVATAR -> {
                binding.cropImageView.apply {
                    cropShape = CropImageView.CropShape.OVAL
                    setFixedAspectRatio(true)
                }
            }
            ChangeImageType.COVER -> {
                binding.cropImageView.apply {
                    cropShape = CropImageView.CropShape.RECTANGLE_HORIZONTAL_ONLY
                    setFixedAspectRatio(false)
                }
            }

            else -> binding.cropImageView.cropShape = CropImageView.CropShape.OVAL
        }

        val uri = Uri.fromFile(File(imagePath))
        binding.toolbar.initUpNavigation()
        binding.toolbar.inflateMenu(R.menu.menu_confirm_blue)
        binding.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.confirm) {

                binding.cropImageView.getCroppedImage()?.let { bitmap -> bitmapToUri(requireContext(), bitmap) }
                    ?.let { saveImageLocally(it) }
                return@setOnMenuItemClickListener true
            }
            false
        }
        binding.cropImageView.setImageUriAsync(uri)


    }

    private fun saveImageLocally(uri: Uri) {
        val fileName = when (viewModel.getChangeAvatarType()) {
            ChangeImageType.AVATAR -> "avatar_${System.currentTimeMillis()}.jpg"
            ChangeImageType.COVER -> "cover_${System.currentTimeMillis()}.jpg"
            else -> "unspecified.jpg"
        }
        val outputDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val outputFile = File(outputDir, fileName)

        try {
            requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(outputFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            // Image saved successfully
            showToast(R.string.image_saved)
            when (viewModel.getChangeAvatarType()) {
                ChangeImageType.AVATAR -> viewModel.updateAvatar(outputFile.absolutePath)
                ChangeImageType.COVER -> viewModel.updateCover(outputFile.absolutePath)
                else -> { /*nothing*/ }
            }
            navigateUp()
        } catch (e: Exception) {
            e.printStackTrace()
            showToast(R.string.image_save_failed)
        }
    }

}