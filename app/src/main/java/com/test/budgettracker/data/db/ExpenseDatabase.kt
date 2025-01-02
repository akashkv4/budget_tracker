package com.test.budgettracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.test.budgettracker.data.model.Category
import com.test.budgettracker.data.model.Expense
import com.test.budgettracker.data.model.MonthlyLimit

@Database(
    entities = [Expense::class, Category::class,MonthlyLimit::class],
    version = 1,
    exportSchema = false
)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun getExpenseDao(): ExpenseDao
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getMonthlyLimitDao(): MonthlyLimitDao


    companion object {
        val prepopulateCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL("INSERT INTO categories (id, name) VALUES (1, 'Food')")
                db.execSQL("INSERT INTO categories (id, name) VALUES (2, 'Transport')")
                db.execSQL("INSERT INTO categories (id, name) VALUES (3, 'Entertainment')")
            }
        }
    }

}