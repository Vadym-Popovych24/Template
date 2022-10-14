package com.android.template.utils

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import androidx.fragment.app.Fragment
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.android.template.R

class ImagePicker {
    companion object {

        fun callCamera(fragment: Fragment, requestCode: Int){
            showOnlyCamera(requestCode, ImagePicker.with(fragment))
        }

      private fun showOnlyCamera(requestCode: Int, builder: ImagePicker.Builder){
            builder
                .setCameraOnly(true)
                .setSavePath("ImagePicker")         //  Image capture folder name
                .setRequestCode(
                    if (requestCode == 0) {
                        Config.RC_PICK_IMAGES
                    } else {
                        requestCode
                    }
                )                 //  Keep screen on when selecting images
                .start()
        }

        fun callInMultipleMode(fragment: Fragment, maxCount: Int) {
            call(ImagePicker.with(fragment), fragment.resources, true, maxCount, 0)
        }

        fun callInSingleMode(activity: Activity, requestCode: Int) {
            call(ImagePicker.with(activity), activity.resources, false, 1, requestCode)
        }

        fun callInSingleMode(fragment: Fragment, requestCode: Int) {
            call(ImagePicker.with(fragment), fragment.resources, false, 1, requestCode)
        }

        fun call(
            builder: ImagePicker.Builder,
            resources: Resources,
            multipleMode: Boolean,
            maxCount: Int,
            requestCode: Int
        ) {
            val colorPrimary = resources.getColor(R.color.colorPrimary).toHex()
            val colorPrimaryDark = resources.getColor(R.color.colorPrimaryDark).toHex()
            val white = resources.getColor(android.R.color.white).toHex()

            builder
                //  Initialize ImagePicker with activity or fragment context
                .setToolbarColor(colorPrimary)         //  Toolbar color
                .setStatusBarColor(colorPrimaryDark)       //  StatusBar color (works with SDK >= 21  )
                .setToolbarTextColor(white)     //  Toolbar text color (Title and Done button)
                .setToolbarIconColor(white)     //  Toolbar icon color (Back and Camera button)
                .setProgressBarColor(colorPrimary)     //  ProgressBar color
                .setBackgroundColor(white)      //  Background color
                .setMultipleMode(multipleMode)
                .setMaxSize(maxCount)//  Select multiple images or single image
                .setFolderMode(true)                //  Folder mode
                .setShowCamera(true)                //  Show camera button
                .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                .setDoneTitle("Done")                          //  Max images can be selected
                .setSavePath("ImagePicker")         //  Image capture folder name
                .setRequestCode(
                    if (requestCode == 0) {
                        Config.RC_PICK_IMAGES
                    } else {
                        requestCode
                    }
                )                 //  Keep screen on when selecting images
                .start()
        }

        fun handleOnActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?,
            callback: (images: ArrayList<Image>) -> Unit
        ): Boolean {
            return if (requestCode == Config.RC_PICK_IMAGES && resultCode == Activity.RESULT_OK && data != null) {
                val images = data.getParcelableArrayListExtra<Image>(Config.EXTRA_IMAGES)
                images?.let {
                    callback(images)
                }
                true
            } else
                false
        }
    }
}