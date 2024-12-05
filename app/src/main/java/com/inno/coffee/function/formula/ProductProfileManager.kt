package com.inno.coffee.function.formula

import android.app.Application
import android.content.Context
import com.inno.coffee.di.FormulaRepositoryEntryPoint
import com.inno.coffee.viewmodel.settings.formula.FormulaRepository
import com.inno.common.db.entity.Formula
import com.inno.common.utils.CoffeeSharedPreferences
import com.inno.common.utils.Logger
import com.inno.serialport.utilities.profile.BACK_GRINDER_ID
import com.inno.serialport.utilities.profile.ComponentProfile
import com.inno.serialport.utilities.profile.ComponentProfileList
import com.inno.serialport.utilities.profile.FRONT_GRINDER_ID
import com.inno.serialport.utilities.profile.LEFT_BOILER_ID
import com.inno.serialport.utilities.profile.LEFT_BREWER_ID
import com.inno.serialport.utilities.profile.LEFT_VALVE_LEFT_BOILER_ID
import com.inno.serialport.utilities.profile.LEFT_VALVE_RIGHT_BOILER_ID
import com.inno.serialport.utilities.profile.MIDILE_VALVE_LEFT_BOILER_ID
import com.inno.serialport.utilities.profile.MIDILE_VALVE_RIGHT_BOILER_ID
import com.inno.serialport.utilities.profile.ProductProfile
import com.inno.serialport.utilities.profile.RIGHT_BOILER_ID
import com.inno.serialport.utilities.profile.RIGHT_BREWER_ID
import com.inno.serialport.utilities.profile.RIGHT_VALVE_LEFT_BOILER_ID
import com.inno.serialport.utilities.profile.RIGHT_VALVE_RIGHT_BOILER_ID
import com.inno.serialport.utilities.profile.WATER_INPUT_PUMP_ID
import com.inno.serialport.utilities.profile.WATER_INPUT_VALVE_ID
import dagger.hilt.android.EntryPointAccessors
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder

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

    suspend fun convertProductProfile(productId: Int, leftSize: Boolean): ByteArray {
        Logger.d(TAG,
            "convertProductProfile() called with: productId = $productId, leftSize = $leftSize")
        val formula = repository.getFormulaByProductId(productId) ?: return byteArrayOf()
        val productProfile = createProductProfile(formula, leftSize)

        val componentSize = productProfile.componentProfileList.componentList.size
        // id2 + preFlush2 + postFlush2 + ComponentProfileList: num2 + COMPONENT_SIZE * (comid2 + 2*para6)
        val productFileSize = 8 + 14 * componentSize
        val serializeBuffer = ByteBuffer.allocate(productFileSize)
        serializeBuffer.order(ByteOrder.LITTLE_ENDIAN)
        serializeBuffer.putShort(productProfile.productId)
        serializeBuffer.putShort(productProfile.preFlush)
        serializeBuffer.putShort(productProfile.postFlush)
        serializeBuffer.putShort(productProfile.componentProfileList.componentNum)

        for (i in 0 until componentSize) {
            val componentProfile = productProfile.componentProfileList.componentList[i]
            serializeBuffer.putShort(componentProfile.componentId)
            for (para in componentProfile.para) {
                serializeBuffer.putShort(para)
            }
        }
        serializeBuffer.flip()
        val serializeProductInfo = ByteArray(serializeBuffer.limit())
        serializeBuffer.get(serializeProductInfo)

        return serializeProductInfo
    }

    private fun createProductProfile(formula: Formula, leftSize: Boolean): ProductProfile {
        val preFlush: Short = if (formula.preFlush) 1 else 0
        val postFlush: Short = if (formula.postFlush) 1 else 0
        val grinderId =
            if (formula.beanHopper?.position != false) FRONT_GRINDER_ID else BACK_GRINDER_ID
        val grinderProfile = ComponentProfile(grinderId,
            shortArrayOf(formula.powderDosage?.value ?: 0, 0, 0, 0, 0, 0))

        val brewerId: Short
        val boilerId: Short
        if (leftSize) {
            brewerId = LEFT_BREWER_ID
            boilerId = LEFT_BOILER_ID
        } else {
            brewerId = RIGHT_BREWER_ID
            boilerId = RIGHT_BOILER_ID
        }
        val sequence: Short = if (formula.waterSequence?.sequence != false) {
            0
        } else {
            1
        }
        val brewerProfile =
            ComponentProfile(brewerId, shortArrayOf(formula.pressWeight?.weight ?: 0,
                formula.preMakeTime?.value ?: 0, formula.postPreMakeWaitTime?.value ?: 0,
                formula.secPressWeight?.value ?: 0, 0, 0))
        val boilerProfile = ComponentProfile(boilerId, shortArrayOf(formula.coffeeWater?.value ?: 0,
            formula.hotWater?.value ?: 0, formula.bypassWater?.value ?: 0, sequence, 0, 0))

        val componentList = mutableListOf(grinderProfile, brewerProfile, boilerProfile)

        if (formula.waterPump.toInt() != -1) {
            val waterPumpProfile =
                ComponentProfile(WATER_INPUT_PUMP_ID, shortArrayOf(0, 0, 0, 0, 0, 0))
            componentList.add(waterPumpProfile)
        }
        if (formula.waterInputValue.toInt() != -1) {
            val waterInputValveProfile = ComponentProfile(WATER_INPUT_VALVE_ID,
                shortArrayOf(0, 0, 0, 0, 0, 0))
            componentList.add(waterInputValveProfile)
        }
        if (formula.leftValueLeftBoiler.toInt() != -1) {
            val leftValveLeftBoilerProfile = ComponentProfile(LEFT_VALVE_LEFT_BOILER_ID,
                shortArrayOf(0, 0, 0, 0, 0, 0))
            componentList.add(leftValveLeftBoilerProfile)
        }
        if (formula.middleValueLeftBoiler.toInt() != -1) {
            val middleValveLeftBoilerProfile = ComponentProfile(MIDILE_VALVE_LEFT_BOILER_ID,
                shortArrayOf(0, 0, 0, 0, 0, 0))
            componentList.add(middleValveLeftBoilerProfile)
        }
        if (formula.rightValueLeftBoiler.toInt() != -1) {
            val rightValveLeftBoilerProfile = ComponentProfile(RIGHT_VALVE_LEFT_BOILER_ID,
                shortArrayOf(0, 0, 0, 0, 0, 0))
            componentList.add(rightValveLeftBoilerProfile)
        }
        if (formula.leftValueRightBoiler.toInt() != -1) {
            val leftValveRightBoilerProfile = ComponentProfile(LEFT_VALVE_RIGHT_BOILER_ID,
                shortArrayOf(0, 0, 0, 0, 0, 0))
            componentList.add(leftValveRightBoilerProfile)
        }
        if (formula.middleValueRightBoiler.toInt() != -1) {
            val middleValveRightBoilerProfile = ComponentProfile(MIDILE_VALVE_RIGHT_BOILER_ID,
                shortArrayOf(0, 0, 0, 0, 0, 0))
            componentList.add(middleValveRightBoilerProfile)
        }
        if (formula.rightValueRightBoiler.toInt() != -1) {
            val rightValveRightBoilerProfile = ComponentProfile(RIGHT_VALVE_RIGHT_BOILER_ID,
                shortArrayOf(0, 0, 0, 0, 0, 0))
            componentList.add(rightValveRightBoilerProfile)
        }

        val componentProfileList =
            ComponentProfileList(componentList.size.toShort(), componentList)
        return ProductProfile(formula.productId.toShort(), preFlush, postFlush,
            componentProfileList)
    }

    suspend fun readFormulaFromAssets(context: Context) {
        if (CoffeeSharedPreferences.getInstance().loadFormula) {
            return
        }
        try {
            val inputStream = context.assets.open("formula.json")
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val json = bufferedReader.use { it.readText() }
            val list: List<Formula> = Json.decodeFromString(json)
            Logger.d(TAG, "readFormulaFromAssets() called with: json $json")
            repository.insertFormulaList(list)
            CoffeeSharedPreferences.getInstance().loadFormula = true
        } catch (e: Exception) {
            e.printStackTrace()
            Logger.e(TAG, "Exception: $e")
        }
    }

}