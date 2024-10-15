package com.inno.common.db

import androidx.room.TypeConverter
import com.inno.common.db.entity.FormulaAmericanoSeq
import com.inno.common.db.entity.FormulaProductName
import com.inno.common.db.entity.FormulaProductType
import com.inno.common.db.entity.FormulaUnitValue
import com.inno.common.db.entity.FormulaVatPosition
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

    @TypeConverter
    fun fromFormulaProductType(value: FormulaProductType): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toFormulaProductType(value: String): FormulaProductType {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromFormulaProductName(value: FormulaProductName): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toFormulaProductName(value: String): FormulaProductName {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromFormulaVatPosition(value: FormulaVatPosition): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toFormulaVatPosition(value: String): FormulaVatPosition {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromFormulaAmericanoSeq(value: FormulaAmericanoSeq): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toFormulaAmericanoSeq(value: String): FormulaAmericanoSeq {
        return Json.decodeFromString(value)
    }

}