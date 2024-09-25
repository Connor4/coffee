package com.inno.coffee.ui.common

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.SystemClock
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.core.content.ContextCompat
import com.inno.coffee.utilities.VIEW_FAST_CLICK_INTERVAL_TIME
import kotlinx.coroutines.delay

/**
 * usage:
 * Column(modifier = Modifier.debouncedClickable{ // do what you want to do here })
 */
@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.debouncedClickable(onClick: () -> Unit, enabled: Boolean = true, delay: Long = 300) =
    composed {
        var clicked by remember {
            mutableStateOf(!enabled)
        }
        LaunchedEffect(key1 = clicked) {
            if (clicked) {
                delay(delay)
                clicked = !clicked
            }
        }
        Modifier.clickable(if (enabled) !clicked else false) {
            clicked = !clicked
            onClick()
        }
    }

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.debouncedClickableWithoutRipple(onClick: () -> Unit, enabled: Boolean = true,
    delay: Long = 300) =
    composed {
        var clicked by remember {
            mutableStateOf(!enabled)
        }
        LaunchedEffect(key1 = clicked) {
            if (clicked) {
                delay(delay)
                clicked = !clicked
            }
        }
        Modifier.clickable(indication = null, enabled = if (enabled) !clicked else false,
            interactionSource = remember {
                MutableInteractionSource()
            }) {
            clicked = !clicked
            onClick()
        }
    }


/**
 * usage:
 * Text(text = "", modifier = Modifier.fastclick{ // do what you want to do here })
 */
@SuppressLint("ModifierFactoryUnreferencedReceiver")
@Composable
inline fun Modifier.fastclick(
    time: Int = VIEW_FAST_CLICK_INTERVAL_TIME,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    crossinline onClick: () -> Unit,
): Modifier {
    var lastClickTime by remember {
        mutableLongStateOf(0)
    }
    return clickable(enabled = enabled, onClickLabel = onClickLabel, role = role) {
        val currentTimeMills = SystemClock.elapsedRealtime()
        if (currentTimeMills - time >= lastClickTime) {
            onClick()
            lastClickTime = currentTimeMills
        }
    }
}

/**
 * usage:
 * Text(text = "", modifier = Modifier.fastclickWithoutRipple{ // do what you want to do here })
 */
@SuppressLint("ModifierFactoryUnreferencedReceiver")
@Composable
inline fun Modifier.fastclickWithoutRipple(
    time: Int = VIEW_FAST_CLICK_INTERVAL_TIME,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    crossinline onClick: () -> Unit,
): Modifier {
    var lastClickTime by remember {
        mutableLongStateOf(0)
    }
    return clickable(enabled = enabled, onClickLabel = onClickLabel, role = role,
        indication = null, interactionSource = remember {
            MutableInteractionSource()
        }) {
        val currentTimeMills = SystemClock.elapsedRealtime()
        if (currentTimeMills - time >= lastClickTime) {
            onClick()
            lastClickTime = currentTimeMills
        }
    }
}

/**
 * usage：
 * Button(onClick = composeClick { // do what you want to do here }){}
 */
@Composable
inline fun composeClick(
    time: Int = VIEW_FAST_CLICK_INTERVAL_TIME,
    crossinline onClick: () -> Unit,
): () -> Unit {
    var lastClickTime by remember {
        mutableLongStateOf(0)
    }
    return {
        val currentTimeMillis = SystemClock.elapsedRealtime()
        if (currentTimeMillis - time >= lastClickTime) {
            onClick()
            lastClickTime = currentTimeMillis
        }
    }
}

/**
 * usage：
 * Text(text = "", modifier = Modifier.doubleTap{// do what you want to do here })
 */
fun Modifier.doubleTap(onDoubleTapUnit: (Offset) -> Unit = {}): Modifier =
    pointerInput(this) {
        detectTapGestures(
            onDoubleTap = onDoubleTapUnit
        )

    }

/**
 * usage：
 * Text(text = "", modifier = Modifier.longPress{// do what you want to do here })
 */
fun Modifier.longPress(onLongPressUnit: (Offset) -> Unit = {}): Modifier =
    pointerInput(this) {
        detectTapGestures(onLongPress = onLongPressUnit)
    }

fun Modifier.draw9Patch(
    context: Context,
    @DrawableRes ninePatchRes: Int,
) = this.drawBehind {
    drawIntoCanvas {
        ContextCompat.getDrawable(context, ninePatchRes)?.let { ninePatch ->
            ninePatch.run {
                bounds = Rect(0, 0, size.width.toInt(), size.height.toInt())
                draw(it.nativeCanvas)
            }
        }
    }
}