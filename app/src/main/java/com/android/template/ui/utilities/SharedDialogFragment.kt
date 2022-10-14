package com.android.template.ui.utilities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.android.template.R

class SharedDialogFragment: DialogFragment() {

    /**
     * Creates new instance to open this fragment.
     *
     * @return new instance of SharedDialogFragment.
     */
    companion object {
        val TAG: String = SharedDialogFragment::class.java.simpleName

        private const val TITLE: String = "title"
        private const val BODY = "body"
        private const val OK = "ok"
        private const val CANCEL = "cancel"

        fun newInstance(body: Int, ok: Int, cancel: Int, title: Int = 0): SharedDialogFragment {
            val args = Bundle()
            args.putInt(TITLE, title)
            args.putInt(BODY, body)
            args.putInt(OK, ok)
            args.putInt(CANCEL, cancel)
            val fragment = SharedDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialog = AlertDialog.Builder(requireActivity())

        val bundle = this.arguments
        val title = bundle!!.getInt(TITLE)
        val body = bundle.getInt(BODY)
        val ok = bundle.getInt(OK)
        val cancel = bundle.getInt(CANCEL)

        val alertDialogMessage = TextView(activity)
        setStyleTextView(alertDialogMessage, 16f, requireContext().getString(body))
        alertDialogMessage.setPadding(24, 24, 24, 0)

        val builder = alertDialog.setView(alertDialogMessage)
            .setPositiveButton(ok) { _, _ ->
                sendResult(Activity.RESULT_OK)
                dismiss()
            }
            .setNegativeButton(cancel) { _, _ ->
                sendResult(Activity.RESULT_CANCELED)
                dismiss()
            }

        if(title > 0) {
            val titleDialog = TextView(activity)
            setStyleTextView(titleDialog, 18f, requireContext().getString(title))
            titleDialog.setPadding(16, 48, 16, 0)

            builder.setCustomTitle(titleDialog)
        }
        return builder.create()
    }

    private fun setStyleTextView(textView: TextView, textSize: Float, text: String){
//        textView.gravity = Gravity.CENTER
        textView.textSize = textSize
        textView.text = text
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
    }

    private fun sendResult(resultCode: Int) {
        targetFragment!!.onActivityResult(targetRequestCode, resultCode, Intent())
    }
}