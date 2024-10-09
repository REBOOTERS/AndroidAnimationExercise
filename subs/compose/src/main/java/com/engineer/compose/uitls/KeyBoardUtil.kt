package com.engineer.compose.uitls

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyBoardUtil {
    fun showKeyboard(view: View) {
        view.requestFocus()
        val manager = provideInputMethodManager(view)
        manager?.showSoftInput(view, 0)
    }

    fun hideKeyboard(view: View) {
        view.clearFocus()
        val manager = provideInputMethodManager(view)
        manager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun provideInputMethodManager(view: View): InputMethodManager? {
        return view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    }
}