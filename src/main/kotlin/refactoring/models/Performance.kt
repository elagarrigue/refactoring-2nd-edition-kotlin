package refactoring.models

data class Performance(val playID: String, val audience: Int)

data class PerformanceWithPlay(val play: Play, val audience: Int) {
    var amount: Int = 0
    var volumeCredits: Int = 0
}