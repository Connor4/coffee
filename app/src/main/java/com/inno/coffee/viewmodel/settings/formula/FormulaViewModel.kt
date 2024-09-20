package com.inno.coffee.viewmodel.settings.formula

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.data.DrinksModel
import com.inno.coffee.di.DefaultDispatcher
import com.inno.common.db.entity.Formula
import com.inno.common.enums.ProductType
import com.inno.common.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val _drinksType = MutableStateFlow<List<DrinksModel>>(emptyList())
    val drinksType: StateFlow<List<DrinksModel>> = _drinksType.asStateFlow()
    private val _formula = MutableStateFlow<Formula?>(null)
    val formula = _formula.asStateFlow()

    init {
        viewModelScope.launch(defaultDispatcher) {
            _drinksType.value = repository.drinkType.filter {
                it.type != ProductType.OPERATION
            }
        }
    }

    fun loadFromSdCard(context: Context) {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                val path = context.filesDir.absolutePath + FORMULA_JSON_FILE
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

    fun getFormula(productId: Int = -1) {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                _formula.value = repository.getFormulaByProductId(productId)
            }
        }
    }

}