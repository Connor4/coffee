package com.inno.coffee.utils.formula

import android.app.Application
import com.inno.coffee.di.FormulaRepositoryEntryPoint
import com.inno.coffee.viewmodel.settings.formula.FormulaRepository
import com.inno.common.db.entity.Formula
import com.inno.common.utils.Logger
import com.inno.serialport.utilities.profile.BACK_GRINDER_ID
import com.inno.serialport.utilities.profile.ComponentProfile
import com.inno.serialport.utilities.profile.ComponentProfileList
import com.inno.serialport.utilities.profile.FRONT_GRINDER_ID
import com.inno.serialport.utilities.profile.LEFT_BOILER_ID
import com.inno.serialport.utilities.profile.LEFT_BREWER_ID
import com.inno.serialport.utilities.profile.ProductProfile
import com.inno.serialport.utilities.profile.RIGHT_BOILER_ID
import com.inno.serialport.utilities.profile.RIGHT_BREWER_ID
import dagger.hilt.android.EntryPointAccessors
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object ProductProfileManager {
    private const val TAG = "ProductProfileManager"
    private lateinit var repository: FormulaRepository

    fun init(application: Application) {
        val entryPoint = EntryPointAccessors.fromApplication(
            application,
            FormulaRepositoryEntryPoint::class.java
        )
        repository = entryPoint.formulaRepository()
    }

    suspend fun convertProductProfile(productId: Int, leftSize: Boolean): String {
        Logger.d(TAG,
            "convertProductProfile() called with: productId = $productId, leftSize = $leftSize")
        val formula = repository.getFormulaByProductId(productId)
        val productProfile = createProductProfile(formula!!, leftSize)
        return productProfile
    }

    private fun createProductProfile(formula: Formula, leftSize: Boolean): String {
        val grinderId = if (formula.vat) FRONT_GRINDER_ID else BACK_GRINDER_ID
        val grinderProfile = ComponentProfile(grinderId, shortArrayOf(formula.powderDosage, 0, 0,
            0, 0, 0))

        val brewerId: Short
        val boilerId: Short
        if (leftSize) {
            brewerId = LEFT_BREWER_ID
            boilerId = LEFT_BOILER_ID
        } else {
            brewerId = RIGHT_BREWER_ID
            boilerId = RIGHT_BOILER_ID
        }
        val brewerProfile = ComponentProfile(brewerId, shortArrayOf(formula.pressWeight, formula
            .preMakeTime, formula.postPreMakeWaitTime, formula.secPressWeight, 0, 0))
        val boilerProfile = ComponentProfile(boilerId, shortArrayOf(formula.coffeeWater, formula
            .hotWater, formula.bypassWater, formula.americanoSequence))

        val componentList = mutableListOf(grinderProfile, brewerProfile, boilerProfile)
        val componentProfileList = ComponentProfileList(componentList.size.toShort(), componentList)
        val productProfile = ProductProfile(formula.productId, 0, 0, componentProfileList)
        try {
            val content = Json.encodeToString(productProfile)
            return content
        } catch (e: SerializationException) {
            Logger.e(TAG, "createProductProfile SerializationException $e")
            return ""
        } catch (e: IllegalArgumentException) {
            Logger.e(TAG, "createProductProfile IllegalArgumentException $e")
            return ""
        }
    }

}