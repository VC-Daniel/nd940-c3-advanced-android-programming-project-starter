package com.notificationsample

import android.widget.TextView
import androidx.databinding.BindingAdapter

/*
 * Set the appropriate text and text color depending on if the download was successful [downloadSuccessStatus]
 */
@BindingAdapter("downloadSuccessStatus")
fun bindDetailsStatus(textView: TextView, wasSuccessful: Boolean) {
    val context = textView.context
    if (wasSuccessful) {
        textView.text = context.getString(R.string.download_success_Description)
        textView.setTextColor(context.getColor(R.color.colorPrimaryDark))
    } else {
        textView.text = context.getString(R.string.download_failure_Description)
        textView.setTextColor(context.getColor(R.color.colorAlertAccent))
    }
}