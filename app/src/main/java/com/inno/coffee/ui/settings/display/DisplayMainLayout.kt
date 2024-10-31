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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.function.display.ScreenDisplayManager
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.ListSelectLayout
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.settings.display.groupone.DisplayGroupOneLayout
import com.inno.coffee.ui.settings.display.groupone.DisplaySettingActivity
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.display.DisplayViewModel

@Composable
fun DisplayMainLayout(
    viewModel: DisplayViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val groupTwoSelectIndex = remember { mutableIntStateOf(INVALID_INT) }
    val defaultValue = remember { mutableStateOf("") }
    val dataMap = remember { mutableMapOf<String, Any>() }

    val language = viewModel.language.collectAsState()
    val time = viewModel.time.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initGroupOne()
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
                ScreenDisplayManager.autoRoute(context, DisplaySettingActivity::class.java,
                    Bundle().apply {
                        putString(key, value)
                    })
            }
            Spacer(modifier = Modifier.height(40.dp))
            DisplayGroupTwoLayout(viewModel, { index, default, map ->
                groupTwoSelectIndex.value = index
                defaultValue.value = default
                dataMap.clear()
                map.forEach {
                    dataMap[it.key] = it.value
                }
            })
            Spacer(modifier = Modifier.height(40.dp))
            DisplayGroupThreeLayout(viewModel) { index, default, map ->
                groupTwoSelectIndex.value = index
                defaultValue.value = default
                dataMap.clear()
                map.forEach {
                    dataMap[it.key] = it.value
                }
            }
        }

        if (groupTwoSelectIndex.value != INVALID_INT) {
            ListSelectLayout(defaultValue.value, dataMap.toMap(), { _, value ->
                viewModel.saveDisplayGroupTwoValue(groupTwoSelectIndex.value, value)
                groupTwoSelectIndex.value = INVALID_INT
            }, {
                groupTwoSelectIndex.value = INVALID_INT
            })
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
        }
        Spacer(modifier = Modifier.width(20.dp))
        ChangeColorButton(
            modifier = Modifier
                .width(220.dp)
                .height(50.dp),
            text = stringResource(id = R.string.display_import_language),
        ) {
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewDisplayMain() {
    DisplayMainLayout()
}