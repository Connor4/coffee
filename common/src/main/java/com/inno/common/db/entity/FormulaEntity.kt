package com.inno.common.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "formula_table")
data class Formula(
    var productId: Short,
    var productType: FormulaItem.FormulaProductType,
    var productName: FormulaItem.FormulaProductName,
    var preFlush: Boolean = false,
    var postFlush: Boolean = false,
    var vat: FormulaItem.FormulaVatPosition? = null,
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
    var cups: FormulaItem.FormulaCups? = null,
    //================to be determined, but i hava to have it==================
    var waterPump: Short = -1, // WATER_INPUT_PUMP_ID
    var waterInputValue: Short = -1, // WATER_INPUT_VALVE_ID
    var leftValueLeftBoiler: Short = -1, // LEFT_VALVE_LEFT_BOILER_ID
    var middleValueLeftBoiler: Short = -1, // MIDILE_VALVE_LEFT_BOILER_ID
    var rightValueLeftBoiler: Short = -1, // RIGHT_VALVE_LEFT_BOILER_ID
    var leftValueRightBoiler: Short = -1, // LEFT_VALVE_RIGHT_BOILER_ID
    var middleValueRightBoiler: Short = -1, // MIDILE_VALVE_RIGHT_BOILER_ID
    var rightValueRightBoiler: Short = -1, // RIGHT_VALVE_RIGHT_BOILER_ID
    @PrimaryKey(autoGenerate = true) val id: Short = 0
)
// 美式没有bypassWater，只有hotWater+americanoSequence。

sealed class FormulaItem {
    @Serializable
    data class FormulaUnitValue(
        var value: Short,
        var rangeStart: Float,
        var rangeEnd: Float,
        var unit: String,
    )

    @Serializable
    data class FormulaProductType(
        var type: String,
    )

    @Serializable
    data class FormulaProductName(
        var name: String,
    )

    @Serializable
    data class FormulaVatPosition(
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
}