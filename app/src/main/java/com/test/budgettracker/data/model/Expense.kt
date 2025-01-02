package com.test.budgettracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String,
    var amount: Double,
    val date: Long,
    var categoryId: Long


) : Serializable
