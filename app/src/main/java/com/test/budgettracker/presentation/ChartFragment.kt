package com.test.budgettracker.presentation

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.test.budgettracker.R
import com.test.budgettracker.data.model.CategoryExpense
import com.test.budgettracker.presentation.viewmodel.ExpenseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar


class ChartFragment : Fragment() {


    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var pieChartCategoryWise: com.github.mikephil.charting.charts.PieChart
    private lateinit var pieChartMonthlySpend: com.github.mikephil.charting.charts.PieChart
    private lateinit var textViewMonthlyLimit: TextView
    private lateinit var linearLayoutEditBudget: LinearLayout
    private lateinit var imageViewEditBudget: ImageView


    private lateinit var buttonEditBudget: Button
    var monthlyBudget: Double = 0.0
    var totalAmountSpend: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        expenseViewModel = (activity as MainActivity).expenseViewModel

        pieChartCategoryWise = view.findViewById(R.id.pieChartCategoryWise)
        pieChartMonthlySpend = view.findViewById(R.id.pieChartMonthlySpend)

        textViewMonthlyLimit = view.findViewById(R.id.textViewMonthlyLimit)
        linearLayoutEditBudget = view.findViewById(R.id.linearLayoutEditBudget)
        imageViewEditBudget = view.findViewById(R.id.imageViewEditBudget)


        buttonEditBudget = view.findViewById(R.id.buttonEditBudget)


        setupPieChartCategoryWise()
        setupPieChartMonthlyLimit()

        lifecycleScope.launch {
            expenseViewModel.categoryWiseData.collectLatest { categoryWiseData ->


                if (categoryWiseData.isNotEmpty()) {
                    categoryWiseData.toMutableList().removeAll { it.totalAmount == 0.0 }
                    updateDataChartCategoryWise(categoryWiseData)
                    totalAmountSpend = categoryWiseData.sumOf { it.totalAmount }
                } else {
                    totalAmountSpend = 0.0
                }

            }


        }


        lifecycleScope.launch {


            expenseViewModel.budgetMonthly.collectLatest { monthlyBudgetLimits ->


                if (monthlyBudgetLimits.isNotEmpty()) {
                    monthlyBudget = monthlyBudgetLimits[0].amount
                } else {
                    monthlyBudget = 0.0
                }

                delay(400)
                updateUI()
            }
        }

        fetchCategoryWiseData()
        fetchMonthlyLimit()


        buttonEditBudget.setOnClickListener {
            val action = ChartFragmentDirections.actionChartFragmentToEditMonthlyBudgetFragment(

            )
            findNavController().navigate(action)
        }

        imageViewEditBudget.setOnClickListener {
            val action = ChartFragmentDirections.actionChartFragmentToEditMonthlyBudgetFragment(

            )
            findNavController().navigate(action)
        }

        updateUI()

    }

    private fun fetchMonthlyLimit() {

        expenseViewModel.fetchMonthlyLimit()
    }

    private fun updateUI() {

        if (monthlyBudget == 0.0) {

            pieChartMonthlySpend.visibility = View.GONE
            textViewMonthlyLimit.visibility = View.GONE

            linearLayoutEditBudget.visibility = View.VISIBLE

        } else {


            pieChartMonthlySpend.visibility = View.VISIBLE
            textViewMonthlyLimit.visibility = View.VISIBLE

            linearLayoutEditBudget.visibility = View.GONE

            textViewMonthlyLimit.text = "Monthly Limit : $monthlyBudget"


        }
        updateDataChartMonthlySpend()
    }

    private fun setupPieChartMonthlyLimit() {

        //pie chart
        pieChartMonthlySpend.setUsePercentValues(false)
        pieChartMonthlySpend.description.isEnabled = false
        pieChartMonthlySpend.setExtraOffsets(5F, 10F, 5F, 5F)

        pieChartMonthlySpend.dragDecelerationFrictionCoef = 0.95f


        pieChartMonthlySpend.isDrawHoleEnabled = true
        pieChartMonthlySpend.setHoleColor(Color.WHITE)

        pieChartMonthlySpend.setTransparentCircleColor(Color.WHITE)
        pieChartMonthlySpend.setTransparentCircleAlpha(110)

        pieChartMonthlySpend.holeRadius = 58f
        pieChartMonthlySpend.transparentCircleRadius = 61f

        pieChartMonthlySpend.setDrawCenterText(true)

        pieChartMonthlySpend.rotationAngle = 0.toFloat()
        // enable rotation of the chart by touch
        pieChartMonthlySpend.isRotationEnabled = true
        pieChartMonthlySpend.isHighlightPerTapEnabled = true


        pieChartMonthlySpend.animateY(1400, Easing.EaseInOutQuad)
        // pieChart.spin(2000, 0, 360);

        pieChartMonthlySpend.spin(2000, 0F, 360F, Easing.EaseInOutQuad)
        val l = pieChartMonthlySpend!!.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        // entry label styling
        pieChartMonthlySpend.setEntryLabelColor(Color.BLACK)
        pieChartMonthlySpend.setEntryLabelTextSize(12f)

    }


    private fun setupPieChartCategoryWise() {

        //pie chart
        pieChartCategoryWise.setUsePercentValues(false)
        pieChartCategoryWise.description.isEnabled = false
        pieChartCategoryWise.setExtraOffsets(5F, 10F, 5F, 5F)

        pieChartCategoryWise.dragDecelerationFrictionCoef = 0.95f


        pieChartCategoryWise.isDrawHoleEnabled = true
        pieChartCategoryWise.setHoleColor(Color.WHITE)

        pieChartCategoryWise.setTransparentCircleColor(Color.WHITE)
        pieChartCategoryWise.setTransparentCircleAlpha(110)

        pieChartCategoryWise.holeRadius = 58f
        pieChartCategoryWise.transparentCircleRadius = 61f

        pieChartCategoryWise.setDrawCenterText(true)

        pieChartCategoryWise.rotationAngle = 0.toFloat()
        // enable rotation of the chart by touch
        pieChartCategoryWise.isRotationEnabled = true
        pieChartCategoryWise.isHighlightPerTapEnabled = true


        pieChartCategoryWise.animateY(1400, Easing.EaseInOutQuad)
        // pieChart.spin(2000, 0, 360);

        pieChartCategoryWise.spin(2000, 0F, 360F, Easing.EaseInOutQuad)
        val l = pieChartCategoryWise!!.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        // entry label styling
        pieChartCategoryWise.setEntryLabelColor(Color.BLACK)
        pieChartCategoryWise.setEntryLabelTextSize(12f)

    }

    /*   override fun onResume() {
           super.onResume()

           fetchCategoryWiseData()

       }*/
    private fun fetchCategoryWiseData() {
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

        expenseViewModel.fetchCategoryWiseData(monthStart, monthEnd)
    }

    private fun updateDataChartCategoryWise(categoryWiseData: List<CategoryExpense>) {

        val entries: ArrayList<PieEntry> = ArrayList()

        for (categoryData in categoryWiseData) {
            entries.add(PieEntry(categoryData.totalAmount.toFloat(), categoryData.categoryName))

        }

        val dataSet = PieDataSet(entries, "")
        val data = PieData(dataSet)


        val colors: ArrayList<Int> = ArrayList()

        colors.add(Color.LTGRAY)
        colors.add(Color.CYAN)
        colors.add(Color.MAGENTA)

        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors

        data.setValueFormatter(PercentFormatter())

        data.setValueTextColor(Color.BLACK)
        pieChartCategoryWise?.setData(data)


    }

    private fun updateDataChartMonthlySpend() {

        val entries: ArrayList<PieEntry> = ArrayList()

        entries.add(PieEntry(totalAmountSpend.toFloat(), "Spend"))
        entries.add(PieEntry((monthlyBudget-totalAmountSpend).toFloat(), "Balance"))


        val dataSet = PieDataSet(entries, "")
        val data = PieData(dataSet)


        val colors: ArrayList<Int> = ArrayList()

        colors.add(Color.YELLOW)
        colors.add(Color.GREEN)
        colors.add(Color.MAGENTA)

        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors

        data.setValueFormatter(PercentFormatter())

        data.setValueTextColor(Color.BLACK)
        pieChartMonthlySpend?.setData(data)


    }


}