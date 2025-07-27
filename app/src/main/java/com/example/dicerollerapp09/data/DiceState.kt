package com.example.dicerollerapp09.data

data class DiceState(
    val currentValue :Int= 1,  //When number (1-6) to show
    val isRolling: Boolean= false  //It animation currently plays
)