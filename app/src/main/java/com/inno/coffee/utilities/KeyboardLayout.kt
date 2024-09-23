package com.inno.coffee.utilities

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun KeyboardLayout(
    onKeyClick: (keyName: String) -> Unit,
    onDelete: () -> Unit,
    onEnter: () -> Unit,
) {
    var capsLock by remember {
        mutableStateOf(false)
    }
    var shift by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp)
    ) {
        KeyboardRow1(listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")) {
            onKeyClick(it)
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (capsLock) {
            KeyboardRow1(listOf("q1", "w1", "e1", "r1", "t1", "y1", "u1", "i1", "o1", "p1")) {
                onKeyClick(it)
            }
        } else {
            KeyboardRow1(listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p")) {
                onKeyClick(it)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (capsLock) {
            KeyboardRow2(listOf("a1", "s1", "d1", "f1", "g1", "h1", "j1", "k1", "l1")) {
                onKeyClick(it)
            }
        } else {
            KeyboardRow2(listOf("a", "s", "d", "f", "g", "h", "j", "k", "l")) {
                onKeyClick(it)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (capsLock) {
            KeyboardRow3(listOf("capslock", "z1", "x1", "c1", "v1", "b1", "n1", "m1", "delete"),
                capsLock = true,
                onKeyClick = { onKeyClick(it) }, onCapsLock = { capsLock = it }, onDelete = {
                    onDelete()
                })
        } else {
            KeyboardRow3(listOf("capslock", "z", "x", "c", "v", "b", "n", "m", "delete"),
                capsLock = false,
                onKeyClick = { onKeyClick(it) }, onCapsLock = { capsLock = it }, onDelete = {
                    onDelete()
                })
        }
        Spacer(modifier = Modifier.height(8.dp))
        KeyboardRow4(listOf("shift", "space", "enter"), shift = shift, onKeyClick = {
            onKeyClick(it)
        },
            onShift = { shift = it }, onEnter = { onEnter() })
    }
}

@Composable
private fun KeyboardRow1(keys: List<String>, onKeyClick: (keyName: String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        for (key in keys) {
            KeyboardKey(key) {
                onKeyClick(it)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
private fun KeyboardRow2(keys: List<String>, onKeyClick: (keyName: String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        for (key in keys) {
            KeyboardKey(key) {
                onKeyClick(it)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
private fun KeyboardRow3(keys: List<String>, capsLock: Boolean,
    onKeyClick: (keyName: String) -> Unit,
    onCapsLock: (lock: Boolean) -> Unit = {}, onDelete: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        KeyboardKeyWithClick(keys[0], capsLock, width = 95) {
            onCapsLock(it)
        }
        Spacer(modifier = Modifier.width(8.dp))
        for (i in 1 until keys.size - 1) {
            KeyboardKey(keys[i]) {
                onKeyClick(it)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        KeyboardKey(keys[8], width = 95) {
            onDelete()
        }
    }
}

@Composable
private fun KeyboardRow4(keys: List<String>, shift: Boolean, onKeyClick: (keyName: String) -> Unit,
    onShift: (shift: Boolean) -> Unit = {}, onEnter: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        KeyboardKeyWithClick(keys[0], shift, width = 165) {
            onShift(it)
        }
        Spacer(modifier = Modifier.width(8.dp))
        KeyboardKey(keys[1], width = 340) {
            onKeyClick(it)
        }
        Spacer(modifier = Modifier.width(8.dp))
        KeyboardKey(keys[2], width = 165) {
            onEnter()
        }
    }
}

@Composable
private fun KeyboardKey(keyName: String, width: Int = 60, height: Int = 55,
    onClick: (keyName: String) -> Unit) {
    var isPressed by remember {
        mutableStateOf(false)
    }
    val imageResId = if (isPressed) "keyboard_${keyName}_press_ic"
    else "keyboard_${keyName}_normal_ic"
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(width.dp)
            .height(height.dp)
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    isPressed = true
                    tryAwaitRelease()
                    isPressed = false
                    onClick(keyName)
                })
            },
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
private fun KeyboardKeyWithClick(keyName: String, click: Boolean, width: Int = 60, height: Int = 55,
    onClick: (click: Boolean) -> Unit) {
    val imageResId = if (click) "keyboard_${keyName}_press_ic"
    else "keyboard_${keyName}_normal_ic"
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(width.dp)
            .height(height.dp)
            .debouncedClickable({
                onClick(!click)
            }),
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
    KeyboardLayout({}, {}, {})
}