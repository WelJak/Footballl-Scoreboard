package com.weljak.football.scoreboard

import com.weljak.football.scoreboard.exception.AmbiguousMatchDetailsException
import com.weljak.football.scoreboard.exception.GameAlreadyStartedException
import com.weljak.football.scoreboard.exception.SameTeamNameException
import com.weljak.football.scoreboard.exception.TeamCurrentlyPlayingException
import com.weljak.football.scoreboard.internal.Utils
import com.weljak.football.scoreboard.model.GameDetails
import java.util.concurrent.ConcurrentHashMap

class FootballScoreboard : Scoreboard {
    private val scoreMap = ConcurrentHashMap<Pair<String, String>, GameDetails>()

    override fun startGame(team1: String, team2: String) {
        val (homeTeamCapitalized, awayTeamCapitalized) = Utils.capitalize(team1, team2)
        if (homeTeamCapitalized == awayTeamCapitalized) {
            throw SameTeamNameException("Given team names are the same.")
        }

        if (scoreMap.containsKey(Pair(homeTeamCapitalized, awayTeamCapitalized)) ||
            scoreMap.containsKey(Pair(awayTeamCapitalized, homeTeamCapitalized))) {
            throw GameAlreadyStartedException(
                "Game for $homeTeamCapitalized and $awayTeamCapitalized is already in play!"
            )
        }


        if (scoreMap.keys().toList().any { key ->
                val teamList = key.toList()
                teamList.contains(homeTeamCapitalized) || teamList.contains(awayTeamCapitalized)
            }) {
            throw TeamCurrentlyPlayingException(
                "Either $homeTeamCapitalized or $awayTeamCapitalized is currently playing."
            )
        }

        val gameDetails = GameDetails(
            homeTeamCapitalized,
            awayTeamCapitalized,
            0,
            0
        )
        scoreMap[Pair(homeTeamCapitalized, awayTeamCapitalized)] = gameDetails
    }

    override fun finishGame(team1: String, team2: String): GameDetails? {
        val (homeTeamCapitalized, awayTeamCapitalized) = Utils.capitalize(team1, team2)
        val removed = scoreMap.remove(Pair(homeTeamCapitalized, awayTeamCapitalized))
        return removed ?: scoreMap.remove(Pair(awayTeamCapitalized, homeTeamCapitalized))
    }

    override fun updateScore(homeTeam: String, awayTeam: String, score: Pair<Int, Int>) {
        val (homeTeamCapitalized, awayTeamCapitalized) = Utils.capitalize(homeTeam, awayTeam)
        if (scoreMap.containsKey(Pair(awayTeamCapitalized, homeTeamCapitalized))) {
            throw AmbiguousMatchDetailsException(
                "Team names are swapped - provide proper data to avoid mistakes when updating score."
            )
        }

        val key = Pair(homeTeamCapitalized, awayTeamCapitalized)
        if (scoreMap.containsKey(key)) {
            scoreMap[key] =
                scoreMap[key]!!.copy(homeTeamScore = score.first, awayTeamScore = score.second)
        }
    }

    override fun getScore(team1: String, team2: String): GameDetails? {
        val (homeTeamCapitalized, awayTeamCapitalized) = Utils.capitalize(team1, team2)
        return scoreMap[Pair(homeTeamCapitalized, awayTeamCapitalized)]
            ?: scoreMap[Pair(awayTeamCapitalized, homeTeamCapitalized)]
    }

    override fun getGamesSummary(): List<GameDetails> {
        return scoreMap.values.sortedByDescending { it.getScore().toList().sum() }
    }

    override fun flush() {
        scoreMap.clear()
    }
}