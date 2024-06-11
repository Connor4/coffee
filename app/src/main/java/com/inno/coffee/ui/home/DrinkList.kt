package com.inno.coffee.ui.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inno.coffee.R
import com.inno.coffee.data.home.DrinksModel
import kotlinx.coroutines.delay

@Composable
fun DrinkList(
    modifier: Modifier = Modifier,
    drinksData: List<DrinksModel> = emptyList()
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(drinksData, key = { drink -> drink.hashCode() }) { drink ->
            val index = drinksData.indexOf(drink)
            val column = index % 4
            AnimatedDrinkItem(model = drink, columnIndex = column, modifier = modifier)
        }
    }
}

@Composable
fun AnimatedDrinkItem(model: DrinksModel, columnIndex: Int, modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp

    val density = LocalDensity.current
    val screenWidthPx = with(density) { screenWidthDp.toPx() }

    var startAnimation by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = model) {
        delay(columnIndex * 100L)
        startAnimation = true
    }
    val offsetX = remember {
        Animatable(screenWidthPx)
    }
    LaunchedEffect(key1 = startAnimation) {
        if (startAnimation) {
            offsetX.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 1200,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    DrinkItem(model = model, offsetX = offsetX.value, modifier = modifier)
}

@Composable
fun DrinkItem(model: DrinksModel, offsetX: Float, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.offset(x = offsetX.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.small
    ) {
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(80.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column {
                Text(
                    text = "$${model.price}",
                    fontSize = 6.sp,
                    modifier = Modifier.padding(start = 5.dp, top = 5.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Image(
                    painter = painterResource(id = model.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = model.name,
                    fontSize = 6.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Preview
@Composable
fun DrinksItemPreview() {
    DrinkItem(model = DrinksModel(20, "美式咖啡", R.drawable.coffee1), offsetX = 0f)
//    val drinks = listOf(
//        DrinksModel(50, "Coffee", android.R.drawable.ic_menu_report_image),
//        DrinksModel(20, "Tea", android.R.drawable.ic_menu_report_image),
//        DrinksModel(30, "Juice", android.R.drawable.ic_menu_report_image),
//        DrinksModel(18, "Water", android.R.drawable.ic_menu_report_image)
//    )
//    DrinkList(drinksData = drinks)
}