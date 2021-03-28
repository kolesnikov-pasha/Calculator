package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.udojava.evalex.Expression

class MainActivity : AppCompatActivity() {
    private val textButtonsIds = listOf(
        R.id.b00,
        R.id.b0,
        R.id.b1,
        R.id.b2,
        R.id.b3,
        R.id.b4,
        R.id.b5,
        R.id.b6,
        R.id.b7,
        R.id.b8,
        R.id.b9,
        R.id.comma,
        R.id.plus,
        R.id.minus,
        R.id.multiply,
        R.id.divide,
        R.id.right_brace,
        R.id.left_brace
    )

    private lateinit var expressionText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        expressionText = findViewById(R.id.expression)
        textButtonsIds.forEach {
            findViewById<View>(it).setOnClickListener(this::onTextClick)
        }
        findViewById<View>(R.id.equals).setOnClickListener { onEqualClick() }
        findViewById<View>(R.id.backspace).setOnClickListener { onBackspaceClick() }
    }

    private fun onBackspaceClick() {
        val currentText = expressionText.text.toString()
        if (currentText.length <= 1) {
            expressionText.text = ZERO
        } else {
            expressionText.text = currentText.subSequence(0, currentText.length - 1)
        }
    }

    private fun onEqualClick() {
        val currentText = expressionText.text.toString()
        if (currentText.isEmpty() || currentText == ERROR) {
            expressionText.text = ZERO
        } else {
            expressionText.text = calculateExpression(currentText)
        }
    }

    private fun calculateExpression(expression: String): String {
        val exp = Expression(
            expression
                .replace('÷', '/')
                .replace('×', '*')
                .replace(',', '.')
        )
        try {
            return reformatStyle(exp.eval().toPlainString())
        } catch (e: Exception) {
            return ERROR
        }
    }

    private fun reformatStyle(result: String): String {
        return result.replace('.', ',')
    }

    private fun onTextClick(v: View) {
        if (textButtonsIds.contains(v.id)) {
            expressionText.text = typeSymbol(
                expressionText.text.toString(),
                (v as TextView).text.toString()
            )
        }
    }

    private fun typeSymbol(currentText: String, symbol: String): String {
        if (currentText == ERROR || currentText == ZERO) {
            return if (symbol == DOUBLE_ZERO) ZERO else symbol
        }
        return currentText + symbol
    }

    private companion object {
        const val ERROR = "ERR"
        const val ZERO = "0"
        const val DOUBLE_ZERO = "00"
    }
}