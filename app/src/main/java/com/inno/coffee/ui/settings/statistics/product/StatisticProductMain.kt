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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.inno.common.db.entity.ProductCount


@Composable
fun ShowProductStatistic(viewModel: StatisticProductViewModel = hiltViewModel()) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp

    val drinksTypeList by viewModel.drinksType.collectAsState()
    val productCounts by viewModel.productCounts.collectAsState()
    var selectedProduct by remember {
        mutableStateOf(drinksTypeList.firstOrNull())
    }
    LaunchedEffect(drinksTypeList) {
        if (selectedProduct == null && drinksTypeList.isNotEmpty()) {
            selectedProduct = drinksTypeList.first()
        }
    }

    Row(modifier = Modifier.fillMaxSize()) {
        StatisticProductLeftSide(screenWidthDp, productCounts) {
            viewModel.resetData()
        }
        StatisticProductRightSide(screenWidthDp, selectedProduct, drinksTypeList) {
            selectedProduct = it
        }
    }

}

@Composable
private fun StatisticProductRightSide(
    screenWidthDp: Dp,
    selectedProduct: DrinksModel?,
    drinksTypeList: List<DrinksModel>,
    onProductClick: (model: DrinksModel) -> Unit,
) {
    Row(
        modifier = Modifier
            .width(screenWidthDp / 2)
            .padding(16.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(50.dp))
            Text(text = "*产品杯数 ${selectedProduct?.productId}", style = MaterialTheme.typography
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
    productCounts: List<ProductCount>,
    onResetClick: () -> Unit,
) {
    Column(
        modifier = Modifier.width(screenWidthDp / 2)
    ) {
        Button(onClick = { onResetClick() }) {
            Text(text = stringResource(id = R.string.statistic_reset_all),
                style = MaterialTheme.typography.titleLarge)
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = stringResource(id = R.string.statistic_last_reset, "2024/08/08"),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Column(
                modifier = Modifier.width(screenWidthDp / 4)
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "*咖啡杯数", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "*热水杯数", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "*奶类杯数", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "*奶泡杯数", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "*蒸汽次数", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "*总杯数", style = MaterialTheme.typography.displaySmall)
            }
            // TODO 显示数据
            Column(
                modifier = Modifier.width(screenWidthDp / 4)
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "*2", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "*4", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "*1", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "*5", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "*2", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "*12", style = MaterialTheme.typography.displaySmall)
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