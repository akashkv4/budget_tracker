package com.test.budgettracker.presentation.di

import android.app.Application
import androidx.room.Room
import com.test.budgettracker.data.db.ExpenseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Singleton
    @Provides
    fun provideExpenseDatabase(app: Application): ExpenseDatabase {
        return Room.databaseBuilder(app, ExpenseDatabase::class.java, "expense_db")
            .fallbackToDestructiveMigration()
            .addCallback(ExpenseDatabase.prepopulateCallback)
            .build()

    }

    @Singleton
    @Provides
    fun provideExpenseDao(expenseDatabase: ExpenseDatabase) = expenseDatabase.getExpenseDao()

    @Singleton
    @Provides
    fun provideCategoryDao(expenseDatabase: ExpenseDatabase) = expenseDatabase.getCategoryDao()

    @Singleton
    @Provides
    fun provideMonthlyLimitDao(expenseDatabase: ExpenseDatabase) = expenseDatabase.getMonthlyLimitDao()
}