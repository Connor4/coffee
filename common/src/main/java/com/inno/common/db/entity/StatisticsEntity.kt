package com.inno.common.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.inno.common.enums.ProductType

@Entity(tableName = "product_count_table")
data class ProductCount(
    var productId: Int,
    var type: ProductType,
    var count: Int = 0,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

data class ProductTypeCount(
    val type: ProductType,
    val totalCount: Int,
)

@Entity(tableName = "product_history_table")
data class ProductHistory(
    var time: String = "",
    var pressFinal: Float = 0f,
    var brewSide: Boolean = false,
    var grindTime: Float = 0f,
    var pqc: Boolean = false,
    var grindAdjust: Int = 0,
    var extTime: Float = 0f,
    var waterQuantity: Int = 0,
    var waterTemp: Int = 0,
    var milkTemp: Int = 0,
    var steamPressure: Int = 0,
    var productType: ProductType,
    var cups: Int = 1,
    var discard: Boolean = false,
    var account: String = "",
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)

@Entity(tableName = "clean_history_table")
data class CleanMachineHistory(
    var time: String = "",
    var startTime: String = "",
    var duration: String = "",
    var stopped: Boolean = false,
    var tabsL: Boolean = false,
    var tabsR: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)

@Entity(tableName = "error_history_table")
data class ErrorHistory(
    var time: String = "",
    var code: String = "",
    var detail: String = "",
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)

@Entity(tableName = "rinse_history_table")
data class RinseHistory(
    var time: String = "",
    var rinseType: String = "",
    var systemFlowRateL: Float = 0f,
    var systemFlowRateR: Float = 0f,
    var systemWaterPressureL: Float = 0f,
    var systemWaterPressureR: Float = 0f,
    var nozzleFlowRateL: Float = 0f,
    var nozzleFlowRateR: Float = 0f,
    var nozzleWaterPressureL: Float = 0f,
    var nozzleWaterPressureR: Float = 0f,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

@Entity(tableName = "maintenance_history_table")
data class MaintenanceHistory(
    var time: String = "",
    var description: String = "",
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
