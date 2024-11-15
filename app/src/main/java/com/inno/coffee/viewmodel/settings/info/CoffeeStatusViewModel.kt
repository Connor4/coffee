package com.inno.coffee.viewmodel.settings.info

import androidx.lifecycle.ViewModel
import com.inno.common.utils.CoffeeDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CoffeeStatusViewModel @Inject constructor(
    private val dataStore: CoffeeDataStore,
) : ViewModel() {

    private val _processCoffeeLeft = MutableStateFlow(0)
    val processCoffeeLeft = _processCoffeeLeft
    private val _actionCoffeeLeft = MutableStateFlow(0)
    val actionCoffeeLeft = _actionCoffeeLeft
    private val _processCoffeeRight = MutableStateFlow(0)
    val processCoffeeRight = _processCoffeeRight
    private val _actionCoffeeRight = MutableStateFlow(0)
    val actionCoffeeRight = _actionCoffeeRight
    private val _prodIdLeft = MutableStateFlow(0)
    val prodIdLeft = _prodIdLeft
    private val _prodIdRight = MutableStateFlow(0)
    val prodIdRight = _prodIdRight
    private val _numWarning = MutableStateFlow(0)
    val numWarning = _numWarning
    private val _numStop = MutableStateFlow(0)
    val numStop = _numStop
    private val _numError = MutableStateFlow(0)
    val numError = _numError

    private val _systemFlowLeft = MutableStateFlow(0)
    val systemFlowLeft = _systemFlowLeft
    private val _nozzleFlowLeft = MutableStateFlow(0)
    val nozzleFlowLeft = _nozzleFlowLeft
    private val _systemFlowRight = MutableStateFlow(0)
    val systemFlowRight = _systemFlowRight
    private val _nozzleFlowRight = MutableStateFlow(0)
    val nozzleFlowRight = _nozzleFlowRight

    private val _valveBrewLeft = MutableStateFlow(false)
    val valveBrewLeft = _valveBrewLeft
    private val _valveBrewRight = MutableStateFlow(false)
    val valveBrewRight = _valveBrewRight
    private val _valveBypassLeft = MutableStateFlow(false)
    val valveBypassLeft = _valveBypassLeft
    private val _valveBypassRight = MutableStateFlow(false)
    val valveBypassRight = _valveBypassRight
    private val _fanFront = MutableStateFlow(false)
    val fanFront = _fanFront
    private val _valveOutLeft = MutableStateFlow(false)
    val valveOutLeft = _valveOutLeft
    private val _valveOutRight = MutableStateFlow(false)
    val valveOutRight = _valveOutRight
    private val _valveWaterInlet = MutableStateFlow(false)
    val valveWaterInlet = _valveWaterInlet
    private val _valveCleanTab = MutableStateFlow(false)
    val valveCleanTab = _valveCleanTab
    private val _grinderLeft = MutableStateFlow(false)
    val grinderLeft = _grinderLeft
    private val _grinderRight = MutableStateFlow(false)
    val grinderRight = _grinderRight
    private val _boilerLeft = MutableStateFlow(false)
    val boilerLeft = _boilerLeft
    private val _boilerRight = MutableStateFlow(false)
    val boilerRight = _boilerRight
    private val _waterPump = MutableStateFlow(false)
    val waterPump = _waterPump
    private val _relayStandby = MutableStateFlow(false)
    val relayStandby = _relayStandby
    private val _fanLeft = MutableStateFlow(false)
    val fanLeft = _fanLeft
    private val _fanRight = MutableStateFlow(false)
    val fanRight = _fanRight



}