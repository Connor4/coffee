package com.inno.coffee.ui.settings.bean

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.inno.coffee.ui.common.composeClick
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.utilities.nsp

@Composable
fun GrinderSettingLayout(
    onCloseClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(id = R.string.bean_grinder_adjustment),
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
        Image(painter = painterResource(id = R.drawable.grinder_exam_ic),
            modifier = Modifier
                .padding(top = 184.dp)
                .width(410.dp)
                .height(430.dp)
                .align(Alignment.TopCenter),
            contentDescription = null
        )
// =============================right start================================
        Column(
            modifier = Modifier
                .padding(top = 200.dp, start = 160.dp)
                .width(160.dp)
                .wrapContentHeight(),
        ) {
            Text(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                text = "0", fontSize = 15.nsp(), color = Color(0xFF6DD400),
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                text = "[0.01mm]",
                fontSize = 5.nsp(), color = Color(0xFF6DD400),
            )
        }
        ChangeColorButton2(
            modifier = Modifier
                .padding(top = 312.dp, start = 160.dp)
                .size(160.dp), {
                Text(
                    text = stringResource(R.string.bean_grinder_finer),
                    fontSize = 6.nsp(), color = Color(0xFF6DD400),
                )
            }, {

            }
        )
        ChangeColorButton2(
            modifier = Modifier
                .padding(top = 492.dp, start = 160.dp)
                .size(160.dp), {
                Text(
                    text = stringResource(R.string.bean_grinder_coarser),
                    fontSize = 6.nsp(), color = Color(0xFF6DD400),
                )
            }, {

            }
        )
        ChangeColorButton(
            modifier = Modifier
                .padding(top = 672.dp, start = 160.dp)
                .width(200.dp)
                .height(50.dp),
            text = stringResource(R.string.bean_grinder_reset),
            normalTextColor = Color(0xFF6DD400)
        ) {

        }
        ChangeColorButton(
            modifier = Modifier
                .padding(top = 672.dp, start = 380.dp)
                .width(210.dp)
                .height(50.dp),
            text = stringResource(R.string.bean_grinder_rear_test),
            normalTextColor = Color(0xFF6DD400)
        ) {

        }
// =============================right end================================
//===============================left start================================
        Column(
            modifier = Modifier
                .padding(top = 200.dp, start = 950.dp)
                .width(160.dp)
                .wrapContentHeight(),
        ) {
            Text(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                text = "0", fontSize = 15.nsp(), color = Color(0xFF0FB9A4),
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                text = "[0.01mm]", fontSize = 5.nsp(), color = Color(0xFF0FB9A4),
            )
        }

        ChangeColorButton2(
            modifier = Modifier
                .padding(top = 312.dp, start = 950.dp)
                .size(160.dp), {
                Text(
                    text = stringResource(R.string.bean_grinder_finer),
                    fontSize = 6.nsp(), color = Color(0xFF0FB9A4),
                )
            }, {

            }
        )
        ChangeColorButton2(
            modifier = Modifier
                .padding(top = 492.dp, start = 950.dp)
                .size(160.dp), {
                Text(
                    text = stringResource(R.string.bean_grinder_coarser),
                    fontSize = 6.nsp(), color = Color(0xFF0FB9A4),
                )
            }, {

            }
        )
        ChangeColorButton(
            modifier = Modifier
                .padding(top = 672.dp, start = 910.dp)
                .width(200.dp)
                .height(50.dp),
            text = stringResource(R.string.bean_grinder_reset),
            normalTextColor = Color(0xFF0FB9A4)
        ) {

        }
        ChangeColorButton(
            modifier = Modifier
                .padding(top = 672.dp, start = 680.dp)
                .width(210.dp)
                .height(50.dp),
            text = stringResource(R.string.bean_grinder_front_test),
            normalTextColor = Color(0xFF0FB9A4)
        ) {

        }
//===============================left end================================
    }
}
@Composable
private fun ChangeColorButton2(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    onClick: () -> Unit = {},
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val pressed by interactionSource.collectIsPressedAsState()
    val bgColor: Color?
    val boarderColor: Color?
    if (pressed) {
        bgColor = Color(0xFF00DE93)
        boarderColor = Color(0xFF00DE93)
    } else {
        bgColor = Color(0xFF191A1D)
        boarderColor = Color(0xFF484848)
    }

    Button(
        modifier = modifier,
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(containerColor = bgColor),
        border = BorderStroke(1.dp, boarderColor),
        shape = RoundedCornerShape(20.dp),
        onClick = composeClick {
            onClick()
        },
    ) {
        content()
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewGrinderSettingLayout() {
    GrinderSettingLayout()
}