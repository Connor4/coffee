package com.inno.common.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "formula_table")
data class Formula(
    var productType: String,
    var productName: String,
    var vat: String,
    var waterDosage: Int,
    var coffeeDosage: Int,
    var pressure: Int,
    var preSoakingTime: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)