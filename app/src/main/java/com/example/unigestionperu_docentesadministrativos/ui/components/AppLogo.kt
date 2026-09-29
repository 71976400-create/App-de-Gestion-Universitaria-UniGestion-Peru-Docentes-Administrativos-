package com.example.unigestionperu_docentesadministrativos.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.unigestionperu_docentesadministrativos.ui.theme.UC_Azul
import com.example.unigestionperu_docentesadministrativos.ui.theme.UC_Morado
import com.example.unigestionperu_docentesadministrativos.ui.theme.UC_Purpura

@Composable
fun AppLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(120.dp)) {
        val width = size.width
        val height = size.height

        // Puntos superiores con Púrpura y Morado Institucional oficial
        drawCircle(
            color = UC_Purpura,
            radius = width * 0.08f,
            center = Offset(width * 0.25f, height * 0.2f)
        )
        drawCircle(
            color = UC_Morado,
            radius = width * 0.08f,
            center = Offset(width * 0.5f, height * 0.26f)
        )
        drawCircle(
            color = UC_Purpura,
            radius = width * 0.08f,
            center = Offset(width * 0.75f, height * 0.2f)
        )

        // Degradado tridimensional de la base usando Azul Institucional y Morado
        val brandGradient = Brush.verticalGradient(
            colors = listOf(UC_Azul, UC_Morado, Color(0xFF01016B))
        )

        val leftPath = Path().apply {
            moveTo(width * 0.25f, height * 0.3f)
            cubicTo(width * 0.2f, height * 0.6f, width * 0.35f, height * 0.85f, width * 0.5f, height * 0.85f)
            cubicTo(width * 0.4f, height * 0.8f, width * 0.3f, height * 0.6f, width * 0.35f, height * 0.35f)
            close()
        }
        drawPath(path = leftPath, brush = brandGradient)

        val rightPath = Path().apply {
            moveTo(width * 0.75f, height * 0.3f)
            cubicTo(width * 0.8f, height * 0.6f, width * 0.65f, height * 0.85f, width * 0.5f, height * 0.85f)
            cubicTo(width * 0.6f, height * 0.8f, width * 0.7f, height * 0.6f, width * 0.65f, height * 0.35f)
            close()
        }
        drawPath(path = rightPath, brush = brandGradient)

        // Trazo central blanco
        val centerLine = Path().apply {
            moveTo(width * 0.5f, height * 0.4f)
            lineTo(width * 0.5f, height * 0.75f)
        }
        drawPath(path = centerLine, color = Color.White, style = Stroke(width = 4f))
    }
}
