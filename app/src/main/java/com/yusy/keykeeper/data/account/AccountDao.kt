package com.yusy.keykeeper.data.account

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(account: Account)

    @Update
    suspend fun update(account: Account)

    @Delete
    suspend fun delete(account: Account)

    @Query("SELECT * from Account ORDER BY createdAt ASC")
    fun getAllAccounts(): Flow<List<Account>>

    @Query("SELECT * from Account WHERE uid = :uid")
    fun getAccountByUid(uid: String): Flow<Account>

    @Query("SELECT * from Account WHERE id = :id")
    fun getAccountById(id: Int): Flow<Account>
}