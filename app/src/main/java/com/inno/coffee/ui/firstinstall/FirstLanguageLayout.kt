package com.inno.coffee.ui.firstinstall

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.LanguageGroupLayout
import com.inno.coffee.ui.common.NextStepButton
import com.inno.coffee.utilities.nsp
import java.util.Locale


@Composable
fun FirstLanguageLayout(
    modifier: Modifier = Modifier,
    onLanguagePick: (Locale) -> Unit,
) {
    val selectedLanguage = remember {
        mutableStateOf(Locale.ENGLISH)
    }

    Box(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(id = R.string.first_install_language_title),
            fontSize = 15.nsp(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 66.dp)
                .wrapContentSize()
        )

        LanguageGroupLayout(Locale.ENGLISH) { locale ->
            selectedLanguage.value = locale
        }

        NextStepButton(modifier = Modifier.align(Alignment.BottomEnd)) {
            onLanguagePick(selectedLanguage.value)
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewFirstLanguage() {
    FirstLanguageLayout { }
}
