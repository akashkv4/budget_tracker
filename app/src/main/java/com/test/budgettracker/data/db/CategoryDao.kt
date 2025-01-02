package com.test.budgettracker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.budgettracker.data.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
      @Insert(onConflict = OnConflictStrategy.REPLACE)
      suspend fun insertCategory(category: Category)

      @Delete
      suspend fun deleteCategory(category: Category)

       @Query("SELECT * FROM categories")
       fun getAllCategory():   Flow<List<Category>>


}