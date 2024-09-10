package com.inno.common.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "formula_table")
data class Formula(
    var productId: Short,
    var productType: String,
    var productName: String,
    var preFlush: Boolean = false,
    var postFlush: Boolean = false,
    var vat: Boolean,
    var coffeeWater: Short, // Boiler
    var powderDosage: Short, // Grinder
    var pressWeight: Short, // Brewer
    var preMakeTime: Short, // Brewerf
    var postPreMakeWaitTime: Short, // Brewer
    var secPressWeight: Short, // Brewer
    var hotWater: Short = -1, // Boiler
    var waterSequence: Short = -1, // Boiler
    var coffeeCycles: Short, // i don't know what it's
    var bypassWater: Short = -1, // Boiler
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