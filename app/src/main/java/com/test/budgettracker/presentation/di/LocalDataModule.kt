package com.test.budgettracker.presentation.di

import com.test.budgettracker.data.db.CategoryDao
import com.test.budgettracker.data.db.ExpenseDao
import com.test.budgettracker.data.db.MonthlyLimitDao
import com.test.budgettracker.data.repository.datasource.ExpenseLocalDataSource
import com.test.budgettracker.data.repository.datasourceImpl.ExpenseLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {
    @Singleton
    @Provides
    fun provideExpenseLocalDataSource(expenseDao: ExpenseDao, categoryDao: CategoryDao,monthlyLimitDao: MonthlyLimitDao): ExpenseLocalDataSource {
        return ExpenseLocalDataSourceImpl(expenseDao, categoryDao,monthlyLimitDao)
    }

}