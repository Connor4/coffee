package com.inno.coffee.ui.home

import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inno.coffee.data.home.DrinksViewModel
import com.inno.coffee.ui.settings.launchSettingActivity

@Composable
fun MakeCoffeeContent(modifier: Modifier = Modifier, viewModel: DrinksViewModel = hiltViewModel()) {

    Surface {
        val drinksData by viewModel.drinksTypes.collectAsStateWithLifecycle()
        DrinkList(modifier = modifier, drinksData = drinksData)
        val context = LocalContext.current
        Button(onClick = {
            launchSettingActivity(context)
        }) {
            Text(text = "跳转设置页")
        }
    }

}