package com.test.budgettracker.data.repository

import com.test.budgettracker.data.model.Category
import com.test.budgettracker.data.model.CategoryExpense
import com.test.budgettracker.data.model.Expense
import com.test.budgettracker.data.model.MonthlyLimit
import com.test.budgettracker.data.repository.datasource.ExpenseLocalDataSource
import com.test.budgettracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class ExpenseRepositoryImpl(
    private val LocalDataSource: ExpenseLocalDataSource
): ExpenseRepository {
    override suspend fun saveExpense(expense: Expense) {
        LocalDataSource.saveExpense(expense)
    }

    override suspend fun deleteExpense(expense: Expense) {
        LocalDataSource.deleteExpense(expense)
    }

    override suspend fun getExpenses(limit: Int, offset: Int): Flow<List<Expense>> {
      return  LocalDataSource.getExpenses(limit= limit, offset= offset)
    }

    override suspend fun getCategories(): Flow<List<Category>> {
        return LocalDataSource.getCategories()
    }

    override suspend fun updateExpense(expense: Expense) {

          LocalDataSource.updateExpense(expense)
    }

    override suspend fun getExpensesWithCategory(
        limit: Int,
        offset: Int,
        categoryId: Long
    ): Flow<List<Expense>> {
        return  LocalDataSource.getExpensesWithCategory(limit= limit, offset= offset,categoryId=categoryId)

    }

    override suspend fun getCategoryWiseData(
        timeStart: Long,
        timeEnd: Long
    ): Flow<List<CategoryExpense>> {
         return LocalDataSource.getCategoryWiseData(
             timeStart= timeStart ,
             timeEnd=  timeEnd
         )
    }

    override suspend fun getMonthlyLimit(): Flow<List<MonthlyLimit>> {
         return LocalDataSource.getMonthlyLimit()
    }

    override suspend fun updateMonthlyLimit(monthlyLimit: MonthlyLimit) {
        LocalDataSource.updateMonthlyLimit(monthlyLimit)
    }
}