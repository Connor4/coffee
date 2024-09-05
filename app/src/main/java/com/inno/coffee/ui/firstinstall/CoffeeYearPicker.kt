package com.inno.coffee.ui.firstinstall

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.utilities.nsp

private const val YearsInRow: Int = 3

@Composable
fun CoffeeYearPicker(
    yearRange: IntRange = IntRange(1970, 2100)
) {
    val currentYear = 2024
    val displayedYear = 2024
    LazyVerticalGrid(
        columns = GridCells.Fixed(YearsInRow),
        modifier = Modifier
            .width(700.dp)
            .height(436.dp)
            .background(Color.LightGray),
        contentPadding = PaddingValues(horizontal = 0.dp), // No padding for a compact layout
        horizontalArrangement = Arrangement.spacedBy(0.dp), // No space between columns
        verticalArrangement = Arrangement.spacedBy(0.dp) // No space between rows
    ) {
        items(yearRange.count()) {
            val selectedYear = it + yearRange.first
            val localizedYear = selectedYear.toString()
            Year(selected = selectedYear == displayedYear, onClick = { /*TODO*/ }) {
                Text(
                    text = localizedYear,
                    textAlign = TextAlign.Center,
                    fontSize = 6.nsp(),
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
private fun Year(
    selected: Boolean = false,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .height(48.dp),
        selected = selected,
        onClick = onClick,
        color = if (selected) Color.Gray else Color.White,
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF191A1D)),
            contentAlignment = Alignment.Center) {
            content()
        }
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewYear() {
    CoffeeYearPicker()
}