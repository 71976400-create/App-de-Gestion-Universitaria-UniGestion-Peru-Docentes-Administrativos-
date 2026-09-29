package com.example.unigestionperu_docentesadministrativos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.unigestionperu_docentesadministrativos.viewmodel.EstudianteConNotas
import com.example.unigestionperu_docentesadministrativos.viewmodel.TeacherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CursoDetailScreen(
    cursoId: Long,
    teacherViewModel: TeacherViewModel,
    onBack: () -> Unit
) {
    val estudiantes by teacherViewModel.getEstudiantesPorCurso(cursoId).collectAsState(initial = emptyList())
    var editingStudent by remember { mutableStateOf<EstudianteConNotas?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estudiantes y Registro de Notas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Lista de Estudiantes Matriculados",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (estudiantes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay estudiantes matriculados en este curso.", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(estudiantes) { estudiante ->
                        EstudianteCard(
                            estudiante = estudiante,
                            onEditClick = { editingStudent = estudiante }
                        )
                    }
                }
            }
        }

        val currentEditingStudent = editingStudent
        if (currentEditingStudent != null) {
            EditNotasDialog(
                estudiante = currentEditingStudent,
                onDismiss = { editingStudent = null },
                onSave = { n1, n2, ef ->
                    teacherViewModel.actualizarNotas(currentEditingStudent.matriculaId, n1, n2, ef) {
                        editingStudent = null
                    }
                }
            )
        }
    }
}

@Composable
fun EstudianteCard(estudiante: EstudianteConNotas, onEditClick: () -> Unit) {
    val esAprobado = estudiante.promedio >= 10.5

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = estudiante.nombreEstudiante,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = estudiante.emailEstudiante,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                IconButton(onClick = onEditClick) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar Notas",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("N1: ${estudiante.nota1}", style = MaterialTheme.typography.bodyMedium)
                Text("N2: ${estudiante.nota2}", style = MaterialTheme.typography.bodyMedium)
                Text("EF: ${estudiante.examenFinal}", style = MaterialTheme.typography.bodyMedium)

                Surface(
                    color = if (esAprobado) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(
                        text = "Prom: ${estudiante.promedio}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (esAprobado) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun EditNotasDialog(
    estudiante: EstudianteConNotas,
    onDismiss: () -> Unit,
    onSave: (Double, Double, Double) -> Unit
) {
    var nota1Str by remember { mutableStateOf(estudiante.nota1.toString()) }
    var nota2Str by remember { mutableStateOf(estudiante.nota2.toString()) }
    var examenFinalStr by remember { mutableStateOf(estudiante.examenFinal.toString()) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Registrar Notas (0 - 20)",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Column {
                Text(
                    "Estudiante: ${estudiante.nombreEstudiante}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = nota1Str,
                    onValueChange = { nota1Str = it },
                    label = { Text("Nota 1 (N1)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nota2Str,
                    onValueChange = { nota2Str = it },
                    label = { Text("Nota 2 (N2)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = examenFinalStr,
                    onValueChange = { examenFinalStr = it },
                    label = { Text("Examen Final (EF)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMsg != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMsg!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val n1 = nota1Str.toDoubleOrNull() ?: -1.0
                val n2 = nota2Str.toDoubleOrNull() ?: -1.0
                val ef = examenFinalStr.toDoubleOrNull() ?: -1.0

                if (n1 !in 0.0..20.0 || n2 !in 0.0..20.0 || ef !in 0.0..20.0) {
                    errorMsg = "Las notas deben estar dentro del rango de 0.0 a 20.0"
                } else {
                    onSave(n1, n2, ef)
                }
            }) {
                Text("Guardar Cambios", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
