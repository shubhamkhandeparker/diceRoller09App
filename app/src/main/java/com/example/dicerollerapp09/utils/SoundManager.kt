package com.example.dicerollerapp09.utils

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.material3.MediumTopAppBar
import com.example.dicerollerapp09.R


class SoundManager(private val context: Context) {
    private var diceRollPlayer: MediaPlayer? = null
    private var backgroundSpinPlayer: MediaPlayer? = null

    init {
        prepareSound()
    }


    private fun prepareSound() {
        try {
            diceRollPlayer = MediaPlayer.create(context, R.raw.diceroll)
            diceRollPlayer?.setOnCompletionListener {
                diceRollPlayer?.seekTo(0)    //Reset to beginning instead of releasing
            }
            //prepare background spin sound
            backgroundSpinPlayer = MediaPlayer.create(context, R.raw.background_spin)
            backgroundSpinPlayer?.isLooping = true

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playDiceRoll() {
        try {

            //play immediate impact sound
            diceRollPlayer?.let {
                if (it.isPlaying) it.seekTo(0)
                it.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startSpinning() {
        try {
            backgroundSpinPlayer?.let {
                if (!it.isPlaying) {
                    it.start()
                }
            }
        } catch (e: Exception){
            e.printStackTrace()
    }
}
    fun stopSpinning(){
        try{
            backgroundSpinPlayer?.pause()
            backgroundSpinPlayer?.seekTo(0)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun release() {
        diceRollPlayer?.release()
        backgroundSpinPlayer?.release()
        diceRollPlayer=null
        backgroundSpinPlayer=null
    }
}