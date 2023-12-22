package com.yusy.keykeeper.ui.pages.account

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.yusy.keykeeper.data.account.AccountsRepository

class AccountEntryViewModel(private val accountsRepository: AccountsRepository): ViewModel() {
    // hold current account ui state
    var accountUiState by mutableStateOf(AccountUiState())
        private set

    fun updateUiState(accountDetails: AccountDetails) {
        accountUiState = AccountUiState(
            accountDetails = accountDetails,
            isValid = validateInput(accountDetails)
        )
    }

    suspend fun saveAccount() {
        if (validateInput()) {
            accountsRepository.insertAccount(accountUiState.accountDetails.toAccount())
        }
    }

    // 输入的appName和plainPasswd不可为空
    private fun validateInput(accountDetails: AccountDetails = accountUiState.accountDetails): Boolean {
        return with(accountDetails) {
            appName.isNotBlank() && plainPasswd.isNotBlank()
        }
    }
}
