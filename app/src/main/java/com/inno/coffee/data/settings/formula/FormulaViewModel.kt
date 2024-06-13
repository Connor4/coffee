package com.inno.coffee.data.settings.formula

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.common.db.entity.Formula
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
class FormulaViewModel @Inject constructor(private val repository: FormulaRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher) : ViewModel() {

    companion object {
        private const val FORMULA_JSON_FILE = "/formula.txt"
    }

    val formulaList: StateFlow<List<Formula>> = repository.getAllFormula()
        .stateIn(scope = viewModelScope, started = SharingStarted.Lazily,
            initialValue = emptyList())

    fun loadFromSdCard(context: Context) {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                val path = context.filesDir.absolutePath + FORMULA_JSON_FILE
                val file = File(path)
                if (file.exists()) {
                    val jsonContent = file.readText()
                    val list: List<Formula> = Json.decodeFromString(jsonContent)
                    repository.insertFormulaList(list)
                } else {
                    // notice file not exists
                }
            }
        }
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

}