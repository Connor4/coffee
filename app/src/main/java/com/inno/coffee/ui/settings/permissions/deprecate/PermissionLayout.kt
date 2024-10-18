package com.inno.coffee.ui.settings.permissions.deprecate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.inno.coffee.ui.settings.permissions.PermissionMainLayout

@Composable
fun PermissionLayout(
    onCloseClick: () -> Unit,
) {
    var loginSuccess by remember { mutableStateOf(false) }

    if (!loginSuccess) {
        PermissionInputLayout(
            onCloseClick = {
                onCloseClick()
            },
            onLoginSuccess = {
                loginSuccess = true
            }
        )
    } else {
        PermissionMainLayout({
            onCloseClick()
        })
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewPermissionLayout() {
    PermissionLayout({})
}