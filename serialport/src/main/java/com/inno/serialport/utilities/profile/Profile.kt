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

//typedef struct{
//    int16_t coffeeWater; //eversys unit: 1 tick = 1 ml
//    int16_t hotWater;  //eversys unit: 1 tick = 1 ml
//    int16_t bypassWater; //
//    int16_t waterSequence;	//0:one flow; //1:two flow, water first, coffee second; 2:two flow, coffee first, water second
//    int16_t reserve0;
//    int16_t reserve1;
//}coffeeBoilerProfile_t;
//
//typedef struct{
//    int16_t pressWeight;
//    int16_t preMakeTime;
//    int16_t postPreMakeWaitTime;
//    int16_t secPressWeight;
//    int16_t brewerAdjParam;//酿造调整参数//brewer adj param
//    int16_t reserve0;
//}brewerProfile_t;
//
//typedef struct{
//    int16_t powderDosage;
//    int16_t reserve0;
//    int16_t reserve1;
//    int16_t reserve2;
//    int16_t reserve3;
//    int16_t reserve4;
//}grinderProfile_t;
//
//
//int16_t temperature;
//int16_t time;
//}foamData_u;
//
//typedef union{
//    int16_t temperature;
//    int16_t time;
//}steamData_u;
//
//typedef struct{
//    int16_t foamMode; //0:temperature/auto mode; 1:time/manual mode; 2: everform mode
//    steamData_u steamData; //
//    int16_t everFoamMode; //everfoam mode//0:temperature mode; 1:time mode;
//    foamData_u foamData;
//    int16_t texture;//foam texture
//    int8_t mixHotWater; //if mix hot water product
//    int8_t cleanWand;	 //if clean wand product
//    int16_t reserve0;
//}steamBoilerProfile_t;
//
//
//typedef struct{
//    uint8_t appearance; //0:white on top, 1:brown on top
//    uint8_t milkOutput; //0:coffee outlet, 1:milk arm
//    uint16_t milkDelayTime;  // ms
//
//    uint16_t milkQuantity1;//0.0 ~ 100.0
//    uint8_t milkTemperature1;//0:cold, 1:warm
//    uint8_t foamTexture1;// 0~100
//
//    uint16_t milkQuantity2;//0.0 ~ 100.0
//    uint8_t milkTemperature2;//0:cold, 1:warm
//    uint8_t foamTexture2;// 0~100
//
//}milkFoamProfile_t;