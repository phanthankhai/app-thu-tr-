package com.example.rent.core.ui

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object UiFeedback {
    fun showSnackbar(view: View, message: CharSequence) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    fun showShortMessage(view: View, message: CharSequence) {
        showSnackbar(view, message)
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun clearErrorOnTextChange(
        inputLayout: TextInputLayout,
        editText: TextInputEditText,
    ) {
        editText.doAfterTextChanged {
            inputLayout.error = null
        }
    }
}
