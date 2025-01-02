package com.test.budgettracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthly_limit")

data class MonthlyLimit(
    @PrimaryKey()
    val id: Long = 1,
    var amount: Double,
)
