package com.test.budgettracker.presentation.di

import com.test.budgettracker.data.repository.ExpenseRepositoryImpl
import com.test.budgettracker.data.repository.datasource.ExpenseLocalDataSource
import com.test.budgettracker.domain.repository.ExpenseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideExpenseRepository(
        expenseLocalDataSource: ExpenseLocalDataSource
    ): ExpenseRepository {
        return ExpenseRepositoryImpl(expenseLocalDataSource)
    }
}