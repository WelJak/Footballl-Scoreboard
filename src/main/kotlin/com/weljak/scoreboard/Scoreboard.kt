package com.weljak.scoreboard

import com.weljak.scoreboard.model.GameDetails

sealed interface Scoreboard {
    fun startGame(team1: String, team2: String)
    fun finishGame(team1: String, team2: String): GameDetails?
    fun updateScore(homeTeam: String, awayTeam: String, score: Pair<Int, Int>)
    fun getScore(team1: String, team2: String): GameDetails?
    fun getGamesSummary(): List<GameDetails>
    fun flush()
}