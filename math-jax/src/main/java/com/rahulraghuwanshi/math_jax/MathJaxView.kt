package com.rahulraghuwanshi.math_jax

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.os.Handler
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout

/**
 * Containerview for an WebView which renders LaTex using MathJax
 * https://github.com/mathjax/MathJax
 * Created by timfreiheit on 26.05.15.
 */
class MathJaxView : FrameLayout {
    private var inputText: String? = null
    private lateinit var mWebView: WebView
    private val handler = Handler()
    protected lateinit var mBridge: MathJaxJavaScriptBridge

    /**
     * laTex can only be rendered when WebView is already loaded
     */
    private var webViewLoaded = false

    interface OnMathJaxRenderListener {
        fun onRendered()
    }

    private var onMathJaxRenderListener: OnMathJaxRenderListener? = null

    constructor(context: Context) : super(context) {
        init(context, null, null)
    }

    constructor(context: Context, config: MathJaxConfig?) : super(context) {
        init(context, null, config)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, null)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, null)
    }

    fun setRenderListener(onMathJaxRenderListener: OnMathJaxRenderListener?) {
        this.onMathJaxRenderListener = onMathJaxRenderListener
    }

    @SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
    private fun init(context: Context, attrSet: AttributeSet?, config: MathJaxConfig?) {
        var config = config
        mWebView = WebView(context)
        var gravity: Int = Gravity.CENTER
        var verticalScrollbarsEnabled = false
        var horizontalScrollbarsEnabled = false
        if (attrSet != null) {
            val attrs: TypedArray = context.obtainStyledAttributes(attrSet, R.styleable.MathJaxView)
            gravity = attrs.getInteger(R.styleable.MathJaxView_android_gravity, Gravity.CENTER)
            verticalScrollbarsEnabled =
                attrs.getBoolean(R.styleable.MathJaxView_verticalScrollbarsEnabled, false)
            horizontalScrollbarsEnabled =
                attrs.getBoolean(R.styleable.MathJaxView_horizontalScrollbarsEnabled, false)
            config = MathJaxConfig(attrs)
            attrs.recycle()
        }
        if (config == null) {
            config = MathJaxConfig()
        }
        addView(
            mWebView,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                gravity
            )
        )

        // callback when WebView is loading completed
        webViewLoaded = false
        mWebView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (webViewLoaded) {
                    // WebView was already finished
                    // do not load content again
                    return
                }
                webViewLoaded = true
                if (!TextUtils.isEmpty(inputText)) {
                    setInputText(inputText)
                }
            }
        })
        mBridge = MathJaxJavaScriptBridge(this)
        mWebView.addJavascriptInterface(mBridge, "Bridge")
        mWebView.addJavascriptInterface(config, "BridgeConfig")

        // be careful, we do not need internet access
        mWebView.getSettings().setBlockNetworkLoads(true)
        mWebView.getSettings().setJavaScriptEnabled(true)
        mWebView.loadUrl(HTML_LOCATION)
        mWebView.setVerticalScrollBarEnabled(verticalScrollbarsEnabled)
        mWebView.setHorizontalScrollBarEnabled(horizontalScrollbarsEnabled)
        mWebView.setBackgroundColor(0)
    }

    /**
     * called when webView is ready with rendering LaTex
     */
    fun rendered() {
        handler.postDelayed({
            mWebView.setVisibility(View.VISIBLE)
            if (onMathJaxRenderListener != null) onMathJaxRenderListener!!.onRendered()
        }, 100)
    }

    /**
     * change the displayed LaTex
     *
     * @param inputText formatted string
     */
    fun setInputText(inputText: String?) {
        this.inputText = inputText

        //wait for WebView to finish loading
        if (!webViewLoaded) {
            return
        }
        val laTexString: String
        laTexString = inputText?.let { doubleEscapeTeX(it) } ?: ""
        mWebView.setVisibility(View.INVISIBLE)
        val javascriptCommand = "javascript:changeLatexText(\"$laTexString\")"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.evaluateJavascript(javascriptCommand, null)
        } else {
            mWebView.loadUrl(javascriptCommand)
        }
    }

    /**
     * @return the current laTex-String
     * null if not set
     */
    fun getInputText(): String? {
        return inputText
    }

    private fun doubleEscapeTeX(s: String): String {
        var t = ""
        for (i in s.indices) {
            if (s[i] == '\'') t += '\\'
            if (s[i] == '\\') t += "\\"
            if (s[i] != '\n') t += s[i]
        }
        return t
    }

    companion object {
        private const val HTML_LOCATION =
            "file:///android_asset/MathJaxAndroid/mathjax_android.html"
    }
}
