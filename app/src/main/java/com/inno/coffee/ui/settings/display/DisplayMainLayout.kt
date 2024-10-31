package com.inno.coffee.ui.settings.display

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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.ListSelectLayout
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.settings.display.groupone.DisplayGroupOneLayout
import com.inno.coffee.utilities.INDEX_AUTO_BACK_TO_FIRST_PAGE
import com.inno.coffee.utilities.INDEX_FRONT_LIGHT_BRIGHTNESS
import com.inno.coffee.utilities.INDEX_FRONT_LIGHT_COLOR
import com.inno.coffee.utilities.INDEX_NUMBER_OF_PRODUCT_PER_PAGE
import com.inno.coffee.utilities.INDEX_SCREEN_BRIGHTNESS
import com.inno.coffee.utilities.INVALID_INT
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.display.DisplayViewModel

@Composable
fun DisplayMainLayout(
    viewModel: DisplayViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val groupTwoSelectIndex = remember { mutableIntStateOf(INVALID_INT) }
    val defaultValue = remember { mutableStateOf("") }
    val dataMap = remember { mutableMapOf<String, Any>() }

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
            DisplayGroupOneLayout(viewModel)
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

            }
        }

        if (groupTwoSelectIndex.value != INVALID_INT) {
            when (groupTwoSelectIndex.value) {
                INDEX_AUTO_BACK_TO_FIRST_PAGE -> {
                    ListSelectLayout(defaultValue.value, dataMap.toMap(), { key, value ->
                        viewModel.saveDisplayGroupTwoValue(INDEX_AUTO_BACK_TO_FIRST_PAGE, value)
                        groupTwoSelectIndex.value = INVALID_INT
                    }, {
                        groupTwoSelectIndex.value = INVALID_INT
                    })
                }
                INDEX_NUMBER_OF_PRODUCT_PER_PAGE -> {
                    ListSelectLayout(defaultValue.value, dataMap.toMap(), { key, value ->
                        viewModel.saveDisplayGroupTwoValue(INDEX_NUMBER_OF_PRODUCT_PER_PAGE, value)
                        groupTwoSelectIndex.value = INVALID_INT
                    }, {
                        groupTwoSelectIndex.value = INVALID_INT
                    })
                }
                INDEX_FRONT_LIGHT_COLOR -> {
                    ListSelectLayout(defaultValue.value, dataMap.toMap(), { key, value ->
                        viewModel.saveDisplayGroupTwoValue(INDEX_FRONT_LIGHT_COLOR, value)
                        groupTwoSelectIndex.value = INVALID_INT
                    }, {
                        groupTwoSelectIndex.value = INVALID_INT
                    })
                }
                INDEX_FRONT_LIGHT_BRIGHTNESS -> {
                    ListSelectLayout(defaultValue.value, dataMap.toMap(), { key, value ->
                        viewModel.saveDisplayGroupTwoValue(INDEX_FRONT_LIGHT_BRIGHTNESS, value)
                        groupTwoSelectIndex.value = INVALID_INT
                    }, {
                        groupTwoSelectIndex.value = INVALID_INT
                    })
                }
                INDEX_SCREEN_BRIGHTNESS -> {
                    ListSelectLayout(defaultValue.value, dataMap.toMap(), { key, value ->
                        viewModel.saveDisplayGroupTwoValue(INDEX_SCREEN_BRIGHTNESS, value)
                        groupTwoSelectIndex.value = INVALID_INT
                    }, {
                        groupTwoSelectIndex.value = INVALID_INT
                    })
                }
                else -> {
                }
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