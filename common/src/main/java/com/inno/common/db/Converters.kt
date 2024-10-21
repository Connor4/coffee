package com.inno.common.db

import androidx.room.TypeConverter
import com.inno.common.db.entity.FormulaItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter
    fun fromFormulaUnitValue(value: FormulaItem.FormulaUnitValue): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toFormulaUnitValue(value: String): FormulaItem.FormulaUnitValue {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromFormulaProductType(value: FormulaItem.FormulaProductType): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toFormulaProductType(value: String): FormulaItem.FormulaProductType {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromFormulaProductName(value: FormulaItem.FormulaProductName): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toFormulaProductName(value: String): FormulaItem.FormulaProductName {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromFormulaVatPosition(value: FormulaItem.FormulaVatPosition): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toFormulaVatPosition(value: String): FormulaItem.FormulaVatPosition {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromFormulaAmericanoSeq(value: FormulaItem.FormulaAmericanoSeq): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toFormulaAmericanoSeq(value: String): FormulaItem.FormulaAmericanoSeq {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromFormulaPressWeight(value: FormulaItem.FormulaPressWeight): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toFormulaPressWeight(value: String): FormulaItem.FormulaPressWeight {
        return Json.decodeFromString(value)
    }

}