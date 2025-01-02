package com.test.budgettracker.data.repository.datasourceImpl

import com.test.budgettracker.data.db.CategoryDao
import com.test.budgettracker.data.db.ExpenseDao
import com.test.budgettracker.data.db.MonthlyLimitDao
import com.test.budgettracker.data.model.Category
import com.test.budgettracker.data.model.CategoryExpense
import com.test.budgettracker.data.model.Expense
import com.test.budgettracker.data.model.MonthlyLimit
import com.test.budgettracker.data.repository.datasource.ExpenseLocalDataSource
import kotlinx.coroutines.flow.Flow

class ExpenseLocalDataSourceImpl(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao,
    private val monthlyLimitDao: MonthlyLimitDao
): ExpenseLocalDataSource {
    override suspend fun saveExpense(expense: Expense) {
        expenseDao.insertExpense(expense)
    }

    override suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }

    override suspend fun getExpenses(limit: Int, offset: Int): Flow<List<Expense>> {
       return expenseDao.getExpenses(limit =limit, offset = offset)
    }

    override suspend fun getCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategory()
    }

    override suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense)
    }

    override suspend fun getExpensesWithCategory(
        limit: Int,
        offset: Int,
        categoryId: Long
    ): Flow<List<Expense>> {
        return expenseDao.getExpensessWithCategory(limit =limit, offset = offset ,categoryId =categoryId )
    }

    override suspend fun getCategoryWiseData(
        timeStart: Long,
        timeEnd: Long
    ): Flow<List<CategoryExpense>> {
        return  expenseDao.getCategoryExpenses(timeStart,timeEnd)
    }

    override suspend fun getMonthlyLimit(): Flow<List<MonthlyLimit>> {
        return monthlyLimitDao.getLimit()
    }

    override suspend fun updateMonthlyLimit(monthlyLimit: MonthlyLimit) {
        monthlyLimitDao.insertMonthlyLimit(monthlyLimit)
    }
}