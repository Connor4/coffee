package com.inno.coffee.viewmodel.settings.maintenance

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ServiceFunctionViewModel @Inject constructor(

) : ViewModel() {

    fun steamTestTurnOff() {
//        CommandControlManager.sendTestCommand(STEAM_OUTPUT_COMMAND_ID)
    }

}