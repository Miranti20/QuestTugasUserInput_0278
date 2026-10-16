package com.example.tugas5.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.example.tugas5.R

/** Palet tetap dari resources agar sesuai contoh tugas pada setiap perangkat. */
@Composable
fun Tugas5Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = colorResource(R.color.form_primary),
            onPrimary = colorResource(R.color.form_surface),
            primaryContainer = colorResource(R.color.form_primary_container),
            onPrimaryContainer = colorResource(R.color.form_primary),
            secondary = colorResource(R.color.form_primary),
            background = colorResource(R.color.form_background),
            onBackground = colorResource(R.color.form_text),
            surface = colorResource(R.color.form_surface),
            onSurface = colorResource(R.color.form_text),
            surfaceVariant = colorResource(R.color.form_surface),
            onSurfaceVariant = colorResource(R.color.form_hint),
            outline = colorResource(R.color.form_outline),
            error = colorResource(R.color.form_error)
        ),
        typography = Typography,
        content = content
    )
}
