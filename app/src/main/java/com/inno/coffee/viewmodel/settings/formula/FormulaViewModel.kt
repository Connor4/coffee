package com.inno.coffee.viewmodel.settings.formula

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.common.db.entity.Formula
import com.inno.common.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FormulaViewModel @Inject constructor(
    private val repository: FormulaRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    companion object {
        private const val FORMULA_JSON_FILE = "/formula.txt"
    }

    val formulaList: StateFlow<List<Formula>> = repository.getAllFormula()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )
    val loadFileErrorDialogFlag = mutableStateOf(false)

    fun loadFromSdCard(context: Context) {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                val path = context.filesDir.absolutePath + FORMULA_JSON_FILE
                Logger.d("loadFromSdCard() called $path")
                val file = File(path)
                if (file.exists()) {
                    val jsonContent = file.readText()
                    try {
                        val list: List<Formula> = Json.decodeFromString(jsonContent)
                        repository.insertFormulaList(list)
                    } catch (e: Exception) {
                        Logger.e("FormulaViewModel", "decodeFromString Exception $e")
                        loadFileErrorDialogFlag.value = true
                    }
                } else {
                    loadFileErrorDialogFlag.value = true
                }
            }
        }
    }

    fun dismissFileNotFoundDialog() {
        loadFileErrorDialogFlag.value = false
    }

    fun insertFormula(formula: Formula) {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                repository.insertFormula(formula)
            }
        }
    }

    fun updateFormula(formula: Formula) {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                repository.updateFormula(formula)
            }
        }
    }

    val fileContent = "[\n" +
            "    {\n" +
            "        \"productId\": 1,\n" +
            "        \"productType\": \"Coffee\",\n" +
            "        \"productName\": \"美式咖啡\",\n" +
            "        \"preFlush\": false ,\n" +
            "        \"postFlush\": false,\n" +
            "        \"vat\": true,\n" +
            "        \"powderDosage\": 140,\n" +
            "        \"coffeeWater\": 50,\n" +
            "        \"pressWeight\": 20,\n" +
            "        \"preMakeTime\":800,\n" +
            "        \"postPreMakeWaitTime\": 2000,\n" +
            "        \"secPressWeight\": 0,\n" +
            "        \"hotWater\": 150,\n" +
            "        \"waterSequence\": 1,\n" +
            "        \"coffeeCycles\": 0,\n" +
            "        \"bypassWater\": 0,\n" +
            "        \"waterPump\": 0,\n" +
            "        \"waterInputValue\": 0,\n" +
            "        \"middleValueLeftBoiler\": 0,\n" +
            "        \"rightValueLeftBoiler\": 0\n" +
            "    },\n" +
            "    {\n" +
            "        \"productId\": 2,\n" +
            "        \"productType\": \"Coffee\",\n" +
            "        \"productName\": \"2x美式咖啡\",\n" +
            "        \"preFlush\": false ,\n" +
            "        \"postFlush\": false,\n" +
            "        \"vat\": true,\n" +
            "        \"powderDosage\": 210,\n" +
            "        \"coffeeWater\": 80,\n" +
            "        \"pressWeight\": 20,\n" +
            "        \"preMakeTime\":320,\n" +
            "        \"postPreMakeWaitTime\": 0,\n" +
            "        \"secPressWeight\": 0,\n" +
            "        \"hotWater\": 320,\n" +
            "        \"waterSequence\": 1,\n" +
            "        \"coffeeCycles\": 0,\n" +
            "        \"bypassWater\": 0,\n" +
            "        \"waterPump\": 0,\n" +
            "        \"waterInputValue\": 0,\n" +
            "        \"middleValueLeftBoiler\": 0,\n" +
            "        \"rightValueLeftBoiler\": 0\n" +
            "    },\n" +
            "{\n" +
            "        \"productId\": 3,\n" +
            "        \"productType\": \"Coffee\",\n" +
            "        \"productName\": \"特浓咖啡\",\n" +
            "        \"preFlush\": false ,\n" +
            "        \"postFlush\": false,\n" +
            "        \"vat\": true,\n" +
            "        \"powderDosage\": 140,\n" +
            "        \"coffeeWater\": 50,\n" +
            "        \"pressWeight\": 20,\n" +
            "        \"preMakeTime\": 29,\n" +
            "        \"pressWeight\":20,\n" +
            "        \"preMakeTime\":800,\n" +
            "        \"postPreMakeWaitTime\": 2000,\n" +
            "        \"secPressWeight\": 0,\n" +
            "        \"hotWater\": 0,\n" +
            "        \"waterSequence\": 0,\n" +
            "        \"coffeeCycles\": 0,\n" +
            "        \"bypassWater\": 0,\n" +
            "        \"waterPump\": 0,\n" +
            "        \"waterInputValue\": 0,\n" +
            "        \"middleValueLeftBoiler\": 0,\n" +
            "        \"rightValueLeftBoiler\": 0\n" +
            "    },\n" +
            "   {\n" +
            "        \"productId\": 4,\n" +
            "        \"productType\": \"Coffee\",\n" +
            "        \"productName\": \"2x特浓咖啡\",\n" +
            "        \"preFlush\": false ,\n" +
            "        \"postFlush\": false,\n" +
            "        \"vat\": true,\n" +
            "        \"powderDosage\": 210,\n" +
            "        \"coffeeWater\": 80,\n" +
            "        \"pressWeight\": 20,\n" +
            "        \"preMakeTime\": 0,\n" +
            "        \"postPreMakeWaitTime\": 0,\n" +
            "        \"secPressWeight\": 0,\n" +
            "        \"hotWater\": 0,\n" +
            "        \"waterSequence\": 0,\n" +
            "        \"coffeeCycles\": 0,\n" +
            "        \"bypassWater\": 0,\n" +
            "        \"waterPump\": 0,\n" +
            "        \"waterInputValue\": 0,\n" +
            "        \"middleValueLeftBoiler\": 0,\n" +
            "        \"rightValueLeftBoiler\": 0\n" +
            "    }\n" +
            "]"

}