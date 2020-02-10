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
        val calculator = createPerformanceCalculator(aPerformance)

        return PerformanceWithPlay(
            calculator.play,
            aPerformance.audience
        )
            .apply {
                amount = calculator.getAmount()
                volumeCredits = volumeCreditsFor(this)
            }
    }

    private fun createPerformanceCalculator(aPerformance: Performance) =
        PerformanceCalculator(aPerformance, playFor(aPerformance))

    private fun totalAmount(data: StatementData): Int =
        data.performances.map { it.amount }.reduce { acc, amount -> acc + amount }

    private fun totalVolumeCredits(data: StatementData): Int =
        data.performances.map { it.volumeCredits }.reduce { acc, volumeCredits -> acc + volumeCredits }

    private fun playFor(aPerformance: Performance) = plays[aPerformance.playID] ?: error("Missing play")

    private fun volumeCreditsFor(aPerformance: PerformanceWithPlay): Int {
        var result = 0
        result += max(aPerformance.audience - 30, 0)
        if (PlayType.COMEDY == aPerformance.play.type)
            result += floor(aPerformance.audience.toDouble() / 5).toInt()

        return result
    }
}