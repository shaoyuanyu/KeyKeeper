package com.yusy.keykeeper.data.account

import kotlinx.coroutines.flow.Flow

interface AccountsRepository {
    // Account
    fun getAllAccountsStream(): Flow<List<Account>>

    fun getAccountStreamByUid(uid: String): Flow<Account?>

    fun getAccountStreamById(id: Int): Flow<Account>

    suspend fun insertAccount(account: Account)

    suspend fun deleteAccount(account: Account)

    suspend fun updateAccount(account: Account)

    // UsedUid
    fun getAllUsedUid(): Flow<List<UsedUid>>

    fun getUsedUid(uid: String): Flow<UsedUid?>

    suspend fun insertUsedUid(usedUid: UsedUid)

    suspend fun updateUsedUid(usedUid: UsedUid)

    suspend fun deleteUsedUid(usedUid: UsedUid)
}