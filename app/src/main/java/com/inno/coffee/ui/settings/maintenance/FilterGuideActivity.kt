package com.inno.coffee.ui.settings.maintenance

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inno.coffee.R
import com.inno.coffee.ui.base.CoffeeActivity
import com.inno.coffee.ui.common.ChangeColorButton
import com.inno.coffee.ui.common.fastclick
import com.inno.coffee.ui.theme.CoffeeTheme
import com.inno.coffee.utilities.nsp
import com.inno.coffee.viewmodel.settings.maintenance.FilterGuideViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterGuideActivity : CoffeeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeTheme {
                FilterGuideMainLayout {
                    finish()
                }
            }
        }
    }

}

@Composable
private fun FilterGuideMainLayout(
    viewModel: FilterGuideViewModel = hiltViewModel(),
    onCloseClick: () -> Unit = {},
) {
    val filterGuide = viewModel.filterGuide.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xED000000))
    ) {
        Text(
            text = stringResource(R.string.maintenance_water_filter),
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

        ChangeColorButton(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 692.dp)
                .width(220.dp)
                .height(50.dp),
            text = stringResource(id = R.string.maintenance_filter_guide),
        ) {

        }

        Row(
            modifier = Modifier
                .padding(start = 50.dp, top = 228.dp)
                .width(384.dp)
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = if (filterGuide.value) painterResource(id = R.drawable
                    .permission_module_check_ic)
                else painterResource(id = R.drawable.permission_module_uncheck_ic),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .fastclick {
                        viewModel.setFilterGuide(!filterGuide.value)
                    }
            )
            Spacer(modifier = Modifier.width(15.dp))
            Box(
                modifier = Modifier
                    .background(Color(0xFF2C2C2C))
                    .width(320.dp)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.maintenance_using_filters),
                    color = Color.White, fontSize = 5.nsp(), maxLines = 1,
                    overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewFilterGuideMain() {
    FilterGuideMainLayout()
}