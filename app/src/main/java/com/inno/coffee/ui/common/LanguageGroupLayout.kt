package com.inno.coffee.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.FIRST_INSTALL_KEY_ENGLISH
import com.inno.coffee.utilities.FIRST_INSTALL_KEY_FRENCH
import com.inno.coffee.utilities.FIRST_INSTALL_KEY_JAPANESE
import com.inno.coffee.utilities.FIRST_INSTALL_KEY_KOREAN
import com.inno.coffee.utilities.FIRST_INSTALL_KEY_SIMPLIFIED_CHINESE
import com.inno.coffee.utilities.FIRST_INSTALL_KEY_TRADITIONAL_CHINESE
import com.inno.coffee.utilities.FIRST_INSTALL_VALUE_ENGLISH
import com.inno.coffee.utilities.FIRST_INSTALL_VALUE_FRENCH
import com.inno.coffee.utilities.FIRST_INSTALL_VALUE_JAPANESE
import com.inno.coffee.utilities.FIRST_INSTALL_VALUE_KOREAN
import com.inno.coffee.utilities.FIRST_INSTALL_VALUE_SIMPLIFIED_CHINESE
import com.inno.coffee.utilities.FIRST_INSTALL_VALUE_TRADITIONAL_CHINESE
import com.inno.coffee.utilities.nsp
import java.util.Locale

@Composable
fun LanguageGroupLayout(
    defaultLocale: String,
    onLanguagePick: (String) -> Unit,
) {
    val radioOptions = mapOf(
        Pair(FIRST_INSTALL_KEY_ENGLISH, FIRST_INSTALL_VALUE_ENGLISH),
        Pair(FIRST_INSTALL_KEY_SIMPLIFIED_CHINESE, FIRST_INSTALL_VALUE_SIMPLIFIED_CHINESE),
        Pair(FIRST_INSTALL_KEY_TRADITIONAL_CHINESE, FIRST_INSTALL_VALUE_TRADITIONAL_CHINESE),
        Pair(FIRST_INSTALL_KEY_JAPANESE, FIRST_INSTALL_VALUE_JAPANESE),
        Pair(FIRST_INSTALL_KEY_KOREAN, FIRST_INSTALL_VALUE_KOREAN),
        Pair(FIRST_INSTALL_KEY_FRENCH, FIRST_INSTALL_VALUE_FRENCH)
    )

    val (selectedKey, setSelectedKey) = remember {
        mutableStateOf(defaultLocale)
    }

    LaunchedEffect(defaultLocale) {
        setSelectedKey(defaultLocale)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 217.dp)
            .background(color = Color.Transparent),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .width(450.dp)
                .height(450.dp)
                .verticalScroll(rememberScrollState())
                .selectableGroup()
        ) {
            radioOptions.forEach {
                LanguageRadioButton(text = it.value, isSelected = (it.key == selectedKey),
                    onClick = {
                        setSelectedKey(it.key)
                        onLanguagePick(it.key)
                    })
            }
        }
    }
}


@Composable
private fun LanguageRadioButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .debouncedClickable({
                onClick()
            }),
    ) {
        if (isSelected) {
            Box(modifier = Modifier
                .fillMaxSize()
                .draw9Patch(LocalContext.current, R.drawable.common_item_select_bg))
        }
        Text(
            text = text,
            fontSize = 7.nsp(),
            color = Color.White,
        )
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewDisplayLanguageGroup() {
    LanguageGroupLayout(Locale.ENGLISH.language) {}
}