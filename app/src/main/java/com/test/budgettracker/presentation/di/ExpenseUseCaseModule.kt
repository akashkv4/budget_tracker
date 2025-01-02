package com.test.budgettracker.presentation.di

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
class ExpenseUseCaseModule {
    @Singleton
    @Provides
    fun provideExpenseUseCaseContainer(
        deleteExpenseUseCase: DeleteExpenseUseCase,
        getCategoriesUseCase: GetCategoriesUseCase,
        getExpenseUseCase: GetExpenseUseCase,
        saveExpenseUseCase: SaveExpenseUseCase,
        updateExpenseUseCase: UpdateExpenseUseCase,
        getCategoryWiseDataUseCase: GetCategoryWiseDataUseCase,
        getMonthlyLimitUseCase: GetMonthlyLimitUseCase,
        updateMonthlyLimitUseCase: UpdateMonthlyLimitUseCase
        ):ExpenseUseCaseContainer{
        return ExpenseUseCaseContainer(
            saveExpenseUseCase = saveExpenseUseCase ,
            deleteExpenseUseCase = deleteExpenseUseCase,
            getExpenseUseCase = getExpenseUseCase,
            getCategoriesUseCase = getCategoriesUseCase,
            updateExpenseUseCase = updateExpenseUseCase,
            getCategoryWiseDataUseCase = getCategoryWiseDataUseCase,
            getMonthlyLimitUseCase = getMonthlyLimitUseCase,
            updateMonthlyLimitUseCase = updateMonthlyLimitUseCase
        )
    }

}