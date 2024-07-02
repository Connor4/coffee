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
import com.inno.serialport.bean.ReceivedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class GlobalDialogManager private constructor(private val application: Application) {

    private val windowManager =
        application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var dialogView: View? = null
    private var dialogData: MutableState<DialogData?> = mutableStateOf(null)
    private val scope = CoroutineScope(Dispatchers.Main)
    private var dialogShowing = false

//    init {
//        scope.launch {
//            SerialPortDataManager.instance.receivedDataFlow.collect { receivedData ->
//                val info = getMessage(receivedData)
//                if (dialogShowing) {
//                    updateDialogContent(info)
//                } else {
//                    showDialog(DialogData(
//                        title = "There is an Alert",
//                        message = "Alert Info $info",
//                        onConfirm = { dismissDialog() }
//                    ))
//                }
//            }
//        }
//    }

    private fun getMessage(receivedData: ReceivedData?): String {
        var info = ""
        receivedData?.let {
            when (it) {
                is ReceivedData.ErrorData -> {
                    info = "ErrorData: ${it.info}, need reboot ${it.reboot}"
                }
                is ReceivedData.PartData -> {
                    info = it.info
                }
                is ReceivedData.HeartBeat -> {
                    info = "HeartBeatData: ${it.info}, need reboot ${it.reboot}, " +
                            "status ${it.heartbeatStatus}"
                }
                else -> {}
            }
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