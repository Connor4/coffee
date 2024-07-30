package com.inno.serialport.utilities.profile

import kotlinx.serialization.Serializable

sealed class Profile {
    @Serializable
    data class GrinderProfile(
        val powderDosage: Int, // 粉量
        val reserve0: Int,
        val reserve1: Int,
        val reserve2: Int,
        val reserve3: Int,
        val reserve4: Int
    ) : Profile()

    @Serializable
    data class BrewerProfile(
        val pressWeight: Int, // 压粉程度
        val preMakeTime: Int, // 预泡时间
        val postPreMakeWaitTime: Int, // 预泡后停水时间
        val secPressWeight: Int, // 二次压粉程度
        val reserve0: Int,
        val reserve1: Int
    ) : Profile()

    @Serializable
    data class BoilerProfile(
        val coffeeWater: Int, // 咖啡出水量
        val hotWater: Int, // 热水出水量
        val bypassWater: Int, // 旁路出水量
        val waterSequence: Int, // waterSequence: 0:one flow; 1:two flow, water first, coffee second; 2:two flow, coffee first,
// water second
        val reserve0: Int,
        val reserve1: Int
    ) : Profile()
}