package com.rahulraghuwanshi.mathjax

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import com.rahulraghuwanshi.math_jax.MathJaxView

/**
 * Created by timfreiheit on 30.05.15.
 */
class LaTexActivity : Activity(), View.OnClickListener {
    var mMathJaxView: MathJaxView? = null
    private var exampleIndex = 0
    private fun getExample(index: Int): String {

        val arr = listOf(
            "\\(361_{10}\\)\n" +
                    "\n" +
                    "   To convert 361 to binary, we perform successive divisions by 2 until the quotient becomes zero:\n" +
                    "\n" +
                    "   \\[\n" +
                    "   \\begin{align*}\n" +
                    "   361 \\div 2 &= 180 \\text{ (remainder 1)} \\\\\n" +
                    "   180 \\div 2 &= 90 \\text{ (remainder 0)} \\\\\n" +
                    "   90 \\div 2 &= 45 \\text{ (remainder 0)} \\\\\n" +
                    "   45 \\div 2 &= 22 \\text{ (remainder 1)} \\\\\n" +
                    "   22 \\div 2 &= 11 \\text{ (remainder 0)} \\\\\n" +
                    "   11 \\div 2 &= 5 \\text{ (remainder 1)} \\\\\n" +
                    "   5 \\div 2 &= 2 \\text{ (remainder 1)} \\\\\n" +
                    "   2 \\div 2 &= 1 \\text{ (remainder 0)} \\\\\n" +
                    "   1 \\div 2 &= 0 \\text{ (remainder 1)}\n" +
                    "   \\end{align*}\n" +
                    "   \\]\n" +
                    "\n" +
                    "   Reading the remainders from bottom to top, we get the binary representation: \\(101101001_2\\).",
            "\\[\n" +
                    "   \\begin{align*}\n" +
                    "   377 \\div 2 &= 188 \\text{ (remainder 1)} \\\\\n" +
                    "   188 \\div 2 &= 94 \\text{ (remainder 0)} \\\\\n" +
                    "   94 \\div 2 &= 47 \\text{ (remainder 0)} \\\\\n" +
                    "   47 \\div 2 &= 23 \\text{ (remainder 1)} \\\\\n" +
                    "   23 \\div 2 &= 11 \\text{ (remainder 1)} \\\\\n" +
                    "   11 \\div 2 &= 5 \\text{ (remainder 1)} \\\\\n" +
                    "   5 \\div 2 &= 2 \\text{ (remainder 1)} \\\\\n" +
                    "   2 \\div 2 &= 1 \\text{ (remainder 0)} \\\\\n" +
                    "   1 \\div 2 &= 0 \\text{ (remainder 1)}\n" +
                    "   \\end{align*}\n" +
                    "   \\]\n" +
                    "\n" +
                    "   Reading the remainders from bottom to top, we get \\(101111001_2\\). Therefore, \\(377_{10} = 101111001_2\\)."
        )
        return arr.toString()
    }

    override fun onClick(@NonNull v: View) {
        val id = v.id
        val e: EditText = findViewById<View>(R.id.edit) as EditText
        when (id) {
            R.id.showBtn -> {
                showMathJax(e.getText().toString())
            }

            R.id.clearBtn -> {
                e.setText("")
                showMathJax("")
            }

            R.id.exampleBtn -> {
                e.setText(getExample(exampleIndex++))
                if (exampleIndex > resources.getStringArray(R.array.tex_examples).size - 1) exampleIndex =
                    0
                showMathJax(e.getText().toString())
            }
        }
    }

    private fun showMathJax(value: String) {
        mMathJaxView?.setInputText(value)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        mMathJaxView = findViewById<View>(R.id.laTexView) as MathJaxView
        setupMathJaxListener()
        val e: EditText = findViewById<View>(R.id.edit) as EditText
        e.setBackgroundColor(Color.LTGRAY)
        e.setTextColor(Color.BLACK)
        e.setText("")
        findViewById<View>(R.id.showBtn).setOnClickListener(this)
        findViewById<View>(R.id.clearBtn).setOnClickListener(this)
        val exampleBtn = findViewById<View>(R.id.exampleBtn)
        exampleBtn.setOnClickListener(this)
        //initial load the first example
        onClick(exampleBtn)
        val t = findViewById<View>(R.id.textview3) as TextView
        t.movementMethod = LinkMovementMethod.getInstance()
        t.text = Html.fromHtml(t.getText().toString())
    }

    private fun setupMathJaxListener() {
        mMathJaxView?.setRenderListener(object : MathJaxView.OnMathJaxRenderListener {
            override fun onRendered() {
                showToast()
            }
        })
    }

    private fun showToast() {
        Toast.makeText(this, "Render complete", Toast.LENGTH_SHORT).show()
    }
}
