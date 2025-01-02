package com.test.budgettracker.domain.usecase

import com.test.budgettracker.data.model.Expense
import com.test.budgettracker.domain.repository.ExpenseRepository

class SaveExpenseUseCase(private val expenseRepository: ExpenseRepository) {
    suspend fun execute(expense: Expense) = expenseRepository.saveExpense(expense)
}