package com.inno.coffee.utilities

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Int.nsp(): TextUnit {
    return getRealDp(LocalContext.current, this.toDouble(), isDp = false).sp
}

@Composable
fun Float.nsp(): TextUnit {
    return getRealDp(LocalContext.current, this.toDouble(), isDp = false).sp
}

@Composable
fun Double.nsp(): TextUnit {
    return getRealDp(LocalContext.current, this, isDp = false).sp
}

@Composable
fun Int.ndp(): Dp {
    return getRealDp(LocalContext.current, this.toDouble()).dp
}

@Composable
fun Float.ndp(): Dp {
    return getRealDp(LocalContext.current, this.toDouble()).dp
}

@Composable
fun Double.ndp(): Dp {
    return getRealDp(LocalContext.current, this).dp
}

private fun getRealDp(context: Context, value: Double, isDp: Boolean = true): Double {
    val density =
        if (isDp) context.resources.displayMetrics.density
        else context.resources.displayMetrics.scaledDensity
    val screenWidth =
        (if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            context.resources.displayMetrics.heightPixels
        else context.resources.displayMetrics.widthPixels) / 375
    return screenWidth * 2 * value / density
}