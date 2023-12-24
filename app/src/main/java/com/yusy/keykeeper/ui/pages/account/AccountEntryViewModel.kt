package com.yusy.keykeeper.ui.pages.account

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import com.yusy.keykeeper.data.account.AccountsRepository
import com.yusy.keykeeper.utils.generatePasswd
import com.yusy.keykeeper.utils.storeIcon
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AccountEntryViewModel(private val accountsRepository: AccountsRepository): ViewModel() {
    // account entry ui state
    var accountEntryUiState by mutableStateOf(AccountEntryUiState())
        private set

    fun updateAccountEntryUiState(accountDetails: AccountDetails) {
        accountEntryUiState = AccountEntryUiState(
            accountDetails = accountDetails,
            isValid = validateInput(accountDetails)
        )
    }

    fun generateSecurePasswd() {
        updateAccountEntryUiState(accountEntryUiState.accountDetails.copy(plainPasswd = generatePasswd()))
    }

    suspend fun saveAccount(context: Context) {
        if (validateInput()) {
            val account = accountEntryUiState.accountDetails.toAccount()
            val accountIcon = accountEntryUiState.accountDetails.appIcon!!

            // time
            val formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            account.createdAt = formattedTime

            // icon
            account.appIconPath = storeIcon(
                context = context,
                iconName = account.appUrl,
                iconImageBitmap = accountIcon
            )

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

    // app choose ui state
    var appChooseUiState by mutableStateOf(AppChooseUiState())
        private set

    fun updateAppChooseUiState(targetName: String) {
        appChooseUiState = AppChooseUiState(
            localDeskAppList = appChooseUiState.localDeskAppList,
            targetAppList = appChooseUiState.targetAppList,
            targetName = targetName
        )
    }

    fun setLoadingLocalDeskAppStatus(status: Boolean) {
        accountEntryUiState.isReadingLocalDeskApp = status
        appChooseUiState.isReadingLocalDeskApp = status
    }

    /**
     * 获取桌面APP列表
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun getDeskAppList(context: Context) {
        val appList: ArrayList<LocalDeskApp> = arrayListOf()

        val intent = Intent()
        intent.setAction(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        GlobalScope.launch {
            // 获取package信息
            val resolveInfoList = context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)

            for (info in resolveInfoList) {
                appList.add(
                    LocalDeskApp(
                        appName = info.loadLabel(context.packageManager).toString(),
                        packageName = info.activityInfo.packageName,
                        appIcon = info.loadIcon(context.packageManager).toBitmap().asImageBitmap()
                    )
                )

            }

            //
            appChooseUiState = AppChooseUiState(
                localDeskAppList = appList.toList(),
                targetAppList = appList.toList()
            )

            // 结束加载状态
            setLoadingLocalDeskAppStatus(false)
        }
    }

    /**
     * 搜索app
     */
    fun searchApp() {
        setLoadingLocalDeskAppStatus(true)

        val targetNameFormatted = appChooseUiState.targetName.lowercase()

        val targetAppArrayList = arrayListOf<LocalDeskApp>()
        for (localDeskApp in appChooseUiState.localDeskAppList) {
            if (localDeskApp.appName.lowercase().contains(targetNameFormatted) || localDeskApp.packageName.lowercase().contains(targetNameFormatted)) {
                targetAppArrayList.add(localDeskApp)
            }
        }
        appChooseUiState.targetAppList = targetAppArrayList.toList()

        setLoadingLocalDeskAppStatus(false)
    }

    /**
     * 选中APP
     */
    fun chooseApp(localDeskApp: LocalDeskApp) {
        updateAccountEntryUiState(
            accountEntryUiState.accountDetails.copy(
                appName = localDeskApp.appName,
                appUrl = localDeskApp.packageName,
                appIcon = localDeskApp.appIcon
            )
        )
    }
}

data class AccountEntryUiState(
    val accountDetails: AccountDetails = AccountDetails(),
    var isReadingLocalDeskApp: Boolean = false,
    val isValid: Boolean = false
)

fun AccountDetails.toAccountEntryUiState(isValid: Boolean): AccountEntryUiState = AccountEntryUiState(
    accountDetails = this,
    isReadingLocalDeskApp = false,
    isValid = isValid
)

data class LocalDeskApp (
    val appName: String,
    val packageName: String,
    val appIcon: ImageBitmap
)

data class AppChooseUiState(
    val localDeskAppList: List<LocalDeskApp> = listOf(),
    var targetAppList: List<LocalDeskApp> = listOf(),
    var targetName: String = "",
    var isReadingLocalDeskApp: Boolean = false
)
