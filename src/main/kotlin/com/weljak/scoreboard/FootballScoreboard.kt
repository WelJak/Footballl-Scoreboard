package com.weljak.scoreboard

import com.weljak.scoreboard.exception.AmbiguousMatchDetailsException
import com.weljak.scoreboard.exception.GameAlreadyStartedException
import com.weljak.scoreboard.exception.SameTeamNameException
import com.weljak.scoreboard.exception.TeamCurrentlyPlayingException
import com.weljak.scoreboard.internal.Utils
import com.weljak.scoreboard.model.GameDetails
import java.util.concurrent.ConcurrentHashMap

class FootballScoreboard : Scoreboard {
    private val scoreMap = ConcurrentHashMap<Pair<String, String>, GameDetails>()

    /**
     * This method starts a new game on scoreboard. Name order is not considered also names are capitalized e.g teAm oNE is converted to Team One etc.
     * @param team1 - name of the home team
     * @param team2 - name of the away team
     * @throws SameTeamNameException - when given team names are the same
     * @throws GameAlreadyStartedException - when game with given teams already started
     * @throws TeamCurrentlyPlayingException - when one of the team is currently playing game
     */
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

    /**
     * This method ends game and removes it from scoreboard. Name order is not considered also names are capitalized e.g teAm oNE is converted to Team One etc.
     * @param team1 - name of the home team
     * @param team2 - name of the away team
     * @return returns GameDetails if game exists or null otherwise
     */
    override fun finishGame(team1: String, team2: String): GameDetails? {
        val (homeTeamCapitalized, awayTeamCapitalized) = Utils.capitalize(team1, team2)
        val removed = scoreMap.remove(Pair(homeTeamCapitalized, awayTeamCapitalized))
        return removed ?: scoreMap.remove(Pair(awayTeamCapitalized, homeTeamCapitalized))
    }

    /**
     * This method update score of currently played match.
     * @param homeTeam - name of the home team
     * @param awayTeam - name of the away team
     * @throws AmbiguousMatchDetailsException - if names of the team are in inverted order
     */
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

    /**
     * This method returns score for given pair of teams. Name order is not considered also names are capitalized e.g teAm oNE is converted to Team One etc.
     * @param team1 - name of the home team
     * @param team2 - name of the away team
     * @return GameDetails if game exists otherwise null
     */
    override fun getScore(team1: String, team2: String): GameDetails? {
        val (homeTeamCapitalized, awayTeamCapitalized) = Utils.capitalize(team1, team2)
        return scoreMap[Pair(homeTeamCapitalized, awayTeamCapitalized)]
            ?: scoreMap[Pair(awayTeamCapitalized, homeTeamCapitalized)]
    }

    /**
     * This method returns list of currently played matches
     * @return List of GameDetails
     */
    override fun getGamesSummary(): List<GameDetails> {
        return scoreMap.values.sortedByDescending { it.getScore().toList().sum() }
    }

    /**
     * This method clears scoreboard
     */
    override fun flush() {
        scoreMap.clear()
    }
}