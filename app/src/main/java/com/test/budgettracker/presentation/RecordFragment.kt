package com.test.budgettracker.presentation

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.test.budgettracker.R
import com.test.budgettracker.data.model.Category
import com.test.budgettracker.data.model.Expense
import com.test.budgettracker.presentation.adapter.ExpenseAdapter
import com.test.budgettracker.presentation.viewmodel.ExpenseViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecordFragment : Fragment(R.layout.fragment_record) {

    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var chipGroup: ChipGroup
    private lateinit var textViewNoRecordsFound:TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseViewModel = (activity as MainActivity).expenseViewModel
        expenseAdapter = (activity as MainActivity).expenseAdapter


        chipGroup = view.findViewById(R.id.chipGroupRecordFragment)
        textViewNoRecordsFound = view.findViewById(R.id.textViewNoRecordsFound)

        view.findViewById<Button>(R.id.createRecordButton).setOnClickListener {
            /* lifecycleScope.launch {
                 callFun()
             }*/

            val action = RecordFragmentDirections.actionRecordFragmentToCreateRecordFragment(
                createOrEdit = CreateOrEdit.CREATE,
                expense = null

            )
            findNavController().navigate(action)
        }

        // Initialize RecyclerView and adapter
        //  expenseAdapter = ExpenseAdapter()

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewRecord).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = expenseAdapter

            expenseAdapter.setOnItemDeleteClickListener { expense: Expense ->

                expenseViewModel.deleteExpense(expense)
                Snackbar.make(view, "Deleted Successfully", Snackbar.LENGTH_LONG)
                    .apply {
                        setAction("Undo") {
                            lifecycleScope.launch {
                                expenseViewModel.addExpenseWithDelay(expense)
                            }
                        }
                        show()
                    }


            }

            expenseAdapter.setOnItemEditClickListener { expense: Expense ->

                val action = RecordFragmentDirections.actionRecordFragmentToCreateRecordFragment(
                    createOrEdit = CreateOrEdit.EDIT,
                    expense = expense

                )
                findNavController().navigate(action)

            }

            // Pagination listener
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    // Check if we are near the end of the list, and fetch more items
                    if (!expenseViewModel.isLoading.value && lastVisibleItemPosition + 2 >= totalItemCount) {
                        expenseViewModel.fetchExpenses()  // Trigger data fetch
                    }
                }
            })
        }


        // Observe categories from ViewModel and populate the ChipGroup
        lifecycleScope.launch {
            expenseViewModel.categories.collect { categories ->

                populateCategoryChips(categories)
            }
        }


        // Observe data
        lifecycleScope.launch {
            expenseViewModel.expenses.collectLatest { expenses ->

                if(expenses.isEmpty()){
                    textViewNoRecordsFound.visibility=View.VISIBLE
                }else{
                    textViewNoRecordsFound.visibility=View.GONE

                }

                expenseAdapter.differ.submitList(expenses)
            }
        }

        // Observe scroll-to-top action
        lifecycleScope.launch {
            expenseViewModel.scrollToTop.collect { shouldScrollToTop ->
                if (shouldScrollToTop) {
                    recyclerView.scrollToPosition(0)
                    expenseViewModel.resetScrollToTopFlag()
                }
            }
        }

        // Initial fetch of expenses
        expenseViewModel.fetchExpenses()
    }

    private suspend fun callFun() {
        expenseViewModel.insertTestExpense()
    }



    // Populate the ChipGroup with categories from the ViewModel
    private fun populateCategoryChips(categories: List<Category>) {
        chipGroup.removeAllViews() // Clear any existing chips




        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            // checkedIds is a list of selected chip IDs. If no chip is selected, the list will be empty.
            if (checkedIds.isEmpty()) {
                // No chip is selected
                expenseViewModel.resetSelectedCategoryRecordFragment() // Set selected category to null
            } else {
                // Get the selected chip ID (first item in the list, since only one chip can be selected)
                val selectedChipId = checkedIds.first()
                val selectedChip = group.findViewById<Chip>(selectedChipId)

                val selectedCategoryId = selectedChip?.let {
                    categories.find { category -> category.name == it.text }?.id
                }

                // Update ViewModel with the selected category ID
                selectedCategoryId?.let {
                    expenseViewModel.setSelectedCategoryRecordFragment(it)
                }
            }
        }


        categories.forEach { category ->
            val chip = Chip(requireContext()).apply {
                text = category.name
                isCheckable = true
                isChecked = false // No chip is selected by default

            }
            chipGroup.addView(chip)
        }
    }
}