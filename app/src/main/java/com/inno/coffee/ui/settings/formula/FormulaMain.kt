package com.inno.coffee.ui.settings.formula

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inno.coffee.R
import com.inno.coffee.utilities.debouncedClickable
import com.inno.coffee.viewmodel.settings.formula.FormulaViewModel
import com.inno.common.db.entity.Formula

@Composable
fun FormulaMain(modifier: Modifier = Modifier, viewModel: FormulaViewModel = hiltViewModel()) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp

    val formulaList by viewModel.formulaList.collectAsStateWithLifecycle()
    var selectedFormula by remember { mutableStateOf(formulaList.firstOrNull()) }

    LaunchedEffect(formulaList) {
        if (selectedFormula == null && formulaList.isNotEmpty()) {
            selectedFormula = formulaList.first()
        }
    }
    val showDialog by rememberSaveable {
        viewModel.loadFileErrorDialogFlag
    }

    if (showDialog) {
        FileNotFoundDialog(onDismiss = { viewModel.dismissFileNotFoundDialog() })
    }

    Row(modifier = modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier
                .width(screenWidthDp / 2)
                .padding(16.dp)
        ) {
            Column {
                Row {
                    val context = LocalContext.current
                    Button(onClick = { viewModel.loadFromSdCard(context) }) {
                        Text(text = "Load from SD Card")
                    }

                    Button(
                        modifier = Modifier.padding(start = 20.dp),
                        onClick = {
                            val formula = Formula(
                                productId = 1, productType = "coffee", productName = "意式",
                                vat = "前方豆缸",
                                coffeeWater = 20, powderDosage = 50, pressWeight = 20,
                                preMakeTime = 29, postPreMakeWaitTime = 30, secPressWeight = 40,
                                hotWater = 20, americanoSequence = 30, coffeeCycles = 1,
                                bypassWater = 1
                            )
                            viewModel.insertFormula(formula)
                        }
                    ) {
                        Text(text = "Add Formula")
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                ) {
                    items(items = formulaList) { itemData ->
                        FormulaItem(formula = itemData) {
                            selectedFormula = it // Update the selected formula
                        }
                    }
                }
            }
        }

        Surface(
            modifier = Modifier
                .width(screenWidthDp / 2)
                .padding(16.dp)
        ) {
            selectedFormula?.let {
                ItemList(formula = it, modifier = Modifier.fillMaxSize())
            } ?: Text(
                text = "Select a formula to see details",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


@Composable
fun FileNotFoundDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Load File Error") },
        text = {
            Text("There is something wrong. Please check the file and try again.")
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(id = R.string.common_button_confirm))
            }
        }
    )
}

@Composable
fun FormulaItem(
    modifier: Modifier = Modifier,
    formula: Formula,
    onItemClick: (model: Formula) -> Unit = {},
) {
    Row(
        modifier = modifier
            .height(30.dp)
            .padding(5.dp)
            .debouncedClickable({ onItemClick(formula) })
    ) {
        Text(text = "${formula.productId}")
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = formula.productType)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = formula.productName)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = formula.vat)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = formula.coffeeWater.toString())
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = formula.powderDosage.toString())
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = formula.pressWeight.toString())
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = formula.preMakeTime.toString())
        Spacer(modifier = Modifier.width(5.dp))
    }
}

@Composable
fun ItemList(formula: Formula, modifier: Modifier) {
    Column(
        modifier = modifier
            .width(300.dp)
            .height(700.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        Row {
            Text(
                text = stringResource(id = R.string.formula_product_type),
                modifier = Modifier.width(150.dp) // Set a fixed width
            )
            Text(text = formula.productType)
        }
        Row {
            Text(
                text = stringResource(id = R.string.formula_product_name),
                modifier = Modifier.width(150.dp) // Set the same fixed width
            )
            Text(text = formula.productName)
        }
        Row {
            Text(
                text = stringResource(id = R.string.formula_vat_position),
                modifier = Modifier.width(150.dp)
            )
            Text(text = formula.vat)
        }
        Row {
            Text(
                text = stringResource(id = R.string.formula_water_dosage),
                modifier = Modifier.width(150.dp)
            )
            Text(text = "${formula.coffeeWater}")
        }
        Row {
            Text(
                text = stringResource(id = R.string.formula_powder_dosage),
                modifier = Modifier.width(150.dp)
            )
            Text(text = "${formula.powderDosage}")
        }
        Row {
            Text(
                text = stringResource(id = R.string.formula_press_weight),
                modifier = Modifier.width(150.dp)
            )
            Text(text = "${formula.pressWeight}")
        }
        Row {
            Text(
                text = stringResource(id = R.string.formula_pre_make_time),
                modifier = Modifier.width(150.dp)
            )
            Text(text = "${formula.preMakeTime}")
        }
        Row {
            Text(
                text = stringResource(id = R.string.formula_pre_make_wait_time),
                modifier = Modifier.width(150.dp)
            )
            Text(text = "${formula.postPreMakeWaitTime}")
        }
        Row {
            Text(
                text = stringResource(id = R.string.formula_second_press_weight),
                modifier = Modifier.width(150.dp)
            )
            Text(text = "${formula.secPressWeight}")
        }
        Row {
            Text(
                text = stringResource(id = R.string.formula_hot_water_dosage),
                modifier = Modifier.width(150.dp)
            )
            Text(text = "${formula.hotWater}")
        }
        Row {
            Text(
                text = stringResource(id = R.string.formula_americano_seq),
                modifier = Modifier.width(150.dp)
            )
            Text(text = "${formula.americanoSequence}")
        }
        Row {
            Text(
                text = stringResource(id = R.string.formula_coffee_cycles),
                modifier = Modifier.width(150.dp)
            )
            Text(text = "${formula.coffeeCycles}")
        }
        Row {
            Text(
                text = stringResource(id = R.string.formula_bypass_dosage),
                modifier = Modifier.width(150.dp)
            )
            Text(text = "${formula.bypassWater}")
        }
    }
}


@Preview(device = Devices.TABLET, showBackground = true)
@Composable
fun PreviewFormula() {
    ItemList(formula = Formula(1, "coffee", "意式", "前",
        20, 50, 20, 29,
        30, 40, 20, 30, 1, 1),
        modifier = Modifier)
}