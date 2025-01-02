package com.test.budgettracker.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.test.budgettracker.data.model.Expense
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ExpenseDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var expenseDao: ExpenseDao
    private lateinit var expenseDatabase: ExpenseDatabase

    @Before
    fun setUp() {
        expenseDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ExpenseDatabase::class.java

        )
            .allowMainThreadQueries()
            .build()

        expenseDao = expenseDatabase.getExpenseDao()

    }

    @After
    fun tearDown() {

        expenseDatabase.close()

    }

    @Test
    fun insertAndRetrieveExpense() = runBlocking {
        val expense = Expense(
            id = 1,
            name = "Expense 1",
            amount = 100.0,
            date = 100,
            categoryId = 1
        )

        expenseDao.insertExpense(expense)

        val retrievedExpenses = expenseDao.getExpenses()

        assertThat(retrievedExpenses).isEqualTo(expense)
    }


}