package com.inno.common.utils

import android.content.Context

object DimenUtils {

    fun dp2px(context: Context, dp: Float) = dp * context.resources.displayMetrics.density

    fun px2dp(context: Context, px: Float) = px / context.resources.displayMetrics.density
}