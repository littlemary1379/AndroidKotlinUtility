package com.mary.utility

import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

interface OnKeyboardVisibilityListener {
    fun onVisibilityChanged(visible: Boolean)
}

object KeyboardListener {

    private val TAG = "KeyboardListener"

    fun setKeyboardVisibilityListener(view : View, onKeyboardVisibilityListener: OnKeyboardVisibilityListener) {
        val parentView: View = view
        parentView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            private var alreadyOpen = false
            private val defaultKeyboardHeightDP = 100
            private val EstimatedKeyboardDP =
                defaultKeyboardHeightDP + if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 48 else 0
            private val rect: Rect = Rect()
            override fun onGlobalLayout() {
                val estimatedKeyboardHeight = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    EstimatedKeyboardDP.toFloat(),
                    parentView.getResources().getDisplayMetrics()
                ).toInt()
                parentView.getWindowVisibleDisplayFrame(rect)
                val heightDiff: Int =
                    parentView.getRootView().getHeight() - (rect.bottom - rect.top)
                val isShown = heightDiff >= estimatedKeyboardHeight
                if (isShown == alreadyOpen) {
                    Log.i(TAG, "Ignoring global layout change...")
                    return
                }
                alreadyOpen = isShown
                onKeyboardVisibilityListener.onVisibilityChanged(isShown)
            }
        })
    }
}