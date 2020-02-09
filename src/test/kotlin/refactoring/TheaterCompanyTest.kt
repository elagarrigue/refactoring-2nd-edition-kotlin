package refactoring

import org.amshove.kluent.`should be equal to`
import org.junit.Test

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
        val result = statement(invoice, plays)

        val expected = "Statement for BigCo\n" +
                "Hamlet: USD 650.00 (55 seats)\n" +
                "As You Like It: USD 580.00 (35 seats)\n" +
                "Othello: USD 500.00 (40 seats)\n" +
                "Amount owed is USD 1730.00\n" +
                "You earned 47 credits\n"

        result `should be equal to` expected
    }
}