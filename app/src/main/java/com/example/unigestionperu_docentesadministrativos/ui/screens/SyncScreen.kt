package com.example.unigestionperu_docentesadministrativos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unigestionperu_docentesadministrativos.data.local.entities.OperacionPendienteEntity
import com.example.unigestionperu_docentesadministrativos.ui.components.ConnectivityBanner
import com.example.unigestionperu_docentesadministrativos.viewmodel.SyncViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncScreen(
    syncViewModel: SyncViewModel,
    onBack: () -> Unit
) {
    val isConnected by syncViewModel.isConnected.collectAsState()
    val operaciones by syncViewModel.operacionesPendientes.collectAsState()
    val totalPendientes by syncViewModel.totalPendientes.collectAsState()
    val metadataList by syncViewModel.metadataList.collectAsState()
    val isSyncing by syncViewModel.isSyncing.collectAsState()
    val mensajeEstado by syncViewModel.mensajeEstado.collectAsState()

    val ultimaSync = metadataList.find { it.recurso == "general" }?.ultimaSincronizacionExitosa
    val fechaFormatted = if (ultimaSync != null) {
        SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date(ultimaSync))
    } else "Nunca"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sincronización Offline REST") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0202C6),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ConnectivityBanner(isConnected = isConnected)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Tarjeta de Resumen
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CloudSync, contentDescription = null, tint = Color(0xFF0202C6))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Estado de Cola Local", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Operaciones Pendientes: $totalPendientes", fontSize = 14.sp)
                        Text("Última Sincronización Exitosa: $fechaFormatted", fontSize = 12.sp, color = Color.Gray)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = { syncViewModel.sincronizarAhora() },
                                enabled = !isSyncing,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0202C6))
                            ) {
                                if (isSyncing) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                } else {
                                    Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                Text("Sincronizar Ahora")
                            }

                            OutlinedButton(
                                onClick = { syncViewModel.limpiarSincronizadas() }
                            ) {
                                Icon(Icons.Default.DeleteSweep, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Limpiar completadas")
                            }
                        }
                    }
                }

                mensajeEstado?.let { msg ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = msg, fontSize = 13.sp, color = Color(0xFF6802C1), fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Cola de Operaciones Locales (Room)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))

                if (operaciones.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay operaciones pendientes en cola.", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(operaciones) { item ->
                            OperacionPendienteItem(item = item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OperacionPendienteItem(item: OperacionPendienteEntity) {
    val colorEstado = when (item.estado) {
        "PENDIENTE" -> Color(0xFFFF9800)
        "ENVIANDO" -> Color(0xFF2196F3)
        "SINCRONIZADO" -> Color(0xFF4CAF50)
        else -> Color(0xFFF44336)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(colorEstado, shape = RoundedCornerShape(6.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${item.tipoOperacion} — ${item.entidad.uppercase()}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text("UUID: ${item.uuidOperacion.take(18)}...", fontSize = 11.sp, color = Color.Gray)
                if (item.mensajeError != null) {
                    Text("Error: ${item.mensajeError}", fontSize = 11.sp, color = Color.Red)
                }
            }
            Text(
                text = item.estado,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = colorEstado
            )
        }
    }
}
