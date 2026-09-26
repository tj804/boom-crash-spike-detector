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

    private val detector = SpikeDetector()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setPadding(32, 32, 32, 32)
        layout.setBackgroundColor(Color.BLACK)

        val title = TextView(this)
        title.text = "Boom & Crash Spike Detector"
        title.textSize = 24f
        title.setTextColor(Color.WHITE)
        title.gravity = Gravity.CENTER

        val input = EditText(this)
        input.hint = "Enter multiplier e.g. 2.50"
        input.setTextColor(Color.WHITE)
        input.setHintTextColor(Color.GRAY)
        input.inputType = 8194

        val button = Button(this)
        button.text = "ANALYZE"

        val status = TextView(this)
        status.text = "Enter a multiplier"
        status.textSize = 18f
        status.setTextColor(Color.WHITE)
        status.gravity = Gravity.CENTER
        status.setPadding(0, 30, 0, 0)

        button.setOnClickListener {
            val value = input.text.toString().toDoubleOrNull()

            if (value == null) {
                status.text = "Please enter a valid multiplier"
                status.setTextColor(Color.RED)
            } else {
                val result = detector.addMultiplier(value)
                status.text = result

                status.setTextColor(
                    when {
                        result.contains("STRONG SPIKE") -> Color.RED
                        result.contains("Possible Spike") -> Color.YELLOW
                        result.contains("Strong Drop") -> Color.CYAN
                        result.contains("Normal") -> Color.GREEN
                        else -> Color.WHITE
                    }
                )
            }
        }

        layout.addView(title)
        layout.addView(input)
        layout.addView(button)
        layout.addView(status)

        setContentView(layout)
    }
} 
