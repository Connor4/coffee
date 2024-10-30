package com.inno.coffee.ui.settings.display

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.settings.display.groupone.DisplayGroupOneLayout
import com.inno.coffee.utilities.nsp

@Composable
fun DisplayMainLayout(
//    viewModel: DisplayViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 50.dp, top = 254.dp, end = 38.dp)
        ) {
            DisplayGroupOneLayout()
            Spacer(modifier = Modifier.height(40.dp))
            DisplayGroupTwoLayout()
            Spacer(modifier = Modifier.height(40.dp))
            DisplayGroupThreeLayout()
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