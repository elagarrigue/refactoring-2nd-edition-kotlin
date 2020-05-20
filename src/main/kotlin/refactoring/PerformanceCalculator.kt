package refactoring

import refactoring.models.Performance
import refactoring.models.Play
import refactoring.models.PlayType
import kotlin.math.floor
import kotlin.math.max

object PerformanceCalculatorFactory {

    fun get(aPerformance: Performance, play: Play) =
        when (play.type) {
            PlayType.COMEDY -> ComedyCalculator(aPerformance, play)
            PlayType.TRAGEDY -> TragedyCalculator(aPerformance, play)
        }
}

sealed class PerformanceCalculator(
    val performance: Performance,
    val play: Play
) {

    abstract fun getAmount(): Int

    open fun getVolumeCredits() = max(performance.audience - 30, 0)
}

class TragedyCalculator(
    performance: Performance,
    play: Play
) : PerformanceCalculator(performance, play) {

    override fun getAmount(): Int {
        var result = 40000
        if (performance.audience > 30) {
            result += 1000 * (performance.audience - 30)
        }

        return result
    }
}

class ComedyCalculator(
    performance: Performance,
    play: Play
) : PerformanceCalculator(performance, play) {

    override fun getAmount(): Int {
        var result = 30000
        if (performance.audience > 20) {
            result += 10000 + 500 * (performance.audience - 20)
        }
        result += 300 * performance.audience

        return result
    }

    override fun getVolumeCredits() =
        super.getVolumeCredits() + floor(performance.audience.toDouble() / 5).toInt()
}

