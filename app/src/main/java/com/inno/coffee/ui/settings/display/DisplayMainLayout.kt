package com.inno.coffee.ui.settings.display

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.inno.coffee.R
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.ListSelectLayout
import com.inno.coffee.ui.common.UnitValueScrollBar
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.settings.display.groupone.DisplayGroupOneLayout
import com.inno.coffee.ui.settings.display.groupone.DisplaySettingActivity
import com.inno.coffee.utilities.INDEX_FRONT_LIGHT_BRIGHTNESS
import com.inno.coffee.utilities.INDEX_SCREEN_BRIGHTNESS
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.display.DisplayViewModel

@Composable
fun DisplayMainLayout(
    viewModel: DisplayViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val itemSelectIndex = remember { mutableIntStateOf(INVALID_INT) }
    val defaultValue = remember { mutableStateOf("") }
    val dataMap = remember { mutableMapOf<String, Any>() }
    val scrollDefaultValue = remember { mutableStateOf(0f) }
    val titleValue = remember { mutableStateOf("") }

    val language = viewModel.language.collectAsState()
    val time = viewModel.time.collectAsState()
    val backToFirstPage = viewModel.backToFirstPage.collectAsState()
    val numberOfProductPerPage = viewModel.numberOfProductPerPage.collectAsState()
    val frontLightColor = viewModel.frontLightColor.collectAsState()
    val frontLightBrightness = viewModel.frontLightBrightness.collectAsState()
    val screenBrightness = viewModel.screenBrightness.collectAsState()
    val showExtractionTime = viewModel.showExtractionTime.collectAsState()
    val showProductPrice = viewModel.showProductPrice.collectAsState()
    val showProductName = viewModel.showProductName.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initGroup()
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.getTime()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(id = R.string.common_display),
            fontSize = 7.nsp(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(start = 54.dp, top = 115.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.common_back_ic),
            modifier = Modifier
                .padding(top = 107.dp, end = 50.dp)
                .align(Alignment.TopEnd)
                .fastclick { onCloseClick() },
            contentDescription = null
        )

        FunctionButton({}, {})

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 50.dp, top = 254.dp, end = 95.dp)
        ) {
            DisplayGroupOneLayout(language.value, time.value) { key, value ->
                itemSelectIndex.value = INVALID_INT
                ScreenDisplayManager.autoRoute(context, DisplaySettingActivity::class.java,
                    Bundle().apply {
                        putString(key, value)
                    })
            }
            Spacer(modifier = Modifier.height(40.dp))
            DisplayGroupTwoLayout(backToFirstPage.value, numberOfProductPerPage.value,
                frontLightColor.value, frontLightBrightness.value, screenBrightness.value,
                { title, index, default, map ->
                    itemSelectIndex.value = index
                    titleValue.value = title
                    defaultValue.value = default
                    dataMap.clear()
                    map.forEach {
                        dataMap[it.key] = it.value
                    }
                }, { index, default ->
                    itemSelectIndex.value = index
                    scrollDefaultValue.value = default.toFloat()
                }
            )
            Spacer(modifier = Modifier.height(40.dp))
            DisplayGroupThreeLayout(
                showExtractionTime = showExtractionTime.value,
                showProductName = showProductName.value,
                showProductPrice = showProductPrice.value) { title, index, default, map ->
                itemSelectIndex.value = index
                titleValue.value = title
                defaultValue.value = default
                dataMap.clear()
                map.forEach {
                    dataMap[it.key] = it.value
                }
            }
        }

        if (itemSelectIndex.value != INVALID_INT) {
            if (itemSelectIndex.value == INDEX_SCREEN_BRIGHTNESS || itemSelectIndex.value ==
                    INDEX_FRONT_LIGHT_BRIGHTNESS) {
                key(scrollDefaultValue.value) {
                    UnitValueScrollBar(
                        modifier = Modifier
                            .padding(top = 172.dp, start = 270.dp)
                            .width(450.dp)
                            .wrapContentHeight(),
                        value = scrollDefaultValue.value,
                        rangeStart = 1f,
                        rangeEnd = 255f,
                    ) { changeValue ->
                        viewModel.saveDisplayGroupTwoValue(context, itemSelectIndex.value,
                            changeValue.toInt())
                    }
                }
            } else {
                ListSelectLayout(titleValue.value, defaultValue.value, dataMap.toMap(),
                    { _, value ->
                        viewModel.saveDisplayGroupTwoValue(context, itemSelectIndex.value, value)
                        itemSelectIndex.value = INVALID_INT
                    }, {
                        itemSelectIndex.value = INVALID_INT
                    })
            }
        }
    }
}

@Composable
private fun FunctionButton(
    onImportScreen: () -> Unit,
    onImportLanguage: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 172.dp, end = 95.dp),
        horizontalArrangement = Arrangement.End
    ) {
        ChangeColorButton(
            modifier = Modifier
                .width(220.dp)
                .height(50.dp),
            text = stringResource(id = R.string.display_import_screen)
        ) {
            onImportScreen()
        }
        Spacer(modifier = Modifier.width(20.dp))
        ChangeColorButton(
            modifier = Modifier
                .width(220.dp)
                .height(50.dp),
            text = stringResource(id = R.string.display_import_language),
        ) {
            onImportLanguage()
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewDisplayMain() {
    DisplayMainLayout()
}