package com.rahulraghuwanshi.math_jax

import android.content.res.TypedArray
import android.os.Build
import android.webkit.JavascriptInterface

/**
 * http://docs.mathjax.org/en/latest/options/
 * for more information
 *
 * Created by timfreiheit on 06.06.15.
 */
class MathJaxConfig() {
    enum class Output(var value: String) {
        SVG("output/SVG"),
        HTML_CSS("output/HTML-CSS"),
        CommonHTML("output/CommonHTML"),
        NativeMML("output/NativeMML")
    }

    enum class Input(var value: String) {
        TeX("input/TeX"),
        MathML("input/MathML"),
        AsciiMath("input/AsciiMath")
    }

    @get:JavascriptInterface
    var input = Input.TeX.value
        private set

    @get:JavascriptInterface
    var output = Output.SVG.value
        private set

    @get:JavascriptInterface
    var outputScale = 100

    @get:JavascriptInterface
    var minScaleAdjust = 100

    @get:JavascriptInterface
    var automaticLinebreaks = false

    @get:JavascriptInterface
    var blacker = 1

    init {
        output = if (Build.VERSION.SDK_INT >= 14) {
            Output.SVG.value
        } else {
            Output.HTML_CSS.value
        }
    }

    constructor(attrs: TypedArray) : this() {
        val inputIndex = attrs.getInteger(R.styleable.MathJaxView_input, -1)
        if (inputIndex > 0) {
            setInput(Input.entries[inputIndex])
        }
        val outputIndex = attrs.getInteger(R.styleable.MathJaxView_output, -1)
        if (outputIndex > 0) {
            setOutput(Output.entries[outputIndex])
        }
        automaticLinebreaks =
            attrs.getBoolean(R.styleable.MathJaxView_automaticLinebreaks, automaticLinebreaks)
        minScaleAdjust = attrs.getInteger(R.styleable.MathJaxView_minScaleAdjust, minScaleAdjust)
        outputScale = attrs.getInteger(R.styleable.MathJaxView_outputScale, outputScale)
        blacker = attrs.getInteger(R.styleable.MathJaxView_blacker, blacker)
    }

    fun setInput(input: Input) {
        this.input = input.value
    }

    fun setOutput(output: Output) {
        this.output = output.value
    }
}
