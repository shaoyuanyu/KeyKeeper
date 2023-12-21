package com.yusy.keykeeper.data.account

import kotlinx.coroutines.flow.Flow

class OfflineAccountsRepository(private val accountDao: AccountDao) : AccountsRepository {
    override fun getAllAccountsStream(): Flow<List<Account>> = accountDao.getAllAccounts()

    override fun getAccountStreamByUid(uid: String): Flow<Account?> = accountDao.getAccountByUid(uid)

    override suspend fun insertAccount(account: Account) = accountDao.insert(account)

    override suspend fun deleteAccount(account: Account) = accountDao.delete(account)

    override suspend fun updateAccount(account: Account) = accountDao.update(account)
}