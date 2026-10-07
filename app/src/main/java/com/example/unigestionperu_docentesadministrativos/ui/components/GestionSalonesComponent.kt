package com.example.unigestionperu_docentesadministrativos.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.unigestionperu_docentesadministrativos.data.local.entities.SalonEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionSalonesComponent(
    salones: List<SalonEntity>,
    onAgregarSalon: (codigo: String, edificio: String, capacidad: Int, ocupados: Int, tipo: String, docenteAsignado: String, horario: String) -> Unit,
    onActualizarSalon: (SalonEntity) -> Unit,
    onEliminarSalon: (SalonEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showFormDialog by remember { mutableStateOf(false) }
    var salonParaEditar by remember { mutableStateOf<SalonEntity?>(null) }
    var salonParaEliminar by remember { mutableStateOf<SalonEntity?>(null) }

    val salonesFiltrados = salones.filter { salon ->
        salon.codigo.contains(searchQuery, ignoreCase = true) ||
                salon.edificio.contains(searchQuery, ignoreCase = true) ||
                salon.docenteAsignado.contains(searchQuery, ignoreCase = true) ||
                salon.horario.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Gestión de Salones y Aulas",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Total de Salones Disponibles: ${salones.size}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Button(
                onClick = {
                    salonParaEditar = null
                    showFormDialog = true
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Nuevo")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Buscador de Salones por código, horario o cupos
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar por aula, horario, pabellón o docente...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                    }
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (salonesFiltrados.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.MeetingRoom,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (salones.isEmpty()) "No hay salones creados actualmente." else "No se encontraron salones para '$searchQuery'.",
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(salonesFiltrados) { salon ->
                    SalonCardItem(
                        salon = salon,
                        onEdit = {
                            salonParaEditar = salon
                            showFormDialog = true
                        },
                        onDelete = {
                            salonParaEliminar = salon
                        }
                    )
                }
            }
        }
    }

    // Dialog para Crear / Editar Salón
    if (showFormDialog) {
        SalonFormDialog(
            salonExistente = salonParaEditar,
            onDismiss = {
                showFormDialog = false
                salonParaEditar = null
            },
            onSave = { codigo, edificio, capacidad, ocupados, tipo, docente, horario ->
                if (salonParaEditar == null) {
                    onAgregarSalon(codigo, edificio, capacidad, ocupados, tipo, docente, horario)
                } else {
                    val actualizado = salonParaEditar!!.copy(
                        codigo = codigo,
                        edificio = edificio,
                        capacidad = capacidad,
                        ocupados = ocupados,
                        tipo = tipo,
                        docenteAsignado = docente,
                        horario = horario
                    )
                    onActualizarSalon(actualizado)
                }
                showFormDialog = false
                salonParaEditar = null
            }
        )
    }

    // Dialog Confirmación de Eliminación
    if (salonParaEliminar != null) {
        AlertDialog(
            onDismissRequest = { salonParaEliminar = null },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("Eliminar Salón") },
            text = { Text("¿Está seguro de que desea eliminar el salón '${salonParaEliminar?.codigo}'? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        salonParaEliminar?.let { onEliminarSalon(it) }
                        salonParaEliminar = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { salonParaEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun SalonCardItem(
    salon: SalonEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val vacantes = (salon.capacidad - salon.ocupados).coerceAtLeast(0)

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
                        Icons.Default.MeetingRoom,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = salon.codigo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar Horario / Cupos",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar Salón",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Horario Asignado Destacado
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Horario: ${salon.horario}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Información de Cupos y Capacidad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    onClick = { },
                    label = { Text("Cupos Max: ${salon.capacidad}") },
                    leadingIcon = { Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.size(16.dp)) }
                )
                AssistChip(
                    onClick = { },
                    label = { Text("Ocupados: ${salon.ocupados}") }
                )
                AssistChip(
                    onClick = { },
                    label = { Text("Vacantes: $vacantes", fontWeight = FontWeight.Bold) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (vacantes > 0) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.errorContainer
                    )
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${salon.edificio} (${salon.tipo})",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Docente: ${salon.docenteAsignado}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun SalonFormDialog(
    salonExistente: SalonEntity?,
    onDismiss: () -> Unit,
    onSave: (codigo: String, edificio: String, capacidad: Int, ocupados: Int, tipo: String, docente: String, horario: String) -> Unit
) {
    var codigo by remember { mutableStateOf(salonExistente?.codigo ?: "") }
    var edificio by remember { mutableStateOf(salonExistente?.edificio ?: "Pabellón A") }
    var capacidadText by remember { mutableStateOf(salonExistente?.capacidad?.toString() ?: "40") }
    var ocupadosText by remember { mutableStateOf(salonExistente?.ocupados?.toString() ?: "0") }
    var tipo by remember { mutableStateOf(salonExistente?.tipo ?: "Teoría") }
    var docente by remember { mutableStateOf(salonExistente?.docenteAsignado ?: "Dr. Carlos Mendoza") }
    var horario by remember { mutableStateOf(salonExistente?.horario ?: "Lun y Mié 08:00 - 10:15 AM") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (salonExistente == null) "Crear Nuevo Salón / Aula" else "Editar Horario y Cupos de Salón") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = codigo,
                    onValueChange = { codigo = it },
                    label = { Text("Código de Salón/Aula") },
                    placeholder = { Text("Ej: Aula A-101") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = horario,
                    onValueChange = { horario = it },
                    label = { Text("Horario de Clases") },
                    placeholder = { Text("Ej: Lun y Mié 08:00 - 10:15 AM") },
                    leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = capacidadText,
                        onValueChange = { capacidadText = it },
                        label = { Text("Cupos Máximos") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = ocupadosText,
                        onValueChange = { ocupadosText = it },
                        label = { Text("Ocupados") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = edificio,
                    onValueChange = { edificio = it },
                    label = { Text("Edificio / Pabellón") },
                    placeholder = { Text("Ej: Pabellón B") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = tipo,
                    onValueChange = { tipo = it },
                    label = { Text("Tipo de Aula") },
                    placeholder = { Text("Ej: Teoría, Laboratorio, Virtual") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = docente,
                    onValueChange = { docente = it },
                    label = { Text("Docente Asignado") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cap = capacidadText.toIntOrNull() ?: 40
                    val ocu = ocupadosText.toIntOrNull() ?: 0
                    if (codigo.isNotBlank()) {
                        onSave(codigo, edificio, cap, ocu, tipo, docente, horario)
                    }
                }
            ) {
                Text("Guardar Cambios")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
