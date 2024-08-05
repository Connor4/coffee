package com.inno.coffee.ui.notice

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.PixelFormat
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.inno.coffee.R
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import com.inno.serialport.utilities.statusenum.ErrorStatusEnum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GlobalDialogManager private constructor(private val application: Application) {

    private val windowManager =
        application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var dialogView: View? = null
    private var dialogData: MutableState<DialogData?> = mutableStateOf(null)
    private val scope = CoroutineScope(Dispatchers.Main)
    private var dialogShowing = false
    private val subscriber = object : Subscriber {
        override fun onDataReceived(data: Any) {
            scope.launch {
                val error = data as ReceivedData
                val info = getMessage(error)

                if (dialogShowing) {
                    updateDialogContent(info)
                } else {
                    showDialog(DialogData(
                        title = "There is an Alert",
                        message = "Alert Info $info",
                        onConfirm = { dismissDialog() }
                    ))
                }
            }
        }
    }

    init {
        DataCenter.subscribe(ReceivedDataType.SERIAL_PORT_ERROR, subscriber)
        DataCenter.subscribe(ReceivedDataType.HEARTBEAT, subscriber)
    }

    private fun getMessage(receivedData: ReceivedData): String {
        var info = ""
        when (receivedData) {
            is ReceivedData.SerialErrorData -> {
                info = "ErrorData: ${receivedData.info}, need reboot ${receivedData.reboot}"
            }
            is ReceivedData.HeartBeat -> {
                receivedData.error?.let {
                    val status = ErrorStatusEnum.getStatus(it.id)
                    // TODO 展示具体错误类型
                }
            }
            else -> {}
        }
        return info
    }

    private fun updateDialogContent(message: String) {
        dialogView?.findViewById<TextView>(R.id.dialog_message)?.text = message
    }

    private fun showDialog(dialogData: DialogData) {
        this.dialogData.value = dialogData
        dialogShowing = true
        showDialogView()
    }

    private fun dismissDialog() {
        dialogData.value = null
        removeDialogView()
        dialogShowing = false
    }

    private fun showDialogView() {
        if (dialogView == null) {
            dialogView =
                LayoutInflater.from(application).inflate(R.layout.global_dialog_layout, null)
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams
                    .FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT
            )
            windowManager.addView(dialogView, params)

            dialogView?.let {
                it.findViewById<TextView>(R.id.dialog_title)?.text = dialogData.value?.title
                it.findViewById<TextView>(R.id.dialog_message)?.text = dialogData.value?.message
                it.findViewById<Button>(R.id.dialog_confirm_button)?.setOnClickListener {
                    dialogData.value?.onConfirm?.let { it1 -> it1() }
                    dismissDialog()
                }
                it.findViewById<Button>(R.id.dialog_cancel_button)?.setOnClickListener {
                    dismissDialog()
                }
            }
        }
    }

    private fun removeDialogView() {
        dialogView?.let {
            windowManager.removeView(it)
            dialogView = null
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: GlobalDialogManager? = null
        private var application: Application? = null

        fun init(context: Application) {
            application = context
            getInstance()
        }

        private fun getInstance(): GlobalDialogManager {
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