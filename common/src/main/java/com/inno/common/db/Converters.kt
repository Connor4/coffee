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
    fun fromFormulaVatPosition(value: FormulaItem.FormulaBeanHopperPosition): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toFormulaVatPosition(value: String): FormulaItem.FormulaBeanHopperPosition {
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

    @TypeConverter
    fun fromFormulaCups(value: FormulaItem.FormulaCups): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toFormulaCups(value: String): FormulaItem.FormulaCups {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromFormulaProductPrice(value: FormulaItem.FormulaProductPrice): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toFormulaProductPrice(value: String): FormulaItem.FormulaProductPrice {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromFormulaAppearance(value: FormulaItem.FormulaAppearance): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toFormulaAppearance(value: String): FormulaItem.FormulaAppearance {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromFormulaMilkOutput(value: FormulaItem.FormulaMilkOutput): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toFormulaMilkOutput(value: String): FormulaItem.FormulaMilkOutput {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromFormulaMilkSequence(value: FormulaItem.FormulaMilkSequence): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toFormulaMilkSequence(value: String): FormulaItem.FormulaMilkSequence {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromFormulaFoamMode(value: FormulaItem.FormulaFoamMode): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toFormulaFoamMode(value: String): FormulaItem.FormulaFoamMode {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromFormulaTemperatureValue(value: FormulaItem.FormulaTemperatureValue): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toFormulaTemperatureValue(value: String): FormulaItem.FormulaTemperatureValue {
        return Json.decodeFromString(value)
    }

}