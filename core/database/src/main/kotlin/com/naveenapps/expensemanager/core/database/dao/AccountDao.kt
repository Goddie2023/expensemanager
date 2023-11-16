package com.naveenapps.expensemanager.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.naveenapps.expensemanager.core.database.entity.AccountEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface AccountDao : BaseDao<AccountEntity> {

    @Query("SELECT * FROM account ORDER BY created_on DESC")
    fun getAccounts(): Flow<List<AccountEntity>?>

    @Query("SELECT * FROM account ORDER BY created_on DESC")
    fun getAllValues(): List<AccountEntity>?

    @Query("SELECT * FROM account WHERE id = :id")
    fun findById(id: String): AccountEntity?

    @Query("SELECT COUNT(*) FROM account")
    fun getCount(): Long
}