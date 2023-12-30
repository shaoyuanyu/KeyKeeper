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

class AccountEditViewModel(val accountsRepository: AccountsRepository): ViewModel() {
    var accountEditUiState by mutableStateOf(AccountEditUiState())
//        private set

    // 用于删除
    lateinit var accountToDelete: Account
}

fun AccountEditViewModel.updateAccountEditUiState(accountDetails: AccountDetails) {
    accountEditUiState = accountEditUiState.updateAccountDetails(accountDetails)
}

fun AccountEditViewModel.changePasswdVisible() {
    accountEditUiState = accountEditUiState.updatePasswdVisibility()
}

fun AccountEditViewModel.generateSecurePasswd() {
    accountEditUiState = accountEditUiState.updateGeneratingPasswd()
}

fun AccountEditViewModel.setId(id: Int) {
    viewModelScope.launch {
        accountToDelete = accountsRepository.getAccountStreamById(id)
            .first()

        accountEditUiState = accountToDelete
            .toAccountDetails()
            .toAccountEditUiState(false)
    }
}

suspend fun AccountEditViewModel.saveAccount() {
    if (accountEditUiState.isValid) {
        // time
        val formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val account = accountEditUiState.accountDetails.toAccount()
        account.createdAt = formattedTime

        // icon path
        account.appIconPath = accountToDelete.appIconPath

        //
        accountsRepository.updateAccount(account)

        // 更新accountToDelete
        accountToDelete = account
    }
}

suspend fun AccountEditViewModel.deleteAccount() {
    // delete account
    accountsRepository.deleteAccount(accountToDelete)

    // used uid --
    // TODO:防错机制
    val usedUid = accountsRepository.getUsedUid(accountToDelete.uid).first()!!
    accountsRepository.updateUsedUid(usedUid.copy(usedTimes = --usedUid.usedTimes))
}

data class AccountEditUiState(
    val accountDetails: AccountDetails = AccountDetails(),
    val originalAccountDetails: AccountDetails = AccountDetails(),
    var isValid: Boolean = true,
    var isPasswdVisible: Boolean = false
) {
    fun updateAccountDetails(accountDetails: AccountDetails): AccountEditUiState =
        this.copy(
            accountDetails = accountDetails,
            isValid = doValidate(accountDetails)
        )

    fun updateGeneratingPasswd(): AccountEditUiState =
        updateAccountDetails(
            accountDetails = this.accountDetails.copy(plainPasswd = generatePasswd())
        )

    fun updatePasswdVisibility(): AccountEditUiState =
        this.copy(isPasswdVisible = !this.isPasswdVisible)

    private fun doValidate(accountDetails: AccountDetails): Boolean =
        (accountDetails != this.originalAccountDetails) && with(accountDetails) {
            // appName和plainPasswd不可为空
            appName.isNotBlank() && plainPasswd.isNotBlank()
        }
}

fun AccountDetails.toAccountEditUiState(isValid: Boolean): AccountEditUiState = AccountEditUiState(
    accountDetails = this,
    originalAccountDetails = this,
    isValid = isValid
)
