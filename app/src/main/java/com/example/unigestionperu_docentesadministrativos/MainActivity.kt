package com.example.unigestionperu_docentesadministrativos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.unigestionperu_docentesadministrativos.ui.navigation.AppNavigation
import com.example.unigestionperu_docentesadministrativos.ui.theme.UniGestionPeruDocentesAdministrativosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniGestionPeruDocentesAdministrativosTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}
