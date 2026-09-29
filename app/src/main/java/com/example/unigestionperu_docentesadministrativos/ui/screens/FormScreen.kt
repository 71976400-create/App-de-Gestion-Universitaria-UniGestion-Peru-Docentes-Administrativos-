package com.example.unigestionperu_docentesadministrativos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    onSaveCourse: (String, String, Int, String) -> Unit,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    var codigo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var creditosStr by remember { mutableStateOf("4") }
    var ciclo by remember { mutableStateOf("2026-I") }
    var mensajeExito by remember { mutableStateOf<String?>(null) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Text(
            text = "Registro Administrativo de Cursos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                OutlinedTextField(
                    value = codigo,
                    onValueChange = { codigo = it },
                    label = { Text("Código de Curso (ej. CS104)") },
                    leadingIcon = { Icon(Icons.Default.Book, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del Curso") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = creditosStr,
                    onValueChange = { creditosStr = it },
                    label = { Text("Créditos") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = ciclo,
                    onValueChange = { ciclo = it },
                    label = { Text("Ciclo Lectivo (ej. 2026-I)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (mensajeError != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(mensajeError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                if (mensajeExito != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(mensajeExito!!, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val creditos = creditosStr.toIntOrNull() ?: 0
                        if (codigo.isBlank() || nombre.isBlank() || creditos <= 0) {
                            mensajeError = "Por favor ingrese valores válidos en todos los campos."
                            mensajeExito = null
                        } else {
                            onSaveCourse(codigo, nombre, creditos, ciclo)
                            mensajeExito = "Curso '$nombre' registrado exitosamente."
                            mensajeError = null
                            codigo = ""
                            nombre = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Registrar Nuevo Curso", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
