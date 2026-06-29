package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.Primary
import com.example.ui.theme.Secondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToNext: () -> Unit) {
    val scale = remember { Animatable(0.9f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        delay(2000)
        onNavigateToNext()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.scale(scale.value)
        ) {
            // Brand Logo container
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(36.dp))
                    .background(Primary),
                contentAlignment = Alignment.Center
            ) {
                // We show our custom foreground vector drawable
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Sou Alérgico Logo",
                    modifier = Modifier.size(120.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Sou Alérgico",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sua segurança em primeiro lugar",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Secondary
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Pulse Loading Dot Indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                val dots = listOf(0, 1, 2)

                dots.forEach { index ->
                    val delayMillis = index * 200
                    val scaleFactor by infiniteTransition.animateFloat(
                        initialValue = 0.6f,
                        targetValue = 1.2f,
                        animationSpec = infiniteRepeatable(
                            animation = keyframes {
                                durationMillis = 1000
                                0.6f at delayMillis
                                1.2f at delayMillis + 300
                                0.6f at delayMillis + 600
                            },
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "dot_scale_$index"
                    )

                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .scale(scaleFactor)
                            .clip(RoundedCornerShape(5.dp))
                            .background(Primary)
                    )
                }
            }
        }

        // Footer Brand tag
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "HEALTH COMPANION",
                fontSize = 11.sp,
                color = Secondary.copy(alpha = 0.6f),
                letterSpacing = 4.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
