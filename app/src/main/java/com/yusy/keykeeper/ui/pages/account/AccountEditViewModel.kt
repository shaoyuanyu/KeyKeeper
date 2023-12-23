package com.yusy.keykeeper.ui.pages.account

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusy.keykeeper.data.account.AccountsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AccountEditViewModel(
    private val accountsRepository: AccountsRepository,
): ViewModel() {
    var accountEditUiState by mutableStateOf(AccountEditUiState())
        private set

    private var id: Int? = null

    fun setId(id: Int) {
        this.id = id

        viewModelScope.launch {
            accountEditUiState = accountsRepository.getAccountStreamById(id)
                .first()
                .toAccountDetails()
                .toAccountEditUiState(true)
        }
    }

    fun updateUiState(accountDetails: AccountDetails) {
        accountEditUiState = AccountEditUiState(
            accountDetails = accountDetails,
            isValid = validateInput(accountDetails)
        )
    }

    suspend fun saveAccount() {
        if (validateInput()) {
            // time
            val formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
            val account = accountEditUiState.accountDetails.toAccount()
            account.createdAt = formattedTime

            //
            accountsRepository.updateAccount(account)
        }
    }

    // appName和plainPasswd不可为空
    private fun validateInput(accountDetails: AccountDetails = accountEditUiState.accountDetails): Boolean {
        return with(accountDetails) {
            appName.isNotBlank() && plainPasswd.isNotBlank()
        }
    }
}

data class AccountEditUiState(
    val accountDetails: AccountDetails = AccountDetails(),
    var isValid: Boolean = true
)

fun AccountDetails.toAccountEditUiState(isValid: Boolean): AccountEditUiState = AccountEditUiState(
    accountDetails = this,
    isValid = isValid
)
