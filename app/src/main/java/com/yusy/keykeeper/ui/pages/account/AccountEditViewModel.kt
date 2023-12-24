package com.yusy.keykeeper.ui.pages.account

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusy.keykeeper.data.account.Account
import com.yusy.keykeeper.data.account.AccountsRepository
import com.yusy.keykeeper.utils.generatePasswd
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AccountEditViewModel(
    private val accountsRepository: AccountsRepository,
): ViewModel() {
    var accountEditUiState by mutableStateOf(AccountEditUiState())
        private set

    // 用于删除
    private lateinit var accountToDelete: Account

    private var id: Int? = null

    fun setId(id: Int) {
        this.id = id

        viewModelScope.launch {
            accountToDelete = accountsRepository.getAccountStreamById(id)
                .first()

            accountEditUiState = accountToDelete
                .toAccountDetails()
                .toAccountEditUiState(true)
        }
    }

    fun updateAccountEditUiState(accountDetails: AccountDetails) {
        accountEditUiState = AccountEditUiState(
            accountDetails = accountDetails,
            isValid = validateInput(accountDetails)
        )
    }

    fun generateSecurePasswd() {
        updateAccountEditUiState(accountEditUiState.accountDetails.copy(plainPasswd = generatePasswd()))
    }

    suspend fun saveAccount() {
        if (validateInput()) {
            // time
            val formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val account = accountEditUiState.accountDetails.toAccount()
            account.createdAt = formattedTime

            //
            accountsRepository.updateAccount(account)

            // 更新accountToDelete
            accountToDelete = account
        }
    }

    suspend fun deleteAccount() {
        accountsRepository.deleteAccount(accountToDelete)
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
