package com.yusy.keykeeper.ui.pages.account

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.yusy.keykeeper.data.account.AccountsRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AccountEntryViewModel(private val accountsRepository: AccountsRepository): ViewModel() {
    // hold current account ui state
    var accountEntryUiState by mutableStateOf(AccountEntryUiState())
        private set

    fun updateUiState(accountDetails: AccountDetails) {
        accountEntryUiState = AccountEntryUiState(
            accountDetails = accountDetails,
            isValid = validateInput(accountDetails)
        )
    }

    suspend fun saveAccount() {
        if (validateInput()) {
            // time
            val formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val account = accountEntryUiState.accountDetails.toAccount()
            account.createdAt = formattedTime

            //
            accountsRepository.insertAccount(account)
        }
    }

    // 输入的appName和plainPasswd不可为空
    private fun validateInput(accountDetails: AccountDetails = accountEntryUiState.accountDetails): Boolean {
        return with(accountDetails) {
            appName.isNotBlank() && plainPasswd.isNotBlank()
        }
    }
}

data class AccountEntryUiState(
    val accountDetails: AccountDetails = AccountDetails(),
    val isValid: Boolean = false
)

fun AccountDetails.toAccountEntryUiState(isValid: Boolean): AccountEntryUiState = AccountEntryUiState(
    accountDetails = this,
    isValid = isValid
)
