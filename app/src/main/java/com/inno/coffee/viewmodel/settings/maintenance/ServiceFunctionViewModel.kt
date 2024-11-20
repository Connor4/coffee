package com.inno.coffee.viewmodel.settings.maintenance

import androidx.lifecycle.ViewModel
import com.inno.coffee.function.CommandControlManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ServiceFunctionViewModel @Inject constructor(

) : ViewModel() {

    fun sendTestCommand(id: Short) {
        CommandControlManager.sendTestCommand(id)
    }

    fun sendTestCommand(id: Short, param: Int) {
        CommandControlManager.sendTestCommand(id, param)
    }

}