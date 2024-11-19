package com.inno.coffee.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inno.coffee.R
import com.inno.coffee.utilities.nsp

@Composable
fun CheckBoxItemLayout(
    modifier: Modifier = Modifier,
    check: Boolean = false,
    text: String,
    onCheckedChange: (Boolean) -> Unit,
) {
    var isChecked by remember {
        mutableStateOf(check)
    }

    Row(
        modifier = modifier
            .width(384.dp)
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = if (isChecked) painterResource(id = R.drawable.permission_module_check_ic)
            else painterResource(id = R.drawable.permission_module_uncheck_ic),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .fastclick {
                    isChecked = !isChecked
                    onCheckedChange(isChecked)
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
                text = text, color = Color.White, fontSize = 5.nsp(), maxLines = 1,
                overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewCheckBoxItemLayout() {
    CheckBoxItemLayout(Modifier, true, "Using Filter", {})
}

