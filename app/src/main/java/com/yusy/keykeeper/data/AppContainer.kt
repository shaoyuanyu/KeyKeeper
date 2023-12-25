package com.yusy.keykeeper.data

import android.content.Context
import com.yusy.keykeeper.data.account.AccountDatabase
import com.yusy.keykeeper.data.account.AccountsRepository
import com.yusy.keykeeper.data.account.OfflineAccountsRepository

interface AppContainer {
    val accountsRepository: AccountsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val accountsRepository: AccountsRepository by lazy {
        OfflineAccountsRepository(
            accountDao = AccountDatabase.getDatabase(context).accountDao(),
            usedUidDao = AccountDatabase.getDatabase(context).usedUidDao()
        )
    }
}