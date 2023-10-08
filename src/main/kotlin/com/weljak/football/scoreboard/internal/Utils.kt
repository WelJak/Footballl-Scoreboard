package com.weljak.football.scoreboard.internal

object Utils {
    private const val WHITESPACE_DELIMITER = "\\s"
    fun capitalize(teamName: String): String {
        return teamName
            .split(WHITESPACE_DELIMITER.toRegex())
            .map { name -> name.lowercase().replaceFirstChar { firstChar -> firstChar.uppercase() } }
            .reduce { acc, s -> "$acc $s" }
    }
}