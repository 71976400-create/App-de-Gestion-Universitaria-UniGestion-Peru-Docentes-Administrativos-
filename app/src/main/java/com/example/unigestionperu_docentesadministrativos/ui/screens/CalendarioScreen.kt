package com.example.unigestionperu_docentesadministrativos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class HorarioClase(
    val hora: String,
    val cursoCodigo: String,
    val cursoNombre: String,
    val aula: String,
    val diaSemana: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarioScreen(
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    var selectedDayIndex by remember { mutableStateOf(0) }
    val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")

    val horariosDemo = listOf(
        HorarioClase("08:00 - 10:15 AM", "CS101", "Desarrollo Móvil Android", "Lab Móvil A-102", "Lunes"),
        HorarioClase("10:30 - 12:45 PM", "CS102", "Base de Datos Avanzada", "Lab Cómputo B-204", "Lunes"),
        HorarioClase("08:00 - 10:15 AM", "CS103", "Ingeniería de Software", "Aula Magna 301", "Martes"),
        HorarioClase("02:00 - 04:15 PM", "CS101", "Desarrollo Móvil Android (Práctica)", "Lab Móvil A-102", "Miércoles"),
        HorarioClase("10:30 - 12:45 PM", "CS102", "Base de Datos Avanzada", "Lab Cómputo B-204", "Jueves"),
        HorarioClase("08:00 - 10:15 AM", "CS103", "Ingeniería de Software", "Aula Magna 301", "Viernes")
    )

    val clasesDelDia = horariosDemo.filter { it.diaSemana == dias[selectedDayIndex] }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Text(
            text = "Horario Académico Docente",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        PrimaryScrollableTabRow(
            selectedTabIndex = selectedDayIndex,
            edgePadding = 0.dp,
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ) {
            dias.forEachIndexed { index, dia ->
                Tab(
                    selected = selectedDayIndex == index,
                    onClick = { selectedDayIndex = index },
                    text = {
                        Text(
                            dia,
                            fontWeight = if (selectedDayIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (clasesDelDia.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay clases programadas para el día ${dias[selectedDayIndex]}.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(clasesDelDia) { clase ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.AccessTime,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = clase.hora,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                AssistChip(
                                    onClick = { },
                                    label = { Text(clase.cursoCodigo) },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.School,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = clase.cursoNombre,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = clase.aula,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
