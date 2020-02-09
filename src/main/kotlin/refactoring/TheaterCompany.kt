package refactoring

import org.joda.money.CurrencyUnit
import org.joda.money.Money
import kotlin.math.floor
import kotlin.math.max

data class Performance(val playID: String, val audience: Int)

data class Invoice(val costumer: String, val performances: List<Performance>)

enum class PlayType { TRAGEDY, COMEDY }

data class Play(val name: String, val type: PlayType)

class TheaterCompany(private val plays: Map<String, Play>) {

    fun statement(invoice: Invoice): String {
        var totalAmount = 0
        var volumeCredits = 0
        var result = "Statement for ${invoice.costumer}\n"
        val format: (money: Double) -> String = {
            Money.of(CurrencyUnit.USD, it).toString()
        }

        invoice.performances.forEach { perf ->
            val thisAmount = amountFor(perf)

            // add volume credits
            volumeCredits += max(perf.audience - 30, 0)
            // add extra credit for every ten comedy attendees
            if (PlayType.COMEDY == playFor(perf)?.type)
                volumeCredits += floor(perf.audience.toDouble() / 5).toInt()

            // print line for this order
            result += "${playFor(perf)?.name}: ${format(thisAmount.toDouble() / 100)} (${perf.audience} seats)\n"
            totalAmount += thisAmount
        }

        result += "Amount owed is ${format(totalAmount.toDouble() / 100)}\n"
        result += "You earned $volumeCredits credits\n"

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
}