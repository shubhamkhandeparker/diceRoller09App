package com.example.dicerollerapp09.data

data class DiceStatistics(
    val totalRolls: Int = 0,
    val lastRolls: Int = 1,
    val rollCounts: Map<Int, Int> = mapOf(
        1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0, 6 to 0
    )
) {
    //Calculate lucky number ( most rolled)
     val luckyNumber : Pair<Int,Int> = rollCounts.maxByOrNull { it.value }?.toPair()?:(1 to 0)
}

