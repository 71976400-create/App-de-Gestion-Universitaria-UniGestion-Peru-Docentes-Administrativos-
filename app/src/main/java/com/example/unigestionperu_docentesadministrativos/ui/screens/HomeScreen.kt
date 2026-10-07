package com.example.unigestionperu_docentesadministrativos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.unigestionperu_docentesadministrativos.data.local.entities.UsuarioEntity
import com.example.unigestionperu_docentesadministrativos.ui.components.GestionSalonesComponent
import com.example.unigestionperu_docentesadministrativos.viewmodel.TeacherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    docente: UsuarioEntity?,
    teacherViewModel: TeacherViewModel,
    onCourseClick: (Long) -> Unit,
    onOpenSync: () -> Unit = {},
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val cursos by teacherViewModel.cursosDocente.collectAsState(initial = emptyList())
    val salones by teacherViewModel.allSalones.collectAsState(initial = emptyList())

    LaunchedEffect(docente) {
        if (docente != null) {
            teacherViewModel.setDocenteId(docente.id)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("UniGestión Perú - Portal Docente") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onOpenSync) {
                        Icon(Icons.Default.CloudSync, contentDescription = "Sincronización Offline")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar Sesión")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Book, contentDescription = null) },
                    label = { Text("Mis Cursos") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.MeetingRoom, contentDescription = null) },
                    label = { Text("Salones") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    label = { Text("Horarios") }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Search, contentDescription = null) },
                    label = { Text("Buscar") }
                )
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> DocenteHomeScreen(
                docente = docente,
                teacherViewModel = teacherViewModel,
                onCourseClick = onCourseClick,
                onLogout = onLogout
            )
            1 -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                GestionSalonesComponent(
                    salones = salones,
                    onAgregarSalon = { codigo, edificio, capacidad, ocupados, tipo, docenteAsignado, horario ->
                        teacherViewModel.agregarSalon(codigo, edificio, capacidad, ocupados, tipo, docenteAsignado, horario) {}
                    },
                    onActualizarSalon = { salon ->
                        teacherViewModel.actualizarSalon(salon) {}
                    },
                    onEliminarSalon = { salon ->
                        teacherViewModel.eliminarSalon(salon) {}
                    }
                )
            }
            2 -> CalendarioScreen(paddingValues = padding)
            3 -> ListScreen(
                cursos = cursos,
                onCourseClick = onCourseClick,
                paddingValues = padding
            )
        }
    }
}
