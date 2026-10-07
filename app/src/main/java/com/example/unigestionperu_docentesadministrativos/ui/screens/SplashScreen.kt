package com.example.unigestionperu_docentesadministrativos.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unigestionperu_docentesadministrativos.ui.components.AppLogo
import com.example.unigestionperu_docentesadministrativos.ui.theme.UC_Azul
import com.example.unigestionperu_docentesadministrativos.ui.theme.UC_Morado
import com.example.unigestionperu_docentesadministrativos.ui.theme.UC_Purpura
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onTimeout: () -> Unit
) {
    val progressAnim = remember { Animatable(0f) }
    var isLoadingPhase by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        // Fase 1: Muestra el logo inicial en fondo blanco (Splash)
        delay(500)
        isLoadingPhase = true

        // Fase 2: Animación de cargado (Circular Ring 0% -> 100%)
        progressAnim.animateTo(
            targetValue = 100f,
            animationSpec = tween(
                durationMillis = 2200,
                easing = LinearEasing
            )
        )

        delay(300)
        onTimeout()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Contenedor del Logo con Anillo de Progreso Circular
            Box(
                modifier = Modifier.size(240.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isLoadingPhase) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 8.dp.toPx()
                        val diameter = size.minDimension - strokeWidth
                        val topLeftOffset = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)
                        val arcSize = Size(diameter, diameter)

                        // Anillo Fondo (Púrpura muy claro / suave)
                        drawArc(
                            color = Color(0xFFE8E5F8),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = topLeftOffset,
                            size = arcSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )

                        // Anillo Progreso Activo (Gradiente Azul a Morado)
                        if (progressAnim.value > 0f) {
                            drawArc(
                                brush = Brush.sweepGradient(
                                    colors = listOf(UC_Azul, UC_Morado, UC_Purpura, UC_Azul)
                                ),
                                startAngle = -90f,
                                sweepAngle = 360f * (progressAnim.value / 100f),
                                useCenter = false,
                                topLeft = topLeftOffset,
                                size = arcSize,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }
                    }
                }

                // Logo UniGestión Perú en el centro
                AppLogo(modifier = Modifier.size(130.dp))
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Título Principal de la App
            Text(
                text = "UNIGESTIÓN PERÚ",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = UC_Azul
            )

            if (isLoadingPhase) {
                Spacer(modifier = Modifier.height(16.dp))

                // Porcentaje de Cargado (ej: 50%)
                Text(
                    text = "${progressAnim.value.toInt()}%",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = UC_Purpura
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Subtítulo Oficial
                Text(
                    text = "Gestión Universitaria",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = Color(0xFF666666)
                )
            }
        }
    }
}
