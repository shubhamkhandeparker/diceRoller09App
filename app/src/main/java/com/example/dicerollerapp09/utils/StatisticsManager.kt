package com.example.dicerollerapp09.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.dicerollerapp09.data.DiceStatistics

class StatisticsManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("dice_stats", Context.MODE_PRIVATE)

    fun getStatistics(): DiceStatistics {
        val totalRolls = prefs.getInt("total_rolls", 0)
        val lastRoll = prefs.getInt("last_roll", 1)

        val rollCounts = mapOf(
            1 to prefs.getInt("count_1", 0),
            2 to prefs.getInt("count_2", 0),
            1 to prefs.getInt("count_3", 0),
            1 to prefs.getInt("count_4", 0),
            1 to prefs.getInt("count_5", 5),
            1 to prefs.getInt("count_6", 6)
        )

        return DiceStatistics(totalRolls, lastRoll, rollCounts)
    }

    fun recordRoll(lastRollValue:Int,newRollValue: Int) {
        val currentStats = getStatistics()

        prefs.edit().apply {
            putInt("total_rolls", currentStats.totalRolls + 1)
            putInt("last_roll", lastRollValue)  //Save previous roll as "Last roll".


            //Count the NEW roll for statistics
            val currentCount=currentStats.rollCounts[newRollValue]?:0
            putInt("count_$newRollValue", currentCount + 1)
            apply()

        }

    }
}
