package com.inno.coffee.ui.home.deprecate

import android.view.Gravity
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowProvider
import coil.compose.AsyncImage
import com.inno.coffee.R
import com.inno.coffee.function.makedrinks.MakeLeftDrinksHandler
import com.inno.coffee.function.makedrinks.MakeRightDrinksHandler
import com.inno.coffee.utilities.getImageResId
import com.inno.coffee.utilities.previewFormula
import com.inno.coffee.viewmodel.home.HomeViewModel
import com.inno.common.db.entity.Formula

@Composable
fun QueueDialog(
    showDialog: Boolean,
    second: Boolean,
    viewModel: HomeViewModel,
//    properties: DialogProperties = DialogProperties(),
    onDismissClick: () -> Unit,
) {
    if (showDialog) {
        val queue = if (second) {
            MakeRightDrinksHandler.queue.collectAsState()
        } else {
            MakeLeftDrinksHandler.queue.collectAsState()
        }

        Dialog(
            onDismissRequest = { onDismissClick() },
//            properties = properties.let {
//                DialogProperties(
//                    dismissOnBackPress = it.dismissOnBackPress,
//                    dismissOnClickOutside = it.dismissOnClickOutside,
//                    securePolicy = it.securePolicy,
//                    usePlatformDefaultWidth = false,
//                )
//            }
        ) {
            (LocalView.current.parent as? DialogWindowProvider)?.apply {
                window.setGravity(Gravity.TOP)
                window.attributes = window.attributes.apply {
                    x = 600
                    y = 50
                }
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(800.dp)
                    .padding(top = 30.dp, bottom = 20.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.background,
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                ) {
                    itemsIndexed(items = queue.value) { index, item ->
                        QueueItem(index, item) {
                            viewModel.removeQueueDrink(index, item, second)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QueueItem(index: Int, model: Formula, onCancelItemClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 10.dp, top = 10.dp, end = 30.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "${index + 1}", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.width(20.dp))
        val res = getImageResId(model.imageRes)
        AsyncImage(model = res, modifier = Modifier.size(100.dp),
            contentDescription = null)
        Spacer(modifier = Modifier.width(50.dp))
        val name = if (!model.productName?.name.isNullOrBlank()) {
            model.productName?.name
        } else if (!model.productName?.nameRes.isNullOrBlank()) {
            stringResource(getImageResId(model.productName?.nameRes!!))
        } else {
            stringResource(R.string.common_empty_string)
        }
        Text(text = name!!,
            style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.weight(1f)) // 占据剩余的空间
        Button(onClick = { onCancelItemClick() }) {
            Text(text = stringResource(id = R.string.common_button_cancel))
        }
    }
}

@Preview(device = Devices.TABLET, showBackground = true)
@Composable
private fun PreviewItem() {
    QueueItem(1, model = previewFormula) {

    }
}












