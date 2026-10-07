package com.example.unigestionperu_docentesadministrativos.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.unigestionperu_docentesadministrativos.R

@Composable
fun AppLogo(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.ic_logo_unigestion),
        contentDescription = "UniGestión Perú Logo",
        contentScale = ContentScale.Fit,
        modifier = modifier.size(120.dp)
    )
}
