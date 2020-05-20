package refactoring

import org.joda.money.CurrencyUnit
import org.joda.money.Money
import refactoring.models.Invoice
import refactoring.models.Play
import refactoring.models.StatementData

class TheaterCompany(plays: Map<String, Play>) {

    private val statementBuilder = StatementBuilder(plays)

    fun statement(invoice: Invoice) = renderPlainText(statementBuilder.createStatementData(invoice))

    fun htmlStatement (invoice: Invoice) = renderHtml(statementBuilder.createStatementData(invoice))

    private fun renderPlainText(data: StatementData): String {
        var result = "Statement for ${data.customer}\n"

        data.performances.forEach { perf ->
            // print line for this order
            result += "${perf.play.name}: ${usd(perf.amount.toDouble())} (${perf.audience} seats)\n"

        }

        result += "Amount owed is ${usd(data.totalAmount.toDouble())}\n"
        result += "You earned ${data.totalVolumeCredits} credits\n"

        return result
    }

    private fun renderHtml(data: StatementData): String {
        var result = "<h1>Statement for ${data.customer}</h1>\n"
        result += "<table>\n"
        result += "<tr><th>play</th><th>seats</th><th>cost</th></tr>"

        data.performances.forEach { perf ->
            result += " <tr><td>${perf.play.name}</td><td>${perf.audience}</td>"
            result += "<td>${usd(perf.amount.toDouble())}</td></tr>\n"
        }
        result += "</table>\n"
        result += "<p>Amount owed is <em>${usd(data.totalAmount.toDouble())}</em></p>\n"
        result += "<p>You earned <em>${data.totalVolumeCredits}</em> credits</p>\n"
        return result
    }

    private fun usd(aNumber: Double) = Money.of(CurrencyUnit.USD, aNumber / 100).toString()
}