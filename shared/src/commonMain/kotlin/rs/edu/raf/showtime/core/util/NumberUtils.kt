package rs.edu.raf.showtime.core.util

import kotlin.math.round

object NumberUtils {
    fun clamp2Decimals(value: Double): Double {
        return round(value * 100) / 100
    }
}