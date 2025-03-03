package com.inno.common.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "formula_table")
data class Formula(
    var showType: Int = 1, // 1 left 2 right 3 backup
    var index: Int = 0, // item show index
    var productId: Int,
    //=================================coffee================================
    var preFlush: Boolean = false,
    var postFlush: Boolean = false,
    var productType: FormulaItem.FormulaProductType? = null,
    var productPrice: FormulaItem.FormulaProductPrice? = null,
    var productName: FormulaItem.FormulaProductName? = null,
    var beanHopper: FormulaItem.FormulaBeanHopperPosition? = null,
    var coffeeWater: FormulaItem.FormulaUnitValue? = null, // Boiler
    var powderDosage: FormulaItem.FormulaUnitValue? = null, // Grinder
    var pressWeight: FormulaItem.FormulaPressWeight? = null, // Brewer
    var preMakeTime: FormulaItem.FormulaUnitValue? = null, // Brewer
    var postPreMakeWaitTime: FormulaItem.FormulaUnitValue? = null, // Brewer
    var secPressWeight: FormulaItem.FormulaUnitValue? = null, // Brewer
    var hotWater: FormulaItem.FormulaUnitValue? = null, // Boiler
    var waterSequence: FormulaItem.FormulaAmericanoSeq? = null, // Boiler
    var coffeeCycles: FormulaItem.FormulaUnitValue? = null, // how many cups we need to make
    var bypassWater: FormulaItem.FormulaUnitValue? = null, // Boiler
    //=============================steam=====================================
    var manualFoamTime: FormulaItem.FormulaUnitValue? = null,
    var autoFoamTemperature: FormulaItem.FormulaTemperatureValue? = null,
    var foamMode: FormulaItem.FormulaFoamMode? = null,
    var stopAirTime: FormulaItem.FormulaUnitValue? = null,
    var stopAirTemperature: FormulaItem.FormulaTemperatureValue? = null,
    var texture: FormulaItem.FormulaUnitValue? = null,
    var mixHotWater: Short = 0,
    var cleanWand: Short = 0,
    //============================milk=======================================
    var appearance: FormulaItem.FormulaAppearance? = null,
    var milkDelayTime: FormulaItem.FormulaUnitValue? = null,
    var milkOutput: FormulaItem.FormulaMilkOutput? = null,
    var milkSequence: FormulaItem.FormulaMilkSequence? = null,
    //=======================================================================
    var cups: FormulaItem.FormulaCups? = null,
    var imageRes: String = "",
    //================to be determined, but i hava to have it==================
    var steamBoiler: Short = -1, //
    var waterInputValue: Short = -1, // WATER_INPUT_VALVE_ID
    var leftValueLeftBoiler: Short = -1, // LEFT_VALVE_LEFT_BOILER_ID
    var middleValueLeftBoiler: Short = -1, // MIDILE_VALVE_LEFT_BOILER_ID
    var rightValueLeftBoiler: Short = -1, // RIGHT_VALVE_LEFT_BOILER_ID
    var leftValueRightBoiler: Short = -1, // LEFT_VALVE_RIGHT_BOILER_ID
    var middleValueRightBoiler: Short = -1, // MIDILE_VALVE_RIGHT_BOILER_ID
    var rightValueRightBoiler: Short = -1, // RIGHT_VALVE_RIGHT_BOILER_ID

    var steamPurgeValve: Short = -1, // STEAM_PURGE_VALVE_ID
    var steamPurgeMixValve: Short = -1, // STEAM_PURGE_MIX_VALVE_ID
    var steamWaterFillValve: Short = -1, // STEAM_WATER_FILL_VALVE_ID
    var steamHotWaterValve: Short = -1, // STEAM_HOT_WATER_VALVE_ID
    var steamHotWaterValveMix: Short = -1, // STEAM_HOT_WATER_VALVE_MIX_ID
    var steamOutSteamValve1: Short = -1, // STEAM_OUT_STEAM_VALVE1_ID
    var steamOutSteamValve2: Short = -1, // STEAM_OUT_STEAM_VALVE2_ID
    var steamEverFoamValve1: Short = -1, // STEAM_EVER_FOAM_VALVE1_ID
    var steamEverFoamValve2: Short = -1, // STEAM_EVER_FOAM_VALVE2_ID

    var waterPump: Short = -1, // WATER_INPUT_PUMP_ID

    var airPump: Short = -1, // AIR_INPUT_PUMP_ID

    var milkFoamer: Short = -1, // MILK_FOAMER_ID

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)
// 美式没有bypassWater，只有hotWater+americanoSequence。

sealed class FormulaItem {
    @Serializable
    data class FormulaUnitValue(
        var value: Float,
        var rangeStart: Float,
        var rangeEnd: Float,
        var unit: String,
    ) {
        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }
    }

    @Serializable
    data class FormulaProductType(
        var type: String,
    )

    @Serializable
    data class FormulaProductName(
        var name: String?,
        var nameRes: String?,
    )

    @Serializable
    data class FormulaProductPrice(
        var price: Float,
        var rangeStart: Float,
        var rangeEnd: Float,
    )

    @Serializable
    data class FormulaBeanHopperPosition(
        var position: Boolean,
    )

    @Serializable
    data class FormulaAmericanoSeq(
        var sequence: Boolean,
    )

    @Serializable
    data class FormulaPressWeight(
        var weight: Short,
        var rangeStart: Float,
        var rangeEnd: Float,
        var unit: String,
    )

    @Serializable
    data class FormulaCups(
        var single: Int = -1,
        var double: Int = -1,
        var current: Int = -1,
    )

    @Serializable
    data class FormulaFoamMode(
//        foamMode为空是手动/自动蒸汽。不为空是特色蒸汽,不需要true也行。看ProductProfileManager调用代码
        var everFoamMode: Boolean,
    )

    @Serializable
    data class FormulaAppearance(
        var appearance: Boolean,
    )

    @Serializable
    data class FormulaMilkOutput(
        var output: Boolean,
    )

    @Serializable
    data class FormulaTemperatureValue(
        var celsiusValue: Short,
        var celsiusRangeStart: Int,
        var celsiusRangeEnd: Int,
        var celsiusUnit: String,
    ) {
        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }
    }

    @Serializable
    data class FormulaMilkSequence(
        val defaultMilkQuantity: Int,
        val defaultMilkTemperature: Int,
        val defaultFoamTexture: Int,
        var milkQuantity1: Int = -1, // 0-100
        var milkTemperature1: Int = -1, // 0:cold 1:warm
        var foamTexture1: Int = -1, // 0-100
        var milkQuantity2: Int = -1, // 0-100
        var milkTemperature2: Int = -1, // 0:cold 1:warm
        var foamTexture2: Int = -1, // 0-100
    )
}