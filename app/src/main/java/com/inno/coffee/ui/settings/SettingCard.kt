package com.inno.coffee.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R

@Composable
fun SettingCardLayout(
    modifier: Modifier = Modifier
) {
    val list = mutableListOf<Int>()
    for (i in 0..11) {
        list.add(i)
    }
    Surface {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = modifier.fillMaxSize()
        ) {
            items(list) {
                CardItem()
            }
        }
    }

}

@Composable
fun CardItem() {
    Card(
        modifier = Modifier.clickable {

        },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.coffee6),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Preview(device = Devices.TABLET)
@Composable
fun PreviewCardItem() {
    SettingCardLayout()
}