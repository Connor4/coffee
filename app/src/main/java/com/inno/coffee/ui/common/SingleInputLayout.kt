package com.inno.coffee.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.nsp

@Composable
fun SingleInputLayout(
    title: String = "",
    tips: String = "",
    maxInputLimitSize: Int = 70,
    onEnterClick: (String) -> Unit = {},
    onCloseClick: () -> Unit,
) {
    var input by rememberSaveable {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
            .clickable(enabled = false) { },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.permission_input_bg),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(824.dp)
                .height(638.dp)
        )
        Box(
            modifier = Modifier
                .width(770.dp)
                .height(575.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold, fontSize = 7.nsp(), color = Color.White,
                modifier = Modifier.padding(start = 42.dp, top = 40.dp)
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

            Text(
                text = tips,
                fontSize = 5.nsp(), color = Color.White,
                modifier = Modifier.padding(start = 42.dp, top = 100.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 136.dp)
                    .debouncedClickable({ }),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(696.dp)
                        .height(52.dp)
                        .border(2.dp, Color(0xFF00DE93), RoundedCornerShape(4.dp))
                )
                Box(
                    modifier = Modifier
                        .width(692.dp)
                        .height(48.dp)
                        .background(Color(0xFF2C2C2C), RoundedCornerShape(4.dp))
                )
                Text(text = input,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    ),
                    fontSize = 5.nsp(), color = Color(0xFF00DE93),
                    textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .width(692.dp)
                        .wrapContentHeight())
            }


            Box(
                modifier = Modifier.padding(top = 226.dp)
            ) {
                KeyboardLayout(
                    onKeyClick = {
                        if (input.length < maxInputLimitSize) {
                            input += it
                        }
                    }, onDelete = {
                        if (input.isNotEmpty()) {
                            input = input.dropLast(1)
                        }
                    }, onEnter = {
                        onEnterClick(input)
                    }
                )
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewSingleInput() {
    SingleInputLayout(title = "title", tips = "tips") { }
}