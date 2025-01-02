package com.test.budgettracker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.budgettracker.data.model.MonthlyLimit
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthlyLimitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonthlyLimit(monthlyLimit: MonthlyLimit)


    @Query("SELECT * FROM monthly_limit where id = 1")
    fun getLimit(): Flow<List<MonthlyLimit>>



}