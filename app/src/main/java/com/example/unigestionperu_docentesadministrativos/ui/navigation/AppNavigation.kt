package com.example.unigestionperu_docentesadministrativos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.unigestionperu_docentesadministrativos.ui.screens.*
import com.example.unigestionperu_docentesadministrativos.viewmodel.AdminViewModel
import com.example.unigestionperu_docentesadministrativos.viewmodel.AuthViewModel
import com.example.unigestionperu_docentesadministrativos.viewmodel.TeacherViewModel

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Home : Screen("home")
    object AdminHome : Screen("admin_home")
    object CursoDetail : Screen("curso_detail/{cursoId}") {
        fun createRoute(cursoId: Long) = "curso_detail/$cursoId"
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val teacherViewModel: TeacherViewModel = viewModel()
    val adminViewModel: AdminViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { rol ->
                    if (rol == "Docente") {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else if (rol == "Administrativo") {
                        navController.navigate(Screen.AdminHome.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            val usuarioLogueado = authViewModel.uiState.usuarioLogueado
            HomeScreen(
                docente = usuarioLogueado,
                teacherViewModel = teacherViewModel,
                onCourseClick = { cursoId ->
                    navController.navigate(Screen.CursoDetail.createRoute(cursoId))
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.AdminHome.route) {
            val usuarioLogueado = authViewModel.uiState.usuarioLogueado
            AdministrativoHomeScreen(
                admin = usuarioLogueado,
                adminViewModel = adminViewModel,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.AdminHome.route) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Screen.CursoDetail.route,
            arguments = listOf(navArgument("cursoId") { type = NavType.LongType })
        ) { backStackEntry ->
            val cursoId = backStackEntry.arguments?.getLong("cursoId") ?: 0L
            CursoDetailScreen(
                cursoId = cursoId,
                teacherViewModel = teacherViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
