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
        val reserve4: Int,
    ) : Profile()

    @Serializable
    data class BrewerProfile(
        val pressWeight: Int, // 压粉程度
        val preMakeTime: Int, // 预泡时间
        val postPreMakeWaitTime: Int, // 预泡后停水时间
        val secPressWeight: Int, // 二次压粉程度
        val brewerAdjParam: Int, // 酿造调整参数 brewer adj param
        val reserve1: Int,
    ) : Profile()

    @Serializable
    data class BoilerProfile(
        val coffeeWater: Int, // 水量
        val hotWater: Int, // 热水出水量
        val bypassWater: Int, // 旁路出水量
        val waterSequence: Int, // 出咖啡出水顺序
        // 0:one flow; 1:two flow, water first, coffee second;
        // 2:two flow, coffee first, water second
        val reserve0: Int,
        val reserve1: Int,
    ) : Profile()

    @Serializable
    data class SteamBoilerProfile(
        val manualFoamTime: Int = 0,
        val autoFoamTemperature: Int = 0,
        var foamMode: Boolean = false, // 0:temperature mode; 1:time mode
        var stopAirTime: Int = 0,
        var stopAirTemperature: Int = 0,
        var texture: Int = 0,
        var mixHotWater: Short = 0,
        var cleanWand: Short = 0,
        val reserve0: Int,
    ) : Profile()

    @Serializable
    data class MilkFoamProfile(
        val appearance: Short, // 0:white on top, 1:brown on top
        val milkOutput: Short, // 0:coffee outlet, 1:milk arm
        val milkDelayTime: Int, // ms
        val milkQuantity1: Int, // 0-100
        val milkTemperature1: Short, // 0:cold 1:warm
        val foamTexture1: Short, // 0-100
        val milkQuantity2: Int, // 0-100
        val milkTemperature2: Short, // 0:cold 1:warm
        val foamTexture2: Short, // 0-100
    ) : Profile()

}