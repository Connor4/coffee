package com.inno.coffee.ui

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.PixelFormat
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.inno.coffee.R

class GlobalDialogManager private constructor(private val application: Application) {

    private val windowManager =
        application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var dialogView: View? = null
    private var dialogData: MutableState<DialogData?> = mutableStateOf(null)

    fun showDialog(dialogData: DialogData) {
        this.dialogData.value = dialogData
        showDialogView()
    }

    fun dismissDialog() {
        dialogData.value = null
        removeDialogView()
    }

    private fun showDialogView() {
        if (dialogView == null) {
            dialogView = LayoutInflater.from(application).inflate(R.layout.global_dialog_layout,
                null)
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams
                    .FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT
            )
            windowManager.addView(dialogView, params)
            dialogView?.findViewById<ComposeView>(R.id.global_dialog_compose_view)?.setContent {
                DialogContent()
            }
        }
    }

    private fun removeDialogView() {
        dialogView?.let {
            windowManager.removeView(it)
            dialogView = null
        }
    }

    @Composable
    private fun DialogContent() {
        val dialogData by dialogData
        dialogData?.let {
            Dialog(onDismissRequest = { it.onDismiss() }) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Custom Dialog Title",
                            style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "This is a custom dialog")
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = { it.onConfirm }) {
                                Text(text = "Confirm")
                            }
                            Button(onClick = { it.onDismiss() }) {
                                Text("Close")
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: GlobalDialogManager? = null
        private var application: Application? = null

        fun init(context: Application) {
            application = context
        }

        fun getInstance(): GlobalDialogManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: GlobalDialogManager(application!!).also { INSTANCE = it }
            }
        }
    }

}

data class DialogData(
    val title: String,
    val message: String,
    val onConfirm: () -> Unit,
    val onDismiss: () -> Unit = {}
)