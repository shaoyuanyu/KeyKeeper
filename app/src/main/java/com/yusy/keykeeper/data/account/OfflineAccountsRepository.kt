package com.yusy.keykeeper.data.account

import kotlinx.coroutines.flow.Flow

class OfflineAccountsRepository(
    private val accountDao: AccountDao,
    private val usedUidDao: UsedUidDao
) : AccountsRepository {
    // Account
    override fun getAllAccountsStream(): Flow<List<Account>> = accountDao.getAllAccounts()

    override fun getAccountStreamById(id: Int): Flow<Account> = accountDao.getAccountById(id)

    override fun getAccountStreamByAppUrl(appUrl: String): Flow<List<Account>> = accountDao.getAccountByAppUrl(appUrl)

    override suspend fun insertAccount(account: Account) = accountDao.insert(account)

    override suspend fun deleteAccount(account: Account) = accountDao.delete(account)

    override suspend fun updateAccount(account: Account) = accountDao.update(account)

    // UsedUid
    override fun getAllUsedUid(): Flow<List<UsedUid>> = usedUidDao.getAllUsedUid()

    override fun getUsedUid(uid: String): Flow<UsedUid?> = usedUidDao.getUsedUid(uid)

    override suspend fun insertUsedUid(usedUid: UsedUid) = usedUidDao.insert(usedUid)

    override suspend fun updateUsedUid(usedUid: UsedUid) = usedUidDao.update(usedUid)

    override suspend fun deleteUsedUid(usedUid: UsedUid) =usedUidDao.delete(usedUid)
}