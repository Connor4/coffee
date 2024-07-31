package com.inno.common.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "formula_table")
data class Formula(
    var productId: Int,
    var productType: String,
    var productName: String,
    var vat: String,
    var coffeeWater: Int,
    var powderDosage: Int,
    var pressWeight: Int,
    var preMakeTime: Int,
    var postPreMakeWaitTime: Int,
    var secPressWeight: Int,
    var hotWater: Int,
    var waterSequence: Int,
    var coffeeCycles: Int, // i don't know what it's
    var bypassWater: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)