package com.nkuppan.expensemanager.data.di

import android.content.Context
import com.nkuppan.expensemanager.core.common.utils.AppCoroutineDispatchers
import com.nkuppan.expensemanager.data.datastore.CurrencyDataStore
import com.nkuppan.expensemanager.data.datastore.SettingsDataStore
import com.nkuppan.expensemanager.data.datastore.ThemeDataStore
import com.nkuppan.expensemanager.data.db.dao.AccountDao
import com.nkuppan.expensemanager.data.db.dao.CategoryDao
import com.nkuppan.expensemanager.data.db.dao.TransactionDao
import com.nkuppan.expensemanager.data.mappers.*
import com.nkuppan.expensemanager.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependence injection repository module. This will create repository and use cases related object
 * and it's relations.
 */
@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCategoryRepository(
        categoryDao: CategoryDao,
        appCoroutineDispatchers: AppCoroutineDispatchers
    ): CategoryRepository {
        return CategoryRepositoryImpl(
            categoryDao,
            appCoroutineDispatchers
        )
    }

    @Provides
    @Singleton
    fun provideAccountRepository(
        accountDao: AccountDao,
        appCoroutineDispatchers: AppCoroutineDispatchers
    ): AccountRepository {
        return AccountRepositoryImpl(
            accountDao,
            appCoroutineDispatchers
        )
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(
        transactionDao: TransactionDao,
        appCoroutineDispatchers: AppCoroutineDispatchers
    ): TransactionRepository {
        return TransactionRepositoryImpl(
            transactionDao,
            appCoroutineDispatchers
        )
    }

    @Provides
    @Singleton
    fun provideThemeRepository(
        dataStore: ThemeDataStore,
        dispatchers: AppCoroutineDispatchers
    ): ThemeRepository {
        return ThemeRepositoryImpl(dataStore, dispatchers)
    }

    @Provides
    @Singleton
    fun provideCurrencyRepository(
        dataStore: CurrencyDataStore,
        dispatchers: AppCoroutineDispatchers
    ): CurrencyRepository {
        return CurrencyRepositoryImpl(dataStore, dispatchers)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(
        @ApplicationContext context: Context,
        dataStore: SettingsDataStore,
        dispatchers: AppCoroutineDispatchers
    ): SettingsRepository {
        return SettingsRepositoryImpl(context, dataStore, dispatchers)
    }
}
