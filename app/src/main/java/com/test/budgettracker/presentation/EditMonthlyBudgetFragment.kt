package com.test.budgettracker.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.test.budgettracker.R
import com.test.budgettracker.presentation.viewmodel.ExpenseViewModel
import kotlinx.coroutines.launch


class EditMonthlyBudgetFragment : Fragment() {


    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var editTextNumberDecimal:EditText
    private lateinit var buttonEditMonthlyBudget:Button
    var bProcessing = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_monthly_budget, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseViewModel = (activity as MainActivity).expenseViewModel
        editTextNumberDecimal = view.findViewById(R.id.editTextNumberDecimal)
        buttonEditMonthlyBudget = view.findViewById(R.id.buttonEditMonthlyBudget)

        buttonEditMonthlyBudget.setOnClickListener {

            if (bProcessing){
                return@setOnClickListener
            }

            var amount: Double = 0.0
            amount = try {
                editTextNumberDecimal.text.toString().toDouble()
            }catch (e:Exception){
                0.0
            }

            lifecycleScope.launch {
                bProcessing= true
                if (expenseViewModel.editBudget(
                        amount
                    )){
                    findNavController().navigateUp()

                }
                bProcessing = false
            }


        }

        // Observe validation errors
        expenseViewModel.errorEventEditBudget.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { errorMessage ->
                Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT).show()
            }
        }

    }


}