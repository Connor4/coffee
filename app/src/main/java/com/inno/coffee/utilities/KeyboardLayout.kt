package com.inno.coffee.utilities

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun KeyboardLayout() {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp)
    ) {
        KeyboardRow1(listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"))
        Spacer(modifier = Modifier.height(8.dp))
        KeyboardRow1(listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p"))
        Spacer(modifier = Modifier.height(8.dp))
        KeyboardRow2(listOf("a", "s", "d", "f", "g", "h", "j", "k", "l"))
        Spacer(modifier = Modifier.height(8.dp))
        KeyboardRow3(listOf("capslock", "z", "x", "c", "v", "b", "n", "m", "delete"))
        Spacer(modifier = Modifier.height(8.dp))
        KeyboardRow4(listOf("shift", "space", "enter"))
    }
}

@Composable
private fun KeyboardRow1(keys: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        for (key in keys) {
            KeyboardKey(key)
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
private fun KeyboardRow2(keys: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        for (key in keys) {
            KeyboardKey(key)
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
private fun KeyboardRow3(keys: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        KeyboardKey(keys[0], width = 95)
        Spacer(modifier = Modifier.width(8.dp))
        for (i in 1 until keys.size - 1) {
            KeyboardKey(keys[i])
            Spacer(modifier = Modifier.width(8.dp))
        }
        KeyboardKey(keys[8], width = 95)
    }
}

@Composable
private fun KeyboardRow4(keys: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        KeyboardKey(keys[0], width = 165)
        Spacer(modifier = Modifier.width(8.dp))
        KeyboardKey(keys[1], width = 340)
        Spacer(modifier = Modifier.width(8.dp))
        KeyboardKey(keys[2], width = 165)
    }
}

@Composable
private fun KeyboardKey(keyName: String, width: Int = 60, height: Int = 55) {
    val imageResId = "keyboard_${keyName}_normal_ic"
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(width.dp)
            .height(height.dp)
    ) {
        val resId = getImageResId(imageResId)
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun getImageResId(imageName: String): Int {
    val context = LocalContext.current
    return context.resources.getIdentifier(imageName, "drawable", context.packageName)
}

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewKeyBoard() {
    KeyboardLayout()
}