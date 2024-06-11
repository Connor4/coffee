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
    var type: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)

data class WashMachineHistory(
    var time: String,
    var startTime: String,
    var duration: String,
    var interrupt: Boolean,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)

data class FailureHistory(
    var time: String, var code: String, var detail: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)

data class RinseHistory(var time: String, @PrimaryKey(autoGenerate = true) val id: Int = 0)

data class MaintenanceHistory(var time: String, @PrimaryKey(autoGenerate = true) val id: Int = 0)
