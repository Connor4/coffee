package com.inno.common.db

import androidx.room.TypeConverter
import com.inno.common.db.entity.FormulaUnitValue
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun fromFormulaUnitValue(value: FormulaUnitValue): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toFormulaUnitValue(value: String): FormulaUnitValue {
        return Json.decodeFromString(value)
    }

}