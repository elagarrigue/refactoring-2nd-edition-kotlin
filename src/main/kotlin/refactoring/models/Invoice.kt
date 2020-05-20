package refactoring.models

data class Invoice(val costumer: String, val performances: List<Performance>)

data class StatementData(val customer: String, val performances: List<PerformanceWithPlay>) {
    var totalAmount: Int = 0
    var totalVolumeCredits: Int = 0
}
