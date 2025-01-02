package com.test.budgettracker.domain.usecase

data class ExpenseUseCaseContainer(
    val saveExpenseUseCase: SaveExpenseUseCase,
    val deleteExpenseUseCase: DeleteExpenseUseCase,
    val getExpenseUseCase: GetExpenseUseCase,
    val getCategoriesUseCase: GetCategoriesUseCase,
    val updateExpenseUseCase: UpdateExpenseUseCase,
    val getCategoryWiseDataUseCase: GetCategoryWiseDataUseCase,
    val updateMonthlyLimitUseCase: UpdateMonthlyLimitUseCase,
    val getMonthlyLimitUseCase: GetMonthlyLimitUseCase
)
