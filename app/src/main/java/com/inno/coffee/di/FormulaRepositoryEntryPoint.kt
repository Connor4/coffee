package com.inno.coffee.di

import com.inno.coffee.viewmodel.settings.formula.FormulaRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FormulaRepositoryEntryPoint {
    fun formulaRepository(): FormulaRepository
}