package com.test.budgettracker.presentation.di

import com.test.budgettracker.presentation.adapter.ExpenseAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdapterModule {
    @Singleton
    @Provides
    fun provideExpenseAdapter(): ExpenseAdapter {
         return ExpenseAdapter()
    }
}