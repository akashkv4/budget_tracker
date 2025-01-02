package com.test.budgettracker.domain.repository

import com.test.budgettracker.data.model.Category
import com.test.budgettracker.data.model.CategoryExpense
import com.test.budgettracker.data.model.Expense
import com.test.budgettracker.data.model.MonthlyLimit
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    suspend fun saveExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
    suspend fun getExpenses(limit: Int, offset: Int): Flow<List<Expense>>
    suspend fun getCategories():  Flow<List<Category>>
    suspend fun updateExpense(expense: Expense)
    suspend fun getExpensesWithCategory(limit: Int, offset: Int, categoryId: Long): Flow<List<Expense>>
    suspend fun getCategoryWiseData(timeStart: Long, timeEnd: Long): Flow<List<CategoryExpense>>
    suspend fun getMonthlyLimit(): Flow<List<MonthlyLimit>>
    suspend fun updateMonthlyLimit(monthlyLimit: MonthlyLimit)

}