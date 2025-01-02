package com.test.budgettracker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.test.budgettracker.data.model.CategoryExpense
import com.test.budgettracker.data.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expenses ORDER BY date DESC LIMIT :limit OFFSET :offset")
    fun getExpenses(limit: Int, offset: Int): Flow<List<Expense>>


    @Query("SELECT * FROM expenses limit 1")
    fun getExpenses(): Expense


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)


    @Query("SELECT * FROM expenses WHERE categoryId = :categoryId ORDER BY date DESC LIMIT :limit OFFSET :offset")
    fun getExpensessWithCategory(limit: Int, offset: Int, categoryId: Long): Flow<List<Expense>>


    @Query("SELECT categories.id AS categoryId, categories.name AS categoryName, SUM(expenses.amount) AS totalAmount FROM categories LEFT JOIN expenses ON categories.id = expenses.categoryId WHERE expenses.date BETWEEN :timeStart AND :timeEnd GROUP BY categories.id, categories.name")
    fun getCategoryExpenses(timeStart: Long, timeEnd: Long): Flow<List<CategoryExpense>>
}