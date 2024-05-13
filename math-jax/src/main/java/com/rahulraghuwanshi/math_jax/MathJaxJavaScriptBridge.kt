package com.rahulraghuwanshi.math_jax

import android.webkit.JavascriptInterface

/**
 * Bridge to enable callbacks for MathJax
 * Wrapped MathJaxView to disable global visibility of [MathJaxView.laTexRendered]
 *
 * Created by timfreiheit on 30.05.15.
 */
class MathJaxJavaScriptBridge(var mOwner: MathJaxView) {
    @JavascriptInterface
    fun rendered() {
        mOwner.rendered()
    }
}
