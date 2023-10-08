package com.weljak.football.scoreboard

import com.weljak.football.scoreboard.exception.AmbiguousMatchDetailsException
import com.weljak.football.scoreboard.exception.GameAlreadyStartedException
import com.weljak.football.scoreboard.exception.SameTeamNameException
import com.weljak.football.scoreboard.exception.TeamCurrentlyPlayingException
import com.weljak.football.scoreboard.internal.Utils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class FootballScoreboardTest {
    private val scoreboard = FootballScoreboard()

    @BeforeEach
    fun before() {
        scoreboard.flush()
    }

    @Test
    fun scoreboardShouldStartGame() {
        //given
        val homeTeamName = "Team1"
        val awayTeamName = "Team2"

        //when
        scoreboard.startGame(homeTeamName, awayTeamName)

        //then
        val gameDetails = scoreboard.getScore(homeTeamName, awayTeamName)
        assertNotNull(gameDetails)
        assertEquals(homeTeamName, gameDetails.homeTeam)
        assertEquals(awayTeamName, gameDetails.awayTeam)
        assertEquals(Pair(0, 0), gameDetails.getScore())
    }

    @Test
    fun scoreboardShouldThrowExceptionWhenOneTeamIsCurrentlyPlaying() {
        //given
        val homeTeamName = "Team1"
        val awayTeamName = "Team2"
        val thirdTeamName = "Team3"

        //when
        scoreboard.startGame(homeTeamName, awayTeamName)

        //then
        assertThrows<TeamCurrentlyPlayingException> { scoreboard.startGame(homeTeamName, thirdTeamName) }
    }

    @Test
    fun scoreboardShouldThrowExceptionWhenGameIsStartedAndTeamNamesAreSwapped() {
        //given
        val homeTeamName = "team1"
        val awayTeamName = "team2"

        //then
        scoreboard.startGame(homeTeamName, awayTeamName)
        assertThrows<GameAlreadyStartedException> { scoreboard.startGame(awayTeamName, homeTeamName) }
    }

    @Test
    fun scoreboardShouldThrowExceptionWhenTeamsNamesAreTheSame() {
        //given
        val teamName = "team"
        //then
        assertThrows<SameTeamNameException> { scoreboard.startGame(teamName, teamName) }
    }

    @Test
    fun scoreboardShouldThrowExceptionWhenGameIsAlreadyStarted() {
        //given
        val homeTeamName = "team1"
        val awayTeamName = "team2"

        //when
        scoreboard.startGame(homeTeamName, awayTeamName)

        //then
        assertThrows<GameAlreadyStartedException> { scoreboard.startGame(homeTeamName, awayTeamName) }
    }

    @Test
    fun scoreboardShouldCapitalizeTeamNames() {
        //given
        val homeTeamName = "tEam1 NAme"
        val awayTeamName = "team2 nAME abc"

        //when
        scoreboard.startGame(homeTeamName, awayTeamName)

        //then
        val gameDetails = scoreboard.getScore(homeTeamName, awayTeamName)
        assertNotNull(gameDetails)
        assertEquals(Utils.capitalize(homeTeamName), gameDetails.homeTeam)
        assertEquals(Utils.capitalize(awayTeamName), gameDetails.awayTeam)
    }

    @Test
    fun scoreboardShouldReturnGameDetailsWhenTeamNamesAreSwapped() {
        //given
        val homeTeamName = "Team1"
        val awayTeamName = "Team2"

        //when
        scoreboard.startGame(homeTeamName, awayTeamName)

        //then
        val gameDetails = scoreboard.getScore(awayTeamName, homeTeamName)
        assertNotNull(gameDetails)
        assertEquals(homeTeamName, gameDetails.homeTeam)
        assertEquals(awayTeamName, gameDetails.awayTeam)

        val gameDetails2 = scoreboard.getScore(homeTeamName, awayTeamName)
        assertEquals(gameDetails2, gameDetails)
    }


    @Test
    fun scoreboardShouldFinishGame() {
        //given
        val homeTeamName = "team1"
        val awayTeamName = "team2"

        //when
        scoreboard.startGame(homeTeamName, awayTeamName)

        //then
        assertNotNull(scoreboard.finishGame(homeTeamName, awayTeamName))
        assertNull(scoreboard.getScore(homeTeamName, awayTeamName))
    }

    @Test
    fun scoreboardShouldFinishGameWhenTeamNamesAreSwapped() {
        //given
        val homeTeamName = "team1"
        val awayTeamName = "team2"

        //when
        scoreboard.startGame(homeTeamName, awayTeamName)

        //then
        assertNotNull(scoreboard.finishGame(awayTeamName, homeTeamName))
        assertNull(scoreboard.getScore(homeTeamName, awayTeamName))
    }

    @Test
    fun scoreboardShouldCapitalizeNamesAndFinishGame() {
        //given
        val homeTeamName = "team1 naMe"
        val awayTeamName = "team2 NAME"

        //when
        scoreboard.startGame(homeTeamName, awayTeamName)

        //then
        assertNotNull(scoreboard.finishGame(Utils.capitalize(homeTeamName), Utils.capitalize(awayTeamName)))
        assertNull(scoreboard.getScore(homeTeamName, awayTeamName))
    }

    @Test
    fun scoreboardShouldCapitalizeNamesAndFinishGameWhenNamesAreSwapped() {
        //given
        val homeTeamName = "team1 naMe"
        val awayTeamName = "team2 NAME"

        //when
        scoreboard.startGame(homeTeamName, awayTeamName)

        //then
        assertNotNull(scoreboard.finishGame(Utils.capitalize(awayTeamName), Utils.capitalize(homeTeamName)))
        assertNull(scoreboard.getScore(homeTeamName, awayTeamName))
    }

    @Test
    fun scoreboardShouldReturnNullWhenGameToFinishDoesNotExist() {
        //given
        val homeTeamName = "team1"
        val awayTeamName = "team2"

        //then
        assertNull(scoreboard.finishGame(homeTeamName, awayTeamName))
    }

    @Test
    fun scoreboardShouldReturnNullWhenGameDoesNotExists() {
        //given
        val homeTeamName = "team1"
        val awayTeamName = "team2"

        //then
        assertNull(scoreboard.getScore(homeTeamName, awayTeamName))
    }

    @Test
    fun scoreboardShouldUpdateScore() {
        //given
        val homeTeamName = "Team1"
        val awayTeamName = "Team2"
        val updateScore = Pair(0, 1)

        //when
        scoreboard.startGame(homeTeamName, awayTeamName)
        scoreboard.updateScore(homeTeamName, awayTeamName, updateScore)

        //then
        val gameDetails = scoreboard.getScore(homeTeamName, awayTeamName)
        assertNotNull(gameDetails)
        assertEquals(0, gameDetails.homeTeamScore)
        assertEquals(homeTeamName, gameDetails.homeTeam)

        assertEquals(1, gameDetails.awayTeamScore)
        assertEquals(awayTeamName, gameDetails.awayTeam)
    }

    @Test
    fun scoreboardShouldCapitalizeTeamNamesAndUpdateScore() {
        //given
        val homeTeamName = "TEAm1"
        val awayTeamName = "Team2 nAme"
        val updateScore = Pair(0, 1)

        //when
        scoreboard.startGame(homeTeamName, awayTeamName)
        scoreboard.updateScore(homeTeamName, awayTeamName, updateScore)

        //then
        val gameDetails = scoreboard.getScore(homeTeamName, awayTeamName)
        assertNotNull(gameDetails)
        assertEquals(0, gameDetails.homeTeamScore)
        assertEquals(Utils.capitalize(homeTeamName), gameDetails.homeTeam)

        assertEquals(1, gameDetails.awayTeamScore)
        assertEquals(Utils.capitalize(awayTeamName), gameDetails.awayTeam)
    }

    @Test
    fun scoreboardUpdateScoreShouldThrowExceptionWhenTeamNamesAreSwapped() {
        //given
        val homeTeamName = "Team1"
        val awayTeamName = "Team2"
        val updateScore = Pair(0, 1)

        //when
        scoreboard.startGame(homeTeamName, awayTeamName)

        //then
        assertThrows<AmbiguousMatchDetailsException> { scoreboard.updateScore(awayTeamName, homeTeamName, updateScore) }
    }

    @Test
    fun scoreboardShouldReturnGameSummaryByTotalScore() {
        //given
        val firstMatchHomeTeamName = "Team1"
        val firstMatchAwayTeamName = "Team2"
        val firstMatchScore = Pair(2, 0)

        val secondMatchHomeTeamName = "Team3"
        val secondMatchAwayTeamName = "Team4"
        val secondMatchScore = Pair(2, 5)

        val thirdMatchHomeTeamName = "Team5"
        val thirdMatchAwayTeamName = "Team6"
        val thirdMatchScore = Pair(1, 3)

        //when
        scoreboard.startGame(firstMatchHomeTeamName, firstMatchAwayTeamName)
        scoreboard.startGame(secondMatchHomeTeamName, secondMatchAwayTeamName)
        scoreboard.startGame(thirdMatchHomeTeamName, thirdMatchAwayTeamName)

        scoreboard.updateScore(firstMatchHomeTeamName, firstMatchAwayTeamName, firstMatchScore)
        scoreboard.updateScore(secondMatchHomeTeamName, secondMatchAwayTeamName, secondMatchScore)
        scoreboard.updateScore(thirdMatchHomeTeamName, thirdMatchAwayTeamName, thirdMatchScore)

        //then
        val summary = scoreboard.getGamesSummary()
        assertEquals(3, summary.size)
        assertEquals(secondMatchHomeTeamName, summary.first().homeTeam)
        assertEquals(secondMatchAwayTeamName, summary.first().awayTeam)

        assertEquals(thirdMatchHomeTeamName, summary[1].homeTeam)
        assertEquals(thirdMatchAwayTeamName, summary[1].awayTeam)

        assertEquals(firstMatchHomeTeamName, summary.last().homeTeam)
        assertEquals(firstMatchAwayTeamName, summary.last().awayTeam)
    }

}