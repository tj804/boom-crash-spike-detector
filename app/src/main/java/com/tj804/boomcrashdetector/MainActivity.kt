package com.tj804.boomcrashdetector

import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var detector = SpikeDetector()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(30, 30, 30, 30)

        val title = TextView(this)
        title.text = "Boom 500 Spike Detector"
        title.textSize = 26f
        title.gravity = Gravity.CENTER
        title.setTextColor(Color.WHITE)

        val input = EditText(this)
        input.hint = "Enter Boom 500 price e.g. 5304.345"
        input.inputType = 8194
        input.setTextColor(Color.WHITE)
        input.setHintTextColor(Color.GRAY)

        val analyzeButton = Button(this)
        analyzeButton.text = "ANALYZE PRICE"

        val resetButton = Button(this)
        resetButton.text = "CLEAR / RESET"

        val status = TextView(this)
        status.text = "Enter a Boom 500 price"
        status.textSize = 22f
        status.gravity = Gravity.CENTER
        status.setPadding(10, 20, 10, 20)
        status.setTextColor(Color.WHITE)

        val countText = TextView(this)
        countText.text = "Prices collected: 0 / 20"
        countText.textSize = 16f
        countText.gravity = Gravity.CENTER
        countText.setTextColor(Color.LTGRAY)

        val averageText = TextView(this)
        averageText.text = "Average price: --"
        averageText.textSize = 18f
        averageText.gravity = Gravity.CENTER
        averageText.setTextColor(Color.LTGRAY)

        val historyText = TextView(this)
        historyText.text = "Recent prices:\n--"
        historyText.textSize = 17f
        historyText.setPadding(10, 20, 10, 20)
        historyText.setTextColor(Color.WHITE)

        layout.addView(title)
        layout.addView(input)
        layout.addView(analyzeButton)
        layout.addView(resetButton)
        layout.addView(status)
        layout.addView(countText)
        layout.addView(averageText)
        layout.addView(historyText)

        setContentView(layout)

        analyzeButton.setOnClickListener {

            val value = input.text.toString().trim().toDoubleOrNull()

            if (value == null || value <= 0) {
                status.text = "⚠️ Enter a valid Boom 500 price"
                status.setTextColor(Color.YELLOW)
                return@setOnClickListener
            }

            val result = detector.addPrice(value)

            status.text = result

            when {
                result.contains("STRONG UPWARD SPIKE") -> {
                    status.setTextColor(Color.RED)
                }

                result.contains("SPIKE CONDITIONS") -> {
                    status.setTextColor(Color.YELLOW)
                }

                result.contains("UPWARD MOVEMENT") -> {
                    status.setTextColor(Color.rgb(255, 165, 0))
                }

                result.contains("STRONG DOWNWARD") -> {
                    status.setTextColor(Color.BLUE)
                }

                else -> {
                    status.setTextColor(Color.GREEN)
                }
            }

            val history = detector.getHistory()

            countText.text = "Prices collected: ${history.size} / 20"

            if (history.isNotEmpty()) {
                val average = history.average()
                averageText.text =
                    "Average price: %.3f".format(average)

                val recent = history.takeLast(10)
                    .joinToString("\n") {
                        "%.3f".format(it)
                    }

                historyText.text = "Recent prices:\n$recent"
            }
        }

        resetButton.setOnClickListener {

            detector = SpikeDetector()

            input.text.clear()

            status.text = "Enter a Boom 500 price"
            status.setTextColor(Color.WHITE)

            countText.text = "Prices collected: 0 / 20"
            averageText.text = "Average price: --"
            historyText.text = "Recent prices:\n--"
        }
    }
}
