package refactoring

import org.joda.money.CurrencyUnit
import org.joda.money.Money
import kotlin.math.floor
import kotlin.math.max

data class Performance(val playID: String, val audience: Int)

data class Invoice(val costumer: String, val performances: List<Performance>)

enum class PlayType { TRAGEDY, COMEDY }

data class Play(val name: String, val type: PlayType)

data class StatementData(val costumer: String, val performances: List<Performance>)

class TheaterCompany(private val plays: Map<String, Play>) {

    fun statement(invoice: Invoice): String {
        val statementData = StatementData(invoice.costumer, invoice.performances)
        return renderPlainText(statementData)
    }

    private fun renderPlainText(data: StatementData): String {
        var result = "Statement for ${data.costumer}\n"

        data.performances.forEach { perf ->
            // print line for this order
            result += "${playFor(perf)?.name}: ${usd(amountFor(perf).toDouble())} (${perf.audience} seats)\n"

        }

        result += "Amount owed is ${usd(totalAmount(data.performances).toDouble())}\n"
        result += "You earned ${totalVolumeCredits(data.performances)} credits\n"

        return result
    }

    private fun amountFor(aPerformance: Performance): Int {
        var result = 0

        when (playFor(aPerformance)?.type) {
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
            else -> throw  Error("unknown type: ${playFor(aPerformance)?.type}")
        }

        return result
    }

    private fun playFor(aPerformance: Performance) = plays[aPerformance.playID]

    private fun volumeCreditsFor(aPerformance: Performance): Int {
        var result = 0
        result += max(aPerformance.audience - 30, 0)
        if (PlayType.COMEDY == playFor(aPerformance)?.type)
            result += floor(aPerformance.audience.toDouble() / 5).toInt()

        return result
    }

    private fun usd(aNumber: Double) = Money.of(CurrencyUnit.USD, aNumber / 100).toString()

    private fun totalVolumeCredits(performances: List<Performance>): Int {
        var result = 0
        performances.forEach { perf ->
            result += volumeCreditsFor(perf)
        }
        return result
    }

    private fun totalAmount(performances: List<Performance>): Int {
        var result = 0
        performances.forEach { perf ->
            result += amountFor(perf)
        }
        return result
    }
}