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
        title.text = "Boom & Crash Spike Detector"
        title.textSize = 26f
        title.gravity = Gravity.CENTER
        title.setTextColor(Color.WHITE)

        val input = EditText(this)
        input.hint = "Enter multiplier e.g. 2.50"
        input.inputType = 2
        input.setTextColor(Color.WHITE)
        input.setHintTextColor(Color.GRAY)

        val analyzeButton = Button(this)
        analyzeButton.text = "ANALYZE"

        val resetButton = Button(this)
        resetButton.text = "CLEAR / RESET"

        val status = TextView(this)
        status.text = "Enter a multiplier"
        status.textSize = 22f
        status.gravity = Gravity.CENTER
        status.setPadding(10, 20, 10, 20)

        val countText = TextView(this)
        countText.text = "Results collected: 0 / 20"
        countText.textSize = 16f
        countText.gravity = Gravity.CENTER
        countText.setTextColor(Color.LTGRAY)

        val averageText = TextView(this)
        averageText.text = "Average: --"
        averageText.textSize = 18f
        averageText.gravity = Gravity.CENTER
        averageText.setTextColor(Color.LTGRAY)

        val historyText = TextView(this)
        historyText.text = "Recent results:\n--"
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
                status.text = "⚠️ Enter a valid multiplier"
                status.setTextColor(Color.YELLOW)
                return@setOnClickListener
            }

            val result = detector.addMultiplier(value)

            status.text = result

            when {
                result.contains("STRONG SPIKE") -> {
                    status.setTextColor(Color.RED)
                }

                result.contains("Possible Spike") -> {
                    status.setTextColor(Color.YELLOW)
                }

                result.contains("Strong Drop") -> {
                    status.setTextColor(Color.BLUE)
                }

                else -> {
                    status.setTextColor(Color.GREEN)
                }
            }

            val history = detector.getHistory()

            countText.text = "Results collected: ${history.size} / 20"

            if (history.isNotEmpty()) {
                val average = history.average()
                averageText.text = "Average: %.2f".format(average)

                val recent = history.takeLast(10).joinToString("  ")
                historyText.text = "Recent results:\n$recent"
            }
        }

        resetButton.setOnClickListener {

            detector = SpikeDetector()

            input.text.clear()

            status.text = "Enter a multiplier"
            status.setTextColor(Color.WHITE)

            countText.text = "Results collected: 0 / 20"
            averageText.text = "Average: --"
            historyText.text = "Recent results:\n--"
        }
    }
}
