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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.nsp
import java.util.Locale

@Composable
fun LanguageGroupLayout(
    onLanguagePick: (String) -> Unit,
) {
    val context = LocalContext.current
    val english = context.getString(R.string.first_install_language_English)
    val simplifiedChinese = context.getString(R.string.first_install_language_Chinese_simplified)
    val radioOptions = mapOf<String, String>(
        Pair(english, Locale.ENGLISH.language),
        Pair(simplifiedChinese, Locale.SIMPLIFIED_CHINESE.language),
//        Pair("中文(繁體)", Locale.TRADITIONAL_CHINESE.language),
//        Pair("日本語", Locale.JAPAN.language),
//        Pair("한국어", Locale.KOREA.language),
//        Pair("Français", Locale.FRANCE.language)
    )

    val (selectedKey, setSelectedKey) = remember {
        mutableStateOf(english)
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
                LanguageRadioButton(text = it.key, isSelected = (it.key == selectedKey),
                    onClick = {
                        setSelectedKey(it.key)
                        onLanguagePick(it.value)
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
    LanguageGroupLayout() {}
}