package refactoring

class PerformanceCalculator (
    private val performance: Performance,
    val play: Play
    ) {

    fun getAmount(): Int {
        var result = 0

        when (play.type) {
            PlayType.TRAGEDY -> {
                result = 40000
                if (performance.audience > 30) {
                    result += 1000 * (performance.audience - 30)
                }
            }
            PlayType.COMEDY -> {
                result = 30000
                if (performance.audience > 20) {
                    result += 10000 + 500 * (performance.audience - 20)
                }
                result += 300 * performance.audience
            }
        }

        return result
    }

}