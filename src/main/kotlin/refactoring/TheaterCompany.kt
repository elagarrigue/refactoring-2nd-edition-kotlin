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

class TheaterCompany(plays: Map<String, Play>) {

    private val statementBuilder = StatementBuilder(plays)

    fun statement(invoice: Invoice) = renderPlainText(statementBuilder.createStatementData(invoice))
    
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