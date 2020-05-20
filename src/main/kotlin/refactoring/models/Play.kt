package refactoring.models

enum class PlayType { TRAGEDY, COMEDY }

data class Play(val name: String, val type: PlayType)