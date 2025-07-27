package com.example.dicerollerapp09.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dicerollerapp09.R
import com.example.dicerollerapp09.viewmodel.DiceViewModel
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.rotate
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.WhitePoint
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import kotlin.math.*
import androidx.compose.ui.graphics.graphicsLayer

//Color Scheme
private val DarkBackground = Color(0xFF000000)
private val GoldPrimary = Color(0xFFFFD700)
private val GoldSecondary = Color(0xFFFFA500)
private val WhiteText = Color(0xFFFFFFFF)
private val GrayText = Color(0xFFCCCCCC)

private fun getDiceImage(value: Int): Int {
    return when (value) {
        1 -> R.drawable.dice_six_faces_one
        2 -> R.drawable.dice_six_faces_two
        3 -> R.drawable.dice_six_faces_three
        4 -> R.drawable.dice_six_faces_four
        5 -> R.drawable.dice_six_faces_five
        6 -> R.drawable.dice_six_faces_six
        else -> R.drawable.dice_six_faces_one
    }
}


@Composable
fun FingerprintSpiral(
    isRolling: Boolean,
    modifier: Modifier = Modifier
) {
    //Beat/Pulse Animation
    val beatScale by animateFloatAsState(
        targetValue = if (isRolling) 1.08f else 1.0f,
        animationSpec = if (isRolling) {
            tween(300, easing = LinearEasing)
        } else {
            tween(500)
        }
    )

    val glowIntensity by animateFloatAsState(
        targetValue = if (isRolling) 1.0f else 0.4f,
        animationSpec = tween(300)
    )
    Canvas(
        modifier = modifier
            .size(600.dp)
            .scale(beatScale)
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val maxRadius = size.width / 2 * 0.95f

        //Draw FingerPrint Spiral Pattern

        for (layer in 0..18) {
            val baseRadius = 30f + layer * 35f

            if (baseRadius > maxRadius) break

            val segments = 80 + layer * 10
            val path = Path()
            var isFistPoint = true

            for (i in 0 until segments) {
                val angle = (i.toFloat() / segments) * PI.toFloat() * 8 //Multiple Spiral
                val spiralRadius = baseRadius + sin(angle * 3) * (8f + layer * 2f)

                //Create gaps for fingerPrint effect
                val gapFactor = sin(angle * 12 + layer * 0.5f)
                if (gapFactor < -0.2f) continue

                val x = center.x + cos(angle) * spiralRadius
                val y = center.y + sin(angle) * spiralRadius

                if (isFistPoint) {
                    path.moveTo(x, y)
                    isFistPoint = false
                } else {
                    path.lineTo(x, y)
                }
            }

            //Color and Opacity based on rolling State
            val alpha = if (isRolling) {
                (0.9f - layer * 0.05f) * glowIntensity
            } else {
                (0.5f - layer * 0.03f) * glowIntensity
            }

            val color=if(isRolling){
                GoldPrimary.copy(alpha=alpha)
            }
            else{
                Color.White.copy(alpha=alpha)
            }

            drawPath(
                path=path,
                color=color,
                style=androidx.compose.ui.graphics.drawscope.Stroke(
                    width = if(isRolling)3.5f else 2.5f,
                    cap= StrokeCap.Round
                )
            )
        }
    }
}


@Composable
fun DiceScreen(
    modifier: Modifier = Modifier,
    viewModel: DiceViewModel = viewModel()
) {
    //Get current state from ViewModel
    val diceState by viewModel.diceState
    val statistics by viewModel.statistics

    val infiniteTransition =rememberInfiniteTransition(label = "dice_animation")

    val rotationAngle by if(diceState.isRolling){
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue=360f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "rotation"
        )
    }
    else{
       remember { mutableStateOf(0f) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        //FingerPrint spiral background
        FingerprintSpiral(
            isRolling = diceState.isRolling,
            modifier=Modifier.align(Alignment.Center)
        )


        //Main Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        )
        {

            //Header WIth Gradient text Effect
            Text(
                text = "\uD83C\uDFB2  Dice Roller",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary,
                modifier = Modifier.padding(bottom = 60.dp)
            )

            //Dice Button
            IconButton(
                onClick = { viewModel.rollDice() },
                modifier = Modifier.size(120.dp)
            ) {
                Image(
                    painter = painterResource(id = getDiceImage(diceState.currentValue)),
                    contentDescription = "Dice Showing ${diceState.currentValue}",
                    modifier = Modifier
                        .size(90.dp)
                        .scale(if (diceState.isRolling) 1.1f else 1.0f)
                        .rotate (if(diceState.isRolling) rotationAngle else 0f )
                        )
            }


            //Statistics Section
            Column(
                modifier = Modifier.padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Roll Statistics",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GoldPrimary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Total Rolls: ${statistics.totalRolls}",
                        fontSize = 18.sp,
                        color = WhiteText
                    )

                    Text(
                        text = "Last Roll: ${statistics.lastRolls}",
                        fontSize = 18.sp,
                        color = WhiteText
                    )

                    Text(
                        text = "Lucky Number :${statistics.luckyNumber.first}(${statistics.luckyNumber.second} times)",
                        fontSize = 18.sp,
                        color = WhiteText

                    )

                }
            }



            //Instruction Text
            Column(
                modifier = Modifier.padding(top = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tap the dice to roll",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = GoldSecondary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Experience smooth animation and realistic Sound effects ",
                    fontSize = 14.sp,
                    color = GrayText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

    }

}

