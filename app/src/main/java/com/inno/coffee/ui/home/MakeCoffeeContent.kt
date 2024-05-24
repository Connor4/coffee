package com.inno.coffee.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MakeCoffeeContent(modifier: Modifier = Modifier, viewModel: DrinksViewModel = hiltViewModel()) {

    val drinksData by viewModel.drinksTypes.collectAsStateWithLifecycle()
    DrinkList(modifier = modifier, drinksData = drinksData)
}