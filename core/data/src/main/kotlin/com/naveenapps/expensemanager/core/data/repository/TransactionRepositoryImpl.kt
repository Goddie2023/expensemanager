package com.naveenapps.expensemanager.core.data.repository

import android.content.Context
import android.util.Log
import com.naveenapps.expensemanager.core.common.utils.AppCoroutineDispatchers
import com.naveenapps.expensemanager.core.data.mappers.toDomainModel
import com.naveenapps.expensemanager.core.data.mappers.toEntityModel
import com.naveenapps.expensemanager.core.database.dao.AccountDao
import com.naveenapps.expensemanager.core.database.dao.CategoryDao
import com.naveenapps.expensemanager.core.database.dao.TransactionDao
import com.naveenapps.expensemanager.core.database.entity.TransactionEntity
import com.naveenapps.expensemanager.core.database.entity.TransactionRelation
import com.naveenapps.expensemanager.core.model.Resource
import com.naveenapps.expensemanager.core.model.Transaction
import com.naveenapps.expensemanager.core.model.TransactionType
import com.naveenapps.expensemanager.core.model.isIncome
import com.naveenapps.expensemanager.core.model.isTransfer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    private val dispatchers: AppCoroutineDispatchers
) : TransactionRepository {

    override fun getAllTransaction(): Flow<List<Transaction>?> =
        transactionDao.getAllTransaction().map {
            convertTransactionAndCategory(it)
        }

    override suspend fun findTransactionById(transactionId: String): Resource<Transaction> =
        withContext(dispatchers.io) {
            return@withContext try {
                val transaction = transactionDao.findById(transactionId)

                if (transaction != null) {
                    Resource.Success(convertTransactionCategoryRelation(transaction))
                } else {
                    Resource.Error(KotlinNullPointerException())
                }
            } catch (e: Exception) {
                Resource.Error(e)
            }
        }

    override suspend fun addTransaction(transaction: Transaction): Resource<Boolean> =
        withContext(dispatchers.io) {
            return@withContext try {
                val response = transactionDao.insertTransaction(
                    transaction.toEntityModel(),
                    if (transaction.type == TransactionType.INCOME) {
                        transaction.amount.amount
                    } else {
                        transaction.amount.amount * -1
                    },
                    transaction.type.isTransfer()
                )
                Resource.Success(response != -1L)
            } catch (exception: Exception) {
                Resource.Error(exception)
            }
        }

    override suspend fun updateTransaction(transaction: Transaction): Resource<Boolean> =
        withContext(dispatchers.io) {
            return@withContext try {
                val transactionEntity = transaction.toEntityModel()
                transactionDao.removePreviousEnteredAmount(transactionEntity)
                transactionDao.updateTransaction(
                    transactionEntity,
                    if (transaction.type.isIncome()) {
                        transaction.amount.amount
                    } else {
                        transaction.amount.amount * -1
                    },
                    transaction.type.isTransfer()
                )
                Resource.Success(true)
            } catch (exception: Exception) {
                Resource.Error(exception)
            }
        }

    override suspend fun deleteTransaction(transaction: Transaction): Resource<Boolean> =
        withContext(dispatchers.io) {
            return@withContext try {
                val transactionEntity = transaction.toEntityModel()
                transactionDao.removePreviousEnteredAmount(transactionEntity)
                val response = transactionDao.delete(transactionEntity)
                Resource.Success(response != -1)
            } catch (exception: Exception) {
                Resource.Error(exception)
            }
        }


    override fun getTransactionsByAccountId(
        accounts: List<String>
    ): Flow<List<Transaction>?> =
        transactionDao.getTransactionsByAccounts(accounts).map { transaction ->
            transaction?.map { it.toDomainModel() }
        }

    override fun getTransactionByAccountIdAndDateFilter(
        accounts: List<String>,
        startDate: Long,
        endDate: Long
    ): Flow<List<Transaction>> =
        transactionDao.getTransactionsByAccountIdAndDateFilter(accounts, startDate, endDate).map {
            Log.i("TAG", "getTransactionByAccountIdAndDateFilter: ${it?.size}")
            convertTransactionAndCategory(it)
        }

    override fun getTransactionByDateFilter(
        startDate: Long,
        endDate: Long
    ): Flow<List<Transaction>> =
        transactionDao.getTransactionsByDateFilter(startDate, endDate).map {
            convertTransactionAndCategory(it)
        }

    private fun convertTransactionAndCategory(
        transactionWithCategory: List<TransactionRelation>?
    ): MutableList<Transaction> {

        val outputTransactions = mutableListOf<Transaction>()

        if (transactionWithCategory?.isNotEmpty() == true) {

            transactionWithCategory.forEach {
                val transaction = convertTransactionCategoryRelation(it)
                outputTransactions.add(transaction)
            }
        }
        return outputTransactions
    }

    private fun convertTransactionCategoryRelation(relation: TransactionRelation): Transaction {
        return relation.transactionEntity.toDomainModel().apply {
            category = relation.categoryEntity.toDomainModel()
            fromAccount = relation.fromAccountEntity.toDomainModel()
            toAccount = relation.toAccountEntity?.toDomainModel()
        }
    }

    private fun convertTransactionCategoryRelation(relation: TransactionEntity): Transaction {
        val transaction = relation.toDomainModel()
        categoryDao.findById(transaction.categoryId)?.let {
            transaction.category = it.toDomainModel()
        }
        accountDao.findById(transaction.fromAccountId)?.let {
            transaction.fromAccount = it.toDomainModel()
        }
        transaction.toAccountId?.let {
            accountDao.findById(it)?.let { accountEntity ->
                transaction.toAccount = accountEntity.toDomainModel()
            }
        }
        return transaction
    }

    override fun getTransactionAmount(
        accounts: List<String>,
        categories: List<String>,
        categoryType: List<Int>,
        startDate: Long,
        endDate: Long
    ): Flow<Double?> {
        return transactionDao.getTransactionTotalAmount(
            accounts, categories, categoryType, startDate, endDate
        )
    }

    override fun getFilteredTransaction(
        accounts: List<String>,
        categories: List<String>,
        categoryType: List<Int>,
        startDate: Long,
        endDate: Long
    ): Flow<List<Transaction>?> {
        return transactionDao.getFilteredTransaction(
            accounts, categories, categoryType, startDate, endDate
        ).map {
            Log.i("TAG", "getTransactionByAccountIdAndDateFilter: ${it?.size}")
            convertTransactionAndCategory(it)
        }
    }
}