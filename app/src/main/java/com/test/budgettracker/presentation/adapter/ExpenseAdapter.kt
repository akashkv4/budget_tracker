package com.test.budgettracker.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.budgettracker.data.model.Expense
import com.test.budgettracker.databinding.ListItemRecordBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExpenseAdapter : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {


    private val callback = object : DiffUtil.ItemCallback<Expense>(){
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,callback)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
         val binding = ListItemRecordBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ExpenseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
         val  expense = differ.currentList[position]
        holder.bind(expense)
    }


    inner class ExpenseViewHolder(val binding:ListItemRecordBinding) : RecyclerView.ViewHolder(binding.root) {

        val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
            Date()
        )

        fun bind(expense: Expense) {
            binding.textViewTitle.text = expense.name
            binding.textViewAmount.text = expense.amount.toString()
            binding.textViewDate.text = currentDateTime
            binding.imageViewDelete.setOnClickListener {
                onItemDeleteClickListener?.let {
                    it(expense)
                }
            }
            binding.imageViewEditRecord.setOnClickListener {
                onItemEditClickListener?.let {
                    it(expense)
                }
            }
        }

    }

    private var onItemDeleteClickListener :((Expense)->Unit)?=null
    private var onItemEditClickListener :((Expense)->Unit)?=null

    fun setOnItemDeleteClickListener(listener : (Expense)->Unit){
        onItemDeleteClickListener = listener
    }

    fun setOnItemEditClickListener(listener : (Expense)->Unit){
        onItemEditClickListener = listener
    }


}