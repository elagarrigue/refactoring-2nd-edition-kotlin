package refactoring

import kotlin.math.floor
import kotlin.math.max

class StatementBuilder(private val plays: Map<String, Play>) {

    fun createStatementData(invoice: Invoice) =
        StatementData(
            invoice.costumer,
            invoice.performances.map(::enrichPerformance)
        ).apply {
            totalAmount = totalAmount(this)
            totalVolumeCredits = totalVolumeCredits(this)
        }

    private fun enrichPerformance(aPerformance: Performance): PerformanceWithPlay {
        val calculator = PerformanceCalculator(aPerformance, playFor(aPerformance))

        return PerformanceWithPlay(
            calculator.play,
            aPerformance.audience
        )
            .apply {
                amount = amountFor(this)
                volumeCredits = volumeCreditsFor(this)
            }
    }

    private fun totalAmount(data: StatementData): Int =
        data.performances.map { it.amount }.reduce { acc, amount -> acc + amount }

    private fun totalVolumeCredits(data: StatementData): Int =
        data.performances.map { it.volumeCredits }.reduce { acc, volumeCredits -> acc + volumeCredits }

    private fun amountFor(aPerformance: PerformanceWithPlay): Int {
        var result = 0

        when (aPerformance.play.type) {
            PlayType.TRAGEDY -> {
                result = 40000
                if (aPerformance.audience > 30) {
                    result += 1000 * (aPerformance.audience - 30)
                }
            }
            PlayType.COMEDY -> {
                result = 30000
                if (aPerformance.audience > 20) {
                    result += 10000 + 500 * (aPerformance.audience - 20)
                }
                result += 300 * aPerformance.audience
            }
        }

        return result
    }

    private fun playFor(aPerformance: Performance) = plays[aPerformance.playID] ?: error("Missing play")

    private fun volumeCreditsFor(aPerformance: PerformanceWithPlay): Int {
        var result = 0
        result += max(aPerformance.audience - 30, 0)
        if (PlayType.COMEDY == aPerformance.play.type)
            result += floor(aPerformance.audience.toDouble() / 5).toInt()

        return result
    }
}