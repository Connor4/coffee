package com.inno.coffee.ui.settings.statistics.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.inno.coffee.R
import com.inno.coffee.data.DrinksModel
import com.inno.coffee.utilities.debouncedClickable
import com.inno.coffee.viewmodel.settings.statistics.StatisticProductViewModel
import com.inno.common.db.entity.ProductTypeCount
import com.inno.common.enums.ProductType


@Composable
fun ShowProductStatistic(viewModel: StatisticProductViewModel = hiltViewModel()) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp

    val drinksTypeList by viewModel.drinksType.collectAsState()
    val typeCounts by viewModel.typeCounts.collectAsState()
    val selectedProductCount by viewModel.productCount.collectAsState()
    val time by viewModel.time.collectAsState()
    LaunchedEffect(Unit) {
        if (drinksTypeList.isNotEmpty()) {
            val productId = drinksTypeList.first().productId
            viewModel.getProductCount(productId)
        }
    }

    Row(modifier = Modifier.fillMaxSize()) {
        StatisticProductLeftSide(screenWidthDp, typeCounts, time) {
            viewModel.resetData()
        }
        StatisticProductRightSide(screenWidthDp, selectedProductCount?.count ?: 0,
            drinksTypeList) {
            viewModel.getProductCount(it.productId)
        }
    }

}

@Composable
private fun StatisticProductRightSide(
    screenWidthDp: Dp,
    selectedProductCount: Int,
    drinksTypeList: List<DrinksModel>,
    onProductClick: (model: DrinksModel) -> Unit,
) {
    val text = stringResource(id = R.string.statistic_product_single_counter)
    Row(
        modifier = Modifier
            .width(screenWidthDp / 2)
            .padding(16.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(50.dp))
            Text(text = "$text: $selectedProductCount", style = MaterialTheme.typography
                .displaySmall)
            Spacer(modifier = Modifier.height(30.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 8.dp),
            ) {
                items(drinksTypeList) { drinksModel ->
                    StatisticDrinkItem(model = drinksModel) {
                        onProductClick(drinksModel)
                    }
                }
            }
        }
    }

}

@Composable
private fun StatisticProductLeftSide(
    screenWidthDp: Dp,
    typeCounts: List<ProductTypeCount>,
    time: String,
    onResetClick: () -> Unit,
) {
    val coffee = lookForCount(typeCounts, ProductType.COFFEE)
    val hotWater = lookForCount(typeCounts, ProductType.HOT_WATER)
    val milk = lookForCount(typeCounts, ProductType.MILK)
    val foam = lookForCount(typeCounts, ProductType.FOAM)
    val steam = lookForCount(typeCounts, ProductType.STEAM)
    val total = coffee + hotWater + milk + foam + steam

    Column(
        modifier = Modifier.width(screenWidthDp / 2)
    ) {
        Button(onClick = { onResetClick() }) {
            Text(text = stringResource(id = R.string.statistic_reset_all),
                style = MaterialTheme.typography.titleLarge)
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = stringResource(id = R.string.statistic_last_reset, time),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Column(
                modifier = Modifier.width(screenWidthDp / 4)
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = stringResource(id = R.string.statistic_product_coffee_counter),
                    style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = stringResource(id = R.string.statistic_product_hotwater_counter),
                    style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = stringResource(id = R.string.statistic_product_milk_counter),
                    style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = stringResource(id = R.string.statistic_product_foam_counter),
                    style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = stringResource(id = R.string.statistic_product_steam_counter),
                    style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = stringResource(id = R.string.statistic_product_total_counter),
                    style = MaterialTheme.typography.displaySmall)
            }
            Column(
                modifier = Modifier.width(screenWidthDp / 4)
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "$coffee", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "$hotWater", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "$milk", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "$foam", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "$steam", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "$total", style = MaterialTheme.typography.displaySmall)
            }
        }
    }
}

@Composable
private fun StatisticDrinkItem(model: DrinksModel, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .debouncedClickable({ onItemClick() })
    ) {
        AsyncImage(
            model = model.imageRes,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = model.name),
            fontSize = 15.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }
}

private fun lookForCount(list: List<ProductTypeCount>, type: ProductType): Int {
    val typeCount = list.find { it.type == type }
    return typeCount?.totalCount ?: 0
}