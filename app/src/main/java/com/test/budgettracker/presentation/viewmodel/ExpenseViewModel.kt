package com.test.budgettracker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.budgettracker.data.model.Category
import com.test.budgettracker.data.model.CategoryExpense
import com.test.budgettracker.data.model.Expense
import com.test.budgettracker.data.model.MonthlyLimit
import com.test.budgettracker.domain.usecase.ExpenseUseCaseContainer
import com.test.budgettracker.presentation.CreateOrEdit
import com.test.budgettracker.presentation.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseUseCaseContainer: ExpenseUseCaseContainer
) : ViewModel() {

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    private val _scrollToTop = MutableStateFlow(false)
    val scrollToTop: StateFlow<Boolean> = _scrollToTop.asStateFlow()

    private var currentOffset = 0
    private var limit = 20

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<Long?>(null) // Track selected category ID
    val selectedCategoryId: StateFlow<Long?> = _selectedCategoryId.asStateFlow()


    private val _errorEvent = MutableLiveData<Event<String>>()
    val errorEvent: LiveData<Event<String>> get() = _errorEvent


    private val _errorEventEditBudget = MutableLiveData<Event<String>>()
    val errorEventEditBudget: LiveData<Event<String>> get() = _errorEventEditBudget


    private val _categoriesRecordFragment = MutableStateFlow<List<Category>>(emptyList())
    val categoriesRecordFragment: StateFlow<List<Category>> =
        _categoriesRecordFragment.asStateFlow()

    private val _selectedCategoryIdRecordFragment =
        MutableStateFlow<Long?>(null) // Track selected category ID
    val selectedCategoryIdRecordFragment: StateFlow<Long?> =
        _selectedCategoryIdRecordFragment.asStateFlow()


    private val _categoryWiseData = MutableStateFlow<List<CategoryExpense>>(emptyList())
    val categoryWiseData: StateFlow<List<CategoryExpense>> = _categoryWiseData.asStateFlow()


    private val _budgetMonthly = MutableStateFlow<List<MonthlyLimit>>(emptyList())
    val budgetMonthly: StateFlow<List<MonthlyLimit>> = _budgetMonthly.asStateFlow()


    init {
        fetchCategories()

        val calendar = Calendar.getInstance()
        // First day of the month

        // First day of the month
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0) // Set to midnight (24-hour format)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val monthStart = calendar.timeInMillis / 1000 //

// Last day of the month
        calendar.set(
            Calendar.DAY_OF_MONTH,
            calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        ) // Set to last day
        calendar.set(Calendar.HOUR_OF_DAY, 23) // Set to 23:59:59
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val monthEnd = calendar.timeInMillis / 1000 //
        fetchCategoryWiseData(monthStart, monthEnd)

        fetchMonthlyLimit()
    }

    fun fetchMonthlyLimit() {
        viewModelScope.launch {
            expenseUseCaseContainer.getMonthlyLimitUseCase.execute().collect { monthlyLimit ->
                _budgetMonthly.value = monthlyLimit
            }
        }
    }

    private fun updateMonthlyLimit(monthlyLimit: MonthlyLimit) {
        viewModelScope.launch {
            expenseUseCaseContainer.updateMonthlyLimitUseCase.execute(monthlyLimit)
        }
    }

    fun fetchCategories() {
        viewModelScope.launch {
            expenseUseCaseContainer.getCategoriesUseCase.execute().collect { categoriesList ->
                _categories.value = categoriesList
            }
        }
    }

    // Set the selected category
    fun setSelectedCategory(categoryId: Long) {
        _selectedCategoryId.value = categoryId
    }

    // Reset selected category
    fun resetSelectedCategory() {
        _selectedCategoryId.value = null
    }

    fun resetQuery() {

        _expenses.value = emptyList()
        currentOffset = 0
        // limit = 20


    }


    // Set the selected category
    fun setSelectedCategoryRecordFragment(categoryId: Long) {
        _selectedCategoryIdRecordFragment.value = categoryId
        resetQuery()
        fetchExpenses()
    }

    // Reset selected category
    fun resetSelectedCategoryRecordFragment() {
        _selectedCategoryIdRecordFragment.value = null
        resetQuery()
        fetchExpenses()
    }

    private var fetchExpensesJob: Job? = null

    fun fetchExpenses() {
        //if (_isLoading.value) return  // Prevent fetching if already loading

        // Cancel the current job if it's active
        fetchExpensesJob?.cancel()

        // Start a new job
        fetchExpensesJob = viewModelScope.launch {
            _isLoading.value = true  // Set loading flag to true before fetch


            var categoryId: Long = -1

            categoryId = if (selectedCategoryIdRecordFragment.value == null) {
                -1
            } else {
                selectedCategoryIdRecordFragment.value!!
            }


            expenseUseCaseContainer.getExpenseUseCase.execute(limit, currentOffset, categoryId)
                .collect { newExpenses ->
                    val currentList = _expenses.value
                    val updatedList = (currentList + newExpenses)
                        .distinctBy { it.id }
                        .sortedByDescending { it.date }

                    _expenses.value = updatedList
                    limit += 20

                    _isLoading.value = false  // Set loading flag to false after fetch
                }
        }
    }


    fun fetchCategoryWiseData(timeStart: Long, timeEnd: Long) {
        viewModelScope.launch {
            expenseUseCaseContainer.getCategoryWiseDataUseCase.execute(timeStart, timeEnd)
                .collect { newCategoryWiseData ->
                    _categoryWiseData.value = newCategoryWiseData
                }
        }
    }

    fun scrollToTop() {
        _scrollToTop.value = true
    }

    fun resetScrollToTopFlag() {
        _scrollToTop.value = false
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            expenseUseCaseContainer.deleteExpenseUseCase.execute(expense)
            _expenses.value = _expenses.value.filter { it.id != expense.id }
        }

        fetchExpenses()
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            expenseUseCaseContainer.updateExpenseUseCase.execute(expense)
            _expenses.value = _expenses.value.map {
                if (it.id == expense.id) expense else it
            }

            fetchExpenses()
        }

    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            expenseUseCaseContainer.saveExpenseUseCase.execute(expense)
            delay(500)
            fetchExpenses()
        }
    }

    suspend fun insertTestExpense() {
        val expenseTest = Expense(
            id = 0,
            date = System.currentTimeMillis() / 1000,
            name = "TestExpense",
            amount = 120.0,
            categoryId = 1
        )
        expenseUseCaseContainer.saveExpenseUseCase.execute(expenseTest)
        scrollToTop()
    }

    suspend fun createOrUpdate(createOrEdit: CreateOrEdit, expensePassed: Expense?): Boolean {


        if (expensePassed!!.name.isEmpty()) {
            _errorEvent.value = Event("Please enter valid name")
            return false
        } else if (expensePassed!!.amount == 0.0) {
            _errorEvent.value = Event("Please enter valid amount")
            return false

        } else if (expensePassed!!.categoryId == -1L) {
            _errorEvent.value = Event("Please select a category")
            return false

        } else {
            when (createOrEdit) {
                CreateOrEdit.CREATE -> {
                    addExpense(expensePassed)
                    delay(500)
                    scrollToTop()
                }

                CreateOrEdit.EDIT -> {
                    updateExpense(expensePassed)
                }
            }


            return true

        }


    }

    fun editBudget(amount: Double): Boolean {
        if (amount == 0.0) {
            _errorEventEditBudget.value = Event("Please enter valid amount")
            return false

        }
        updateMonthlyLimit(
            MonthlyLimit(
                id = 1,
                amount = amount
            )
        )

        return true
    }

    suspend fun addExpenseWithDelay(expense: Expense) {
        addExpense(expense)
        val tempList = _expenses.value.toMutableList()
        tempList.add(expense)
        tempList.sortedByDescending { it.date }
        _expenses.value = tempList
    }
}
