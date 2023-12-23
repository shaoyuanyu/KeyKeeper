package com.yusy.keykeeper.data.account

import kotlinx.coroutines.flow.Flow

interface AccountsRepository {
    fun getAllAccountsStream(): Flow<List<Account>>

    fun getAccountStreamByUid(uid: String): Flow<Account?>

    fun getAccountStreamById(id: Int): Flow<Account>

    suspend fun insertAccount(account: Account)

    suspend fun deleteAccount(account: Account)

    suspend fun updateAccount(account: Account)
}