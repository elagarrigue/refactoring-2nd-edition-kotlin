package refactoring

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
                volumeCredits = calculator.getVolumeCredits()
            }
    }

    private fun createPerformanceCalculator(aPerformance: Performance) =
        when(playFor(aPerformance).type) {
            PlayType.COMEDY ->  ComedyCalculator(aPerformance, playFor(aPerformance))
            PlayType.TRAGEDY ->  TragedyCalculator(aPerformance, playFor(aPerformance))
        }


    private fun totalAmount(data: StatementData): Int =
        data.performances.map { it.amount }.reduce { acc, amount -> acc + amount }

    private fun totalVolumeCredits(data: StatementData): Int =
        data.performances.map { it.volumeCredits }.reduce { acc, volumeCredits -> acc + volumeCredits }

    private fun playFor(aPerformance: Performance) = plays[aPerformance.playID] ?: error("Missing play")
}