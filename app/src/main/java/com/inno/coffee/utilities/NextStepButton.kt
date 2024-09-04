package com.inno.coffee.utilities

import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.inno.coffee.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NextStepButton(
    modifier: Modifier = Modifier,
    onNextClick: () -> Unit = {},
) {
    var isPressed by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = modifier
    ) {
        Button(
            onClick = {

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .padding(16.dp)
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            isPressed = true
                        }
                        MotionEvent.ACTION_UP -> {
                            isPressed = false
                            onNextClick()
                        }
                    }
                    true
                }
        ) {
            Text(
                text = stringResource(id = R.string.first_install_next),
                fontSize = 7.nsp(),
                color = Color.White
            )
            Spacer(modifier = Modifier.width(13.dp))
            Image(
                painter = if (isPressed) painterResource(
                    id = R.drawable.install_language_next_pressed_ic)
                else painterResource(
                    id = R.drawable.install_language_next_normal_ic),
                contentDescription = null,
            )
        }
    }
}
