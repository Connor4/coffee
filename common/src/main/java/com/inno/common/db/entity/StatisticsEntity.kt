package com.inno.common.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ProductType(product: String) {
    Coffee("coffee"),
    Espresso("espresso"),
}

@Entity(tableName = "drinks_history_table")
data class DrinksHistory(
    var time: String,
    var side: String,
    var grindTime: String,
    var pqc: String,
    var extTime: String,
    var waterQnty: String,
    var waterTemp: String,
    var milkTemp: String,
    var streamPressure: String,
    var type: String,
    var username: String = "",
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)

@Entity(tableName = "wash_machine_history_table")
data class WashMachineHistory(
    var time: String,
    var startTime: String,
    var duration: String,
    var interrupt: Boolean,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)

@Entity(tableName = "failure_history_table")
data class FailureHistory(
    var time: String, var code: String, var detail: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)

@Entity(tableName = "rinse_history_table")
data class RinseHistory(var time: String, @PrimaryKey(autoGenerate = true) val id: Int = 0)

@Entity(tableName = "maintenance_history_table")
data class MaintenanceHistory(var time: String, @PrimaryKey(autoGenerate = true) val id: Int = 0)
