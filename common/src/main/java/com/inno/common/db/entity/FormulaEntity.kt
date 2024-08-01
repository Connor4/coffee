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
    var vat: Boolean,
    var coffeeWater: Short, // Boiler
    var powderDosage: Short, // Grinder
    var pressWeight: Short, // Brewer
    var preMakeTime: Short, // Brewer
    var postPreMakeWaitTime: Short, // Brewer
    var secPressWeight: Short, // Brewer
    var hotWater: Short = -1, // Boiler
    var americanoSequence: Short = -1, // Boiler
    var coffeeCycles: Short, // i don't know what it's
    var bypassWater: Short = -1, // Boiler
    @PrimaryKey(autoGenerate = true) val id: Short = 0
)
// 美式没有bypassWater，只有hotWater+americanoSequence。