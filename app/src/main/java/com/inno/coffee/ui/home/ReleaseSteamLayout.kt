package com.inno.coffee.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.data.DrinksModel
import com.inno.coffee.function.selfcheck.SelfCheckManager
import com.inno.coffee.function.selfcheck.SelfCheckManager.RELEASE_STEAM_READY
import com.inno.coffee.utilities.nsp
import com.inno.common.enums.ProductType

@Composable
fun ReleaseSteamLayout(
    onReleaseSteamClick: () -> Unit,
) {
    val releaseSteam by SelfCheckManager.releaseSteam.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(670.dp)
            .background(Color(0xE6000000))
            .clickable(enabled = false) { },
    ) {
//        Image(
//            painter = painterResource(id = R.drawable.home_entrance_close_ic),
//            contentDescription = null,
//            modifier = Modifier
//                .align(Alignment.TopEnd)
//                .padding(top = 50.dp, end = 50.dp)
//                .width(40.dp)
//                .height(42.dp)
//                .fastclick { onCloseClick() },
//        )
        if (releaseSteam == RELEASE_STEAM_READY) {
            DrinkItem(model = DrinksModel(productId = 12,
                type = ProductType.OPERATION,
                name = R.string.home_item_manual_foam,
                imageRes = R.drawable.operate_manual_milk_ic),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 165.dp)
            ) {
                onReleaseSteamClick()
            }
            Text(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 380.dp),
                text = stringResource(id = R.string.home_release_steam_description),
                fontSize = 6.nsp(),
                color = Color.White,
            )
            Text(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 476.dp),
                text = stringResource(id = R.string.home_release_steam_attention),
                fontSize = 6.nsp(),
                color = Color.Red,
            )
            Text(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 506.dp),
                text = stringResource(id = R.string.home_release_steam_activated),
                fontSize = 5.nsp(),
                color = Color.White,
            )
        } else if (releaseSteam == 2) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = stringResource(id = R.string.home_release_steam_attention),
                fontSize = 6.nsp(),
                color = Color.Red,
            )
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 70.dp),
                text = stringResource(id = R.string.home_release_steam_wait),
                fontSize = 5.nsp(),
                color = Color.White,
            )
        }
    }
}

@Preview(device = Devices.TABLET)
@Composable
private fun PreviewReleaseSteam() {
    ReleaseSteamLayout {}
}