package com.inno.serialport.utilities.profile

import kotlinx.serialization.Serializable

// waterSequence: 0:one flow; 1:two flow, water first, coffee second; 2:two flow, coffee first,
// water second
@Serializable
data class BoilerProfile(val coffeeWater: Int, val hotWater: Int, val bypassWater: Int,
    val waterSequence: Int, val reserve0: Int, val reserve1: Int) : Profile()