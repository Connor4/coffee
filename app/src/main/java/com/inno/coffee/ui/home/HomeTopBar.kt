package com.inno.coffee.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.inno.coffee.R
import com.inno.coffee.ui.common.debouncedClickable
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.home.HomeViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeTopBar(
    open: Boolean = false,
    viewModel: HomeViewModel = hiltViewModel(),
    onClick: (show: Boolean) -> Unit = {},
) {
    val date by viewModel.date.collectAsState()
    val currentTime by viewModel.time.collectAsState()

    // TODO this can be lift to upper level.
    LaunchedEffect(Unit) {
        while (true) {
            viewModel.getCurrentDate()
            delay(60_000L)
        }
    }

    // TODO this can be lift to upper level.
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.getCurrentDate()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = Color(0xFF000002)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = date,
            fontSize = 6.nsp(),
            color = Color(0xFFECFFF9),
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
        )
        Text(
            text = currentTime,
            fontSize = 6.nsp(),
            color = Color(0xFFECFFF9),
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
                .wrapContentWidth(Alignment.End)
                .size(60.dp)
                .debouncedClickable({
                    onClick(!open)
                }),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = if (open) painterResource(id = R.drawable.home_entrance_select_ic)
                else painterResource(id = R.drawable.home_entrance_normal_ic),
                contentDescription = null,
                modifier = Modifier
                    .size(26.dp),
            )
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewTop() {
    HomeTopBar(true)
}