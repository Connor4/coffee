package com.inno.coffee.ui.firstinstall

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.NextStepButton
import com.inno.coffee.utilities.debouncedClickable
import com.inno.coffee.utilities.draw9Patch
import com.inno.coffee.utilities.nsp
import java.util.Locale


@Composable
fun LanguageLayout(modifier: Modifier = Modifier, onLanguagePick: (String) -> Unit) {
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
    val (selectedValue, setSelectedValue) = remember {
        mutableStateOf(radioOptions[english]!!)
    }
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 66.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(id = R.string.first_install_language_title),
                fontSize = 15.nsp(),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
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
                    .verticalScroll(scrollState)
                    .selectableGroup()
            ) {
                radioOptions.forEach {
                    LanguageRadioButton(text = it.key, isSelected = (it.key == selectedKey),
                        onClick = {
                            setSelectedKey(it.key)
                            setSelectedValue(it.value)
                        })
                }
            }
        }

        NextStepButton(modifier = Modifier.align(Alignment.BottomEnd)) {
            onLanguagePick(selectedValue)
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
                .fillMaxWidth()
                .fillMaxHeight()
                .draw9Patch(LocalContext.current, R.drawable.common_item_select_bg))
        }
        Text(
            text = text,
            fontSize = 7.nsp(),
            color = Color.White,
        )
    }
}