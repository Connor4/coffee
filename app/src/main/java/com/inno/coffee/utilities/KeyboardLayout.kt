package com.inno.coffee.utilities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            .fillMaxWidth()
            .background(Color.Black)
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
private fun KeyboardRow(keys: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (key in keys) {
            KeyboardKey(key)
        }
    }
}

@Composable
private fun KeyboardRow1(keys: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (key in keys) {
            KeyboardKey(key)
        }
    }
}

@Composable
private fun KeyboardRow2(keys: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (key in keys) {
            KeyboardKey(key)
        }
    }
}

@Composable
private fun KeyboardRow3(keys: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (key in keys) {
            KeyboardKey(key)
        }
    }
}

@Composable
private fun KeyboardRow4(keys: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (key in keys) {
            KeyboardKey(key)
        }
    }
}

@Composable
private fun KeyboardKey(keyName: String) {
    // 拼接资源名，例如：keyboard_a_normal_ic
    val imageResId = "keyboard_${keyName}_normal_ic"

    // 使用键盘图片
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(48.dp)
            .height(64.dp)
            .background(Color.Gray, RoundedCornerShape(8.dp))
    ) {
        // 从资源中加载键位图片
        val resId = getImageResId(imageResId)  // 使用自定义的函数获取图片资源ID
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(48.dp)
                .height(64.dp)
        )
    }
}

// 根据名称获取图片资源ID
@Composable
private fun getImageResId(imageName: String): Int {
    // 这里假设所有资源文件名符合规则 "keyboard_键盘字母_normal_ic"
    val context = LocalContext.current
    return context.resources.getIdentifier(imageName, "drawable", context.packageName)
}

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewKeyBoard() {
    KeyboardLayout()
}