package com.test.budgettracker.presentation.di

import com.test.budgettracker.domain.repository.ExpenseRepository
import com.test.budgettracker.domain.usecase.DeleteExpenseUseCase
import com.test.budgettracker.domain.usecase.ExpenseUseCaseContainer
import com.test.budgettracker.domain.usecase.GetCategoriesUseCase
import com.test.budgettracker.domain.usecase.GetCategoryWiseDataUseCase
import com.test.budgettracker.domain.usecase.GetExpenseUseCase
import com.test.budgettracker.domain.usecase.GetMonthlyLimitUseCase
import com.test.budgettracker.domain.usecase.SaveExpenseUseCase
import com.test.budgettracker.domain.usecase.UpdateExpenseUseCase
import com.test.budgettracker.domain.usecase.UpdateMonthlyLimitUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Singleton
    @Provides
    fun provideDeleteExpenseUseCase(expenseRepository: ExpenseRepository):
            DeleteExpenseUseCase {
        return DeleteExpenseUseCase(expenseRepository)
    }

    @Singleton
    @Provides
    fun provideGetCategoriesUseCase(expenseRepository: ExpenseRepository):
            GetCategoriesUseCase {
        return GetCategoriesUseCase(expenseRepository)
    }

    @Singleton
    @Provides
    fun provideGetExpenseUseCase(expenseRepository: ExpenseRepository):
            GetExpenseUseCase {
        return GetExpenseUseCase(expenseRepository)
    }

    @Singleton
    @Provides
    fun provideSaveExpenseUseCase(expenseRepository: ExpenseRepository):
            SaveExpenseUseCase {
        return SaveExpenseUseCase(expenseRepository)
    }

    @Singleton
    @Provides
    fun provideUpdateExpenseUseCase(expenseRepository: ExpenseRepository):
            UpdateExpenseUseCase {
        return UpdateExpenseUseCase(expenseRepository)
    }

    @Singleton
    @Provides
    fun provideGetCategoryWiseDataUseCase(expenseRepository: ExpenseRepository):
            GetCategoryWiseDataUseCase {
        return GetCategoryWiseDataUseCase(expenseRepository)
    }

    @Singleton
    @Provides
    fun provideGetMonthlyLimitUseCase(expenseRepository: ExpenseRepository):
            GetMonthlyLimitUseCase {
        return GetMonthlyLimitUseCase(expenseRepository)
    }

    @Singleton
    @Provides
    fun provideUpdateMonthlyLimitUseCase(expenseRepository: ExpenseRepository):
            UpdateMonthlyLimitUseCase {
        return UpdateMonthlyLimitUseCase(expenseRepository)
    }






}