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
import com.inno.coffee.R
import com.inno.serialport.function.data.DataCenter
import com.inno.serialport.function.data.Subscriber
import com.inno.serialport.utilities.ReceivedData
import com.inno.serialport.utilities.ReceivedDataType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GlobalDialogManager private constructor(private val application: Application) {

    private val windowManager =
        application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val scope = CoroutineScope(Dispatchers.Main)
    private var dialogView: View? = null
    private var dialogShowing = false
    private var dialogData: DialogData? = null
    private val dialogDataList = mutableListOf<DialogData>()
    private var updateDialogFlag = false
    private val subscriber = object : Subscriber {
        override fun onDataReceived(data: Any) {
            parseReceivedData(data)
        }
    }

    init {
        DataCenter.subscribe(ReceivedDataType.SERIAL_PORT_ERROR, subscriber)
        DataCenter.subscribe(ReceivedDataType.HEARTBEAT_LIST, subscriber)
    }

    private fun parseReceivedData(data: Any) {
        // SerialErrorData发生，一定不会有HeatBeatList；出现HeatBeatList，一定已经解决SerialErrorData
        scope.launch {
            when (val receivedData = data as ReceivedData) {
                is ReceivedData.SerialErrorData -> {
                    if (validDialogData(receivedData.code)) {
                        val dialogData = DialogData().apply {
                            errorCode = receivedData.code
                            message =
                                "ErrorData: ${receivedData.info}, need reboot ${receivedData.reboot}"
                        }
                        dialogDataList.clear()
                        dialogDataList.add(dialogData)
                        updateDialogFlag = true
                    }
                }
                is ReceivedData.HeatBeatList -> {
                    receivedData.list.let { list ->
                        val tempList = mutableListOf<DialogData>()
                        list.forEach { heartbeat ->
                            heartbeat.error?.let { error ->
                                if (validDialogData(error.status.value)) {
                                    val dialogData = DialogData().apply {
                                        errorCode = error.status.value
                                        message = "${error.value}"
                                    }
                                    tempList.add(dialogData)
                                }
                            }
                        }
                        if (tempList.isNotEmpty()) {
                            dialogDataList.clear()
                            dialogDataList.addAll(tempList)
                            updateDialogFlag = true
                        }
                    }
                }
                else -> {}
            }

            if (updateDialogFlag) {
                if (dialogShowing) {
                    updateDialogContent(DialogData())
                } else {
                    showDialog(DialogData())
                }
            }
        }
    }

    private fun validDialogData(code: Int): Boolean {
        return dialogDataList.none { it.errorCode == code }
    }

    private fun updateDialogContent(data: DialogData) {
        dialogView?.findViewById<TextView>(R.id.dialog_title)?.text = "${data.errorCode}"
        dialogView?.findViewById<TextView>(R.id.dialog_message)?.text = data.message
    }

    private fun showDialog(data: DialogData) {
        dialogData = data
        dialogShowing = true
        showDialogView()
    }

    private fun dismissDialog() {
        dialogData = null
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
                it.findViewById<TextView>(R.id.dialog_title)?.text = "${dialogData?.errorCode}"
                it.findViewById<TextView>(R.id.dialog_message)?.text = dialogData?.message
                it.findViewById<Button>(R.id.dialog_confirm_button)?.setOnClickListener {
                    dialogData?.onConfirm?.let { it1 -> it1() }
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

        fun getInstance(): GlobalDialogManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: GlobalDialogManager(application!!).also { INSTANCE = it }
            }
        }
    }

}

data class DialogData(
    var errorCode: Int = 0,
    var message: String = "",
    val onConfirm: () -> Unit = {},
    val onDismiss: () -> Unit = {},
)