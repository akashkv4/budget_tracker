package com.test.budgettracker.domain.usecase

import com.test.budgettracker.data.model.Expense
import com.test.budgettracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetExpenseUseCase(private val expenseRepository: ExpenseRepository)   {


      suspend fun execute(limit: Int, offset: Int, categoryId: Long): Flow<List<Expense>> {
            return if (categoryId == -1L) {
                  expenseRepository.getExpenses(limit, offset)
            } else {
                  expenseRepository.getExpensesWithCategory(limit, offset, categoryId)
            }
      }

   /*   suspend fun execute(limit: Int, offset: Int,categoryId:Long):
              if(categoryId==-1){
            Flow<List<Expense>> = expenseRepository.getExpenses(limit,offset)

      }else{
            Flow<List<Expense>> = expenseRepository.getExpensesWithCategory(limit,offset,categoryId)

      }*/
}