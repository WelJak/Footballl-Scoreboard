package com.weljak.football.scoreboard

import com.weljak.football.scoreboard.model.GameDetails
import java.util.concurrent.ConcurrentHashMap

class FootballScoreboard : Scoreboard {
    private val scoreMap = ConcurrentHashMap<Pair<String, String>, GameDetails>()
    override fun startGame(team1: String, team2: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun finishGame(team1: String, team2: String): GameDetails {
        TODO("Not yet implemented")
    }

    override fun updateScore(homeTeam: String, awayTeam: String, score: Pair<Int, Int>) {
        TODO("Not yet implemented")
    }

    override fun getScore(team1: String, team2: String): GameDetails {
        TODO("Not yet implemented")
    }

    override fun getGamesSummary(): List<GameDetails> {
        TODO("Not yet implemented")
    }

    override fun flush() {
        TODO("Not yet implemented")
    }
}