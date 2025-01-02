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
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.test.budgettracker.R
import com.test.budgettracker.data.model.Category
import com.test.budgettracker.data.model.Expense
import com.test.budgettracker.presentation.viewmodel.ExpenseViewModel
import kotlinx.coroutines.launch

enum class CreateOrEdit {
    CREATE,
    EDIT
}

class CreateRecordFragment : Fragment() {


    private var createOrEdit: CreateOrEdit = CreateOrEdit.CREATE
    private lateinit var expenseViewModel: ExpenseViewModel

    private lateinit var chipGroup: ChipGroup
    private lateinit var editTextName: EditText
    private lateinit var editTextAmount: EditText
    private lateinit var buttonCreateUpdate: Button

    val listOfCategory = mutableListOf<Category>()
    var bProcessing = false
    var expensePassed: Expense? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_record, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseViewModel = (activity as MainActivity).expenseViewModel

        chipGroup = view.findViewById(R.id.chipGroupCategory)
        buttonCreateUpdate = view.findViewById<Button>(R.id.buttonCreateUpdate)
        editTextName = view.findViewById(R.id.editTextText)
        editTextAmount = view.findViewById(R.id.editTextNumberDecimal2)


        buttonCreateUpdate.setOnClickListener {

            if (bProcessing){
                return@setOnClickListener
            }
            // Handle button click

            var amount: Double = 0.0
            amount = try {
                editTextAmount.text.toString().toDouble()
            }catch (e:Exception){
                0.0
            }
            var categoryId:Long = -1

            categoryId = if(expenseViewModel.selectedCategoryId.value==null){
                -1
            }else{
                expenseViewModel.selectedCategoryId.value!!
            }

            var expenseToSubmit:Expense? = null
            when(createOrEdit){

                CreateOrEdit.CREATE -> {




                    expenseToSubmit= Expense(
                        id = 0,
                        date = System.currentTimeMillis() / 1000,
                        name = editTextName.text.toString(),
                        amount = amount,
                        categoryId = categoryId
                    )
                }
                CreateOrEdit.EDIT -> {



                    expenseToSubmit= Expense(
                        id = expensePassed!!.id,
                        date = expensePassed!!.date,
                        name = editTextName.text.toString(),
                        amount = amount,
                        categoryId = categoryId
                    )
            }
            }



            lifecycleScope.launch {
                bProcessing= true
                if (expenseViewModel.createOrUpdate(
                        createOrEdit,
                        expenseToSubmit
                    )){
                    findNavController().navigateUp()

                }
                bProcessing = false
            }





        }

         // Observe validation errors
        expenseViewModel.errorEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { errorMessage ->
                Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT).show()
            }
        }





// Retrieve the passed argument using the generated Args class
        val args = CreateRecordFragmentArgs.fromBundle(requireArguments())
        createOrEdit = args.createOrEdit
        expensePassed = args.expense
        // Use the received argument (e.g., show it in the UI or use it for logic)
        expenseViewModel.resetSelectedCategory()// No chip is selected by default

        when (createOrEdit) {
            CreateOrEdit.CREATE -> {
                buttonCreateUpdate.text = "Create"
            }

            CreateOrEdit.EDIT -> {
                (activity as MainActivity).updateAppBarTitle("Edit Record")
                buttonCreateUpdate.text = "Edit"
                editTextName.setText(expensePassed?.name ?: "")
                editTextAmount.setText(expensePassed?.amount.toString() ?: "")

            }
        }


        // Observe categories from ViewModel and populate the ChipGroup
        lifecycleScope.launch {
            expenseViewModel.categories.collect { categories ->
                listOfCategory.clear()
                listOfCategory.addAll(categories)

                populateCategoryChips(categories)
            }
        }



    }

    // Populate the ChipGroup with categories from the ViewModel
    private fun populateCategoryChips(categories: List<Category>) {
        chipGroup.removeAllViews() // Clear any existing chips


        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            // checkedIds is a list of selected chip IDs. If no chip is selected, the list will be empty.
            if (checkedIds.isEmpty()) {
                // No chip is selected
                expenseViewModel.resetSelectedCategory() // Set selected category to null
            } else {
                // Get the selected chip ID (first item in the list, since only one chip can be selected)
                val selectedChipId = checkedIds.first()
                val selectedChip = group.findViewById<Chip>(selectedChipId)

                val selectedCategoryId = selectedChip?.let {
                    categories.find { category -> category.name == it.text }?.id
                }

                // Update ViewModel with the selected category ID
                selectedCategoryId?.let {
                    expenseViewModel.setSelectedCategory(it)
                }
            }
        }


        categories.forEach { category ->
            val chip = Chip(requireContext()).apply {
                text = category.name
                isCheckable = true
                isChecked = false // No chip is selected by default

                if(createOrEdit==CreateOrEdit.EDIT){
                    if(expensePassed!!.categoryId==category.id){
                        isChecked = true
                        expenseViewModel.setSelectedCategory(category.id)// No chip is selected by default
                    }
                }


          /*      setOnClickListener {
                    expenseViewModel.setSelectedCategory(category.id)
                }*/
            }
            chipGroup.addView(chip)
        }
    }

}