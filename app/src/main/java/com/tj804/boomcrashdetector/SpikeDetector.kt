package com.tj804.boomcrashdetector

class SpikeDetector {

    private val history = mutableListOf<Double>()

    fun addPrice(price: Double): String {

        if (price <= 0) {
            return "Invalid price"
        }

        history.add(price)

        // Keep the most recent 20 Boom 500 prices
        if (history.size > 20) {
            history.removeAt(0)
        }

        if (history.size < 6) {
            return "Collecting Boom 500 data... ${history.size}/20"
        }

        // Calculate price changes between consecutive readings
        val changes = mutableListOf<Double>()

        for (i in 1 until history.size) {
            changes.add(history[i] - history[i - 1])
        }

        val recentChanges = changes.takeLast(5)

        // Average absolute movement
        val averageMovement =
            recentChanges.map { kotlin.math.abs(it) }.average()

        if (averageMovement <= 0.0) {
            return "Waiting for price movement..."
        }

        val latestChange = changes.last()

        val previousChange =
            if (changes.size >= 2) changes[changes.size - 2] else 0.0

        val changeBeforePrevious =
            if (changes.size >= 3) changes[changes.size - 3] else 0.0

        // Positive movement means price is moving upward.
        val upwardCount =
            recentChanges.count { it > 0 }

        // Detect acceleration in upward movement.
        val acceleratingUp =
            latestChange > previousChange &&
            previousChange > changeBeforePrevious &&
            latestChange > 0

        // Strong upward movement compared with recent movement.
        val strongUpwardMove =
            latestChange > averageMovement * 2.5 &&
            latestChange > 0

        // Early upward movement condition.
        val buildingUpwardMove =
            latestChange > averageMovement * 1.5 &&
            upwardCount >= 3 &&
            latestChange > 0

        return when {
            strongUpwardMove && acceleratingUp ->
                "🔴 STRONG UPWARD SPIKE MOVEMENT"

            buildingUpwardMove && acceleratingUp ->
                "🟡 SPIKE CONDITIONS BUILDING"

            latestChange > averageMovement * 1.2 &&
                    upwardCount >= 3 ->
                "🟠 UPWARD MOVEMENT INCREASING"

            latestChange < -averageMovement * 2.5 ->
                "🔵 STRONG DOWNWARD MOVEMENT"

            else ->
                "🟢 NO STRONG SPIKE CONDITIONS"
        }
    }

    fun getHistory(): List<Double> {
        return history.toList()
    }

    fun reset() {
        history.clear()
    }
}
