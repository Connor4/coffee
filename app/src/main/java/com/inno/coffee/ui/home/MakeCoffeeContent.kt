package com.inno.coffee.ui.home

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inno.coffee.data.home.DrinksViewModel

@Composable
fun MakeCoffeeContent(modifier: Modifier = Modifier, viewModel: DrinksViewModel = hiltViewModel()) {
    Surface {
        val drinksData by viewModel.drinksTypes.collectAsStateWithLifecycle()
        DrinkList(modifier = modifier, drinksData = drinksData)

    }
}