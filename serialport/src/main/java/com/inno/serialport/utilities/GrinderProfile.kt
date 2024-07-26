package com.inno.serialport.utilities

import kotlinx.serialization.Serializable

@Serializable
data class GrinderProfile(val powderDosage: Int, val reserve0: Int, val reserve1: Int,
    val reserve2: Int, val reserve3: Int, val reserve4: Int)
