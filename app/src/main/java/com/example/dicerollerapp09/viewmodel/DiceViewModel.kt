package com.example.dicerollerapp09.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dicerollerapp09.data.DiceState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import  com.example.dicerollerapp09.utils.SoundManager
import com.example.dicerollerapp09.utils.StatisticsManager
import com.example.dicerollerapp09.data.DiceStatistics

class DiceViewModel(application: Application) : AndroidViewModel(application) {

    //Add soundManager
    private val soundManager = SoundManager(application)

    private val statisticsManager = StatisticsManager(application)


    //Private mutable State
    private val _diceState = mutableStateOf(DiceState())
    val diceState: State<DiceState> = _diceState

    //statistics state
    private val _statistics = mutableStateOf(statisticsManager.getStatistics())
    val statistics: State<DiceStatistics> = _statistics

    fun rollDice() {

        soundManager.playDiceRoll()
        soundManager.startSpinning()

        // Start Rolling
        _diceState.value = DiceState(
            currentValue = _diceState.value.currentValue,
            isRolling = true
        )

        viewModelScope.launch {
            delay(2000) //2 seconds
            finishRoll()
        }

    }


    fun finishRoll() {
        soundManager.stopSpinning()

        //Save the current value as "last roll" before generating new value
        val previousValue = _diceState.value.currentValue

        val newValue = Random.nextInt(1, 7)

        //Update Dice State with New Value

        _diceState.value = DiceState(
            currentValue = newValue,
            isRolling = false
        )

        //Record the previous value as the last roll and New value as counting
        statisticsManager.recordRoll(previousValue, newValue)


        //Important : Force refresh Statistics state
        _statistics.value = statisticsManager.getStatistics()

    }

    override fun onCleared() {
        super.onCleared()
        soundManager.release()
    }
}