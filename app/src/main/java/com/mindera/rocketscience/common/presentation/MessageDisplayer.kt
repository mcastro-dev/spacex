package com.mindera.rocketscience.common.presentation

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

// matheus: Oh, this could be a lot fancier, right?
//  I hope it's enough for now :)
object MessageDisplayer {
    private const val MESSAGE_DURATION_IN_MILLIS = 6000
    private const val MESSAGE_MAX_LINES = 4

    fun show(error: Throwable, forView: View) {
        val message = ErrorMessageFactory.build(forError = error, withContext = forView.context)
        show(message, forView)
    }

    @SuppressLint("WrongConstant")
    fun show(message: String, forView: View) {
        val snackbar = Snackbar.make(forView, message, MESSAGE_DURATION_IN_MILLIS)
        val textView = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.maxLines = MESSAGE_MAX_LINES
        snackbar.show()
    }
}