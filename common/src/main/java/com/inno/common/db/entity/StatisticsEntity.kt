package com.inno.common.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ProductType(name: String) {
    Coffee(""),
    Espresso(""),
}

@Entity(tableName = "drinks_history_table")
data class DrinksHistory(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var time: String,
    var side: String,
    var grindTime: String,
    var pqc: String,
    var type: String,
)

data class WashMachineHistory(
    var time: String,
    var startTime: String,
    var duration: String,
    var interrupt: Boolean,
)

data class FailureHistory(var time: String, var code: String, var detail: String)

data class RinseHistory(var time: String)

data class MaintenanceHistory(var time: String)
