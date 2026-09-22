package com.tj804.boomcrashdetector

class SpikeDetector {

    private val history = mutableListOf<Double>()

    fun addMultiplier(multiplier: Double): String {
        if (multiplier <= 0) {
            return "Invalid value"
        }

        history.add(multiplier)

        // Keep the most recent 20 values
        if (history.size > 20) {
            history.removeAt(0)
        }

        if (history.size < 5) {
            return "Collecting data..."
        }

        val average = history.average()

        return when {
            multiplier >= average * 3.0 ->
                "⚠️ STRONG SPIKE DETECTED"

            multiplier >= average * 2.0 ->
                "🟠 Possible Spike"

            multiplier <= average * 0.5 ->
                "🔵 Strong Drop"

            else ->
                "🟢 Normal"
        }
    }

    fun getHistory(): List<Double> {
        return history.toList()
    }
}
