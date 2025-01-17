package com.inno.coffee.ui.settings.maintenance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.inno.coffee.utilities.nsp

@Composable
fun SaveRestoreLayout(
    onCloseClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(id = R.string.maintenance_save_restore),
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


        Row(
            modifier = Modifier.padding(start = 54.dp, top = 260.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ChangeColorButton(
                modifier = Modifier
                    .width(280.dp)
                    .height(73.dp),
                text = stringResource(R.string.maintenance_software_update)
            ) {
            }
            ChangeColorButton(
                modifier = Modifier
                    .width(280.dp)
                    .height(73.dp),
                text = stringResource(R.string.maintenance_reset_settings)
            ) {
            }
            ChangeColorButton(modifier = Modifier
                .width(280.dp)
                .height(73.dp),
                text = stringResource(R.string.maintenance_save_all_data)
            ) {
            }
            ChangeColorButton(modifier = Modifier
                .width(280.dp)
                .height(73.dp),
                text = stringResource(R.string.maintenance_restore_settings)
            ) {
            }
        }

//        Row(
//            modifier = Modifier.padding(start = 54.dp, top = 350.dp),
//            horizontalArrangement = Arrangement.spacedBy(20.dp)
//        ) {
//            ChangeColorButton(modifier = Modifier
//                .width(280.dp)
//                .height(73.dp),
//                text = stringResource(R.string.maintenance_remote_update)
//            ) {
//            }
//        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewSaveRestore() {
    SaveRestoreLayout()
}