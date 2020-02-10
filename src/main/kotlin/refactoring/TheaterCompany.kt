package refactoring

import org.joda.money.CurrencyUnit
import org.joda.money.Money
import kotlin.math.floor
import kotlin.math.max

data class Performance(val playID: String, val audience: Int)

data class PerformanceWithPlay(val play: Play, val audience: Int) {
    var amount: Int = 0
    var volumeCredits: Int = 0
}

data class Invoice(val costumer: String, val performances: List<Performance>)

enum class PlayType { TRAGEDY, COMEDY }

data class Play(val name: String, val type: PlayType)

data class StatementData(val costumer: String, val performances: List<PerformanceWithPlay>) {
    var totalAmount: Int = 0
    var totalVolumeCredits: Int = 0
}

class TheaterCompany(private val plays: Map<String, Play>) {

    fun statement(invoice: Invoice): String {
        val statementData =
            StatementData(
                invoice.costumer,
                invoice.performances.map(::enrichPerformance)
            ).apply {
                totalAmount = totalAmount(this.performances)
                totalVolumeCredits = totalVolumeCredits(this.performances)
            }
        return renderPlainText(statementData)
    }

    private fun enrichPerformance(aPerformance: Performance): PerformanceWithPlay =
        PerformanceWithPlay(
            playFor(aPerformance) ?: error("Missing play"),
            aPerformance.audience
        )
            .apply {
                amount = amountFor(this)
                volumeCredits = volumeCreditsFor(this)
            }

    private fun totalAmount(performances: List<PerformanceWithPlay>): Int {
        var result = 0
        performances.forEach { perf ->
            result += perf.amount
        }
        return result
    }

    private fun totalVolumeCredits(performances: List<PerformanceWithPlay>): Int {
        var result = 0
        performances.forEach { perf ->
            result += perf.volumeCredits
        }
        return result
    }

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

    private fun playFor(aPerformance: Performance) = plays[aPerformance.playID]

    private fun volumeCreditsFor(aPerformance: PerformanceWithPlay): Int {
        var result = 0
        result += max(aPerformance.audience - 30, 0)
        if (PlayType.COMEDY == aPerformance.play.type)
            result += floor(aPerformance.audience.toDouble() / 5).toInt()

        return result
    }

    private fun renderPlainText(data: StatementData): String {
        var result = "Statement for ${data.costumer}\n"

        data.performances.forEach { perf ->
            // print line for this order
            result += "${perf.play.name}: ${usd(perf.amount.toDouble())} (${perf.audience} seats)\n"

        }

        result += "Amount owed is ${usd(data.totalAmount.toDouble())}\n"
        result += "You earned ${data.totalVolumeCredits} credits\n"

        return result
    }

    private fun usd(aNumber: Double) = Money.of(CurrencyUnit.USD, aNumber / 100).toString()


}