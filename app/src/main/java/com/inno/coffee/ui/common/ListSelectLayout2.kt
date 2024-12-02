package com.inno.coffee.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.nsp
import com.inno.common.utils.DimenUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ListSelectLayout2(
    title: String = "",
    defaultKey: String,
    map: Map<String, Any>,
    onClick: (key: String, value: Any) -> Unit,
    onCloseClick: () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val selectedKey = remember {
        mutableStateOf(defaultKey)
    }

    LaunchedEffect(Unit) {
        val index = map.keys.indexOf(defaultKey)
        if (index != -1) {
            scrollState.scrollTo(index * DimenUtils.dp2px(context, 75f).toInt())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
            .clickable(enabled = false) { },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.common_list_select_bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(834.dp)
                .height(512.dp)
        )
        Box(
            modifier = Modifier
                .width(770.dp)
                .height(454.dp)
                .background(Color(0xFF191A1D), RoundedCornerShape(20.dp))
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 7.nsp(),
                color = Color.White, modifier = Modifier.padding(start = 40.dp, top = 20.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.home_entrance_close_ic),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 20.dp, end = 22.dp)
                    .width(40.dp)
                    .height(42.dp)
                    .fastclick { onCloseClick() },
            )
            Column(
                modifier = Modifier
                    .padding(top = 100.dp)
                    .align(Alignment.TopCenter)
                    .width(690.dp)
                    .height(338.dp)
                    .verticalScroll(scrollState)
                    .selectableGroup()
            ) {
                map.forEach {
                    SelectItem(stringResource(it.value as Int), (it.key == selectedKey.value)) {
                        selectedKey.value = it.key
                        coroutineScope.launch {
                            delay(300)
                            onClick(it.key, it.value)
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun SelectItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .debouncedClickable({
                onClick()
            }),
    ) {
        if (isSelected) {
            Box(modifier = Modifier
                .fillMaxSize()
                .draw9Patch(LocalContext.current, R.drawable.common_item_select_bg))
        } else {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                .background(Color(0xFF2C2C2C), RoundedCornerShape(10.dp)))
        }
        Text(
            text = text,
            fontSize = 5.nsp(),
            color = Color.White,
        )
    }
}