package com.inno.coffee.ui.settings.formula

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inno.coffee.viewmodel.settings.formula.FormulaViewModel
import com.inno.common.db.entity.Formula

@Composable
fun FormulaPage(modifier: Modifier = Modifier, viewModel: FormulaViewModel = hiltViewModel()) {
    val formulaList by viewModel.formulaList.collectAsStateWithLifecycle()

    Surface(modifier = modifier
        .width(300.dp)
        .fillMaxHeight()) {
        val context = LocalContext.current
        val showDialog by rememberSaveable {
            viewModel.fileNotFoundDialogFlag
        }

        if (showDialog) {
            FileNotFoundDialog(onDismiss = { viewModel.dismissFileNotFoundDialog() })
        }

        Column {
            Text(text = "配方")

            Row {
                Button(modifier = modifier.padding(start = 10.dp, top = 10.dp), onClick = {
                    viewModel.loadFromSdCard(context)
                }) {
                    Text(text = "load from sdcard")
                }

                Button(modifier = modifier.padding(start = 20.dp, top = 10.dp), onClick = {
                    val formula = Formula("coffee", "意式", "前", 20, 50, 20, 29)
                    viewModel.insertFormula(formula)
                }) {
                    Text(text = "add formula")
                }
            }

            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                items(items = formulaList) {
                    FormulaItem(formula = it)
                }
            }
        }
    }
}

@Composable
fun FileNotFoundDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "File Not Found") },
        text = {
            Text("The requested file does not exist. Please check the file path and try again.")
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Composable
fun FormulaItem(formula: Formula, modifier: Modifier = Modifier) {
    Row {
        Text(text = formula.productType)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = formula.productName)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = formula.vat)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = formula.waterDosage.toString())
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = formula.coffeeDosage.toString())
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = formula.pressure.toString())
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = formula.preSoakingTime.toString())
        Spacer(modifier = Modifier.width(5.dp))
    }
}

@Preview
@Composable
fun PreviewFormula() {
    FormulaPage()
}