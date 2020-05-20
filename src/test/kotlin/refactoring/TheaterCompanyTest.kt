package refactoring

import org.amshove.kluent.`should be equal to`
import org.junit.Test
import refactoring.models.Invoice
import refactoring.models.Performance
import refactoring.models.Play
import refactoring.models.PlayType

class TheaterCompanyTest {

    private val plays = mapOf(
        "hamlet" to Play("Hamlet", PlayType.TRAGEDY),
        "as-like" to Play("As You Like It", PlayType.COMEDY),
        "othello" to Play("Othello", PlayType.TRAGEDY)
    )

    private val invoice = Invoice(
        "BigCo",
        listOf(
            Performance("hamlet", 55),
            Performance("as-like", 35),
            Performance("othello", 40)
        )
    )

    @Test
    fun `should build statement string`() {
        val company = TheaterCompany(plays)

        val result = company.statement(invoice)

        val expected = "Statement for BigCo\n" +
                "Hamlet: USD 650.00 (55 seats)\n" +
                "As You Like It: USD 580.00 (35 seats)\n" +
                "Othello: USD 500.00 (40 seats)\n" +
                "Amount owed is USD 1730.00\n" +
                "You earned 47 credits\n"

        result `should be equal to` expected
    }

    @Test
    fun `should build statement html string`() {
        val company = TheaterCompany(plays)

        val result = company.htmlStatement(invoice)

        val expected = "<h1>Statement for BigCo</h1>\n" +
                "<table>\n" +
                "<tr><th>play</th><th>seats</th><th>cost</th></tr> <tr><td>Hamlet</td><td>55</td><td>USD 650.00</td></tr>\n" +
                " <tr><td>As You Like It</td><td>35</td><td>USD 580.00</td></tr>\n" +
                " <tr><td>Othello</td><td>40</td><td>USD 500.00</td></tr>\n" +
                "</table>\n" +
                "<p>Amount owed is <em>USD 1730.00</em></p>\n" +
                "<p>You earned <em>47</em> credits</p>\n"

        result `should be equal to` expected
    }
}