package com.inno.coffee.ui.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inno.coffee.ui.settings.formula.FormulaPage
import com.inno.coffee.ui.settings.permissions.PermissionPage
import com.inno.coffee.ui.settings.statictics.DrinksHistoryList
import com.inno.coffee.ui.theme.CoffeeTheme
import dagger.hilt.android.AndroidEntryPoint

fun launchSettingActivity(context: Context) {
    context.startActivity(Intent(context, SettingActivity::class.java))
    (context as? Activity)?.overridePendingTransition(0, 0)
}

/**
 * 后台操作页面
 */
@AndroidEntryPoint
class SettingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeTheme {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(listOf(1, 2, 3)) { item ->
                        when (item) {
                            1 -> DrinksHistoryList()
                            2 -> FormulaPage()
                            3 -> PermissionPage()
                        }
                    }
                }
            }
        }
    }

}

