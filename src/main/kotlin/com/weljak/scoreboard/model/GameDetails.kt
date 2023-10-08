package com.weljak.scoreboard.model

data class GameDetails(
    val homeTeam: String,
    val awayTeam: String,
    val homeTeamScore: Int,
    val awayTeamScore: Int
) {
    fun getScore(): Pair<Int, Int> = Pair(homeTeamScore, awayTeamScore)
}