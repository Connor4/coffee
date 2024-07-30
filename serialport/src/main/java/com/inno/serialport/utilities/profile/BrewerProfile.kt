package com.inno.serialport.utilities.profile

import kotlinx.serialization.Serializable

@Serializable
data class BrewerProfile(val pressWeight: Int, val preMakeTime: Int, val postPreMakeWaitTime:
Int, val secPressWeight: Int, val reserve0: Int, val reserve1: Int) : Profile()