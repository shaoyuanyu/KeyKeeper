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
import androidx.lifecycle.viewModelScope
import com.yusy.keykeeper.data.account.AccountsRepository
import com.yusy.keykeeper.data.account.UsedUid
import com.yusy.keykeeper.utils.generatePasswd
import com.yusy.keykeeper.utils.storeIcon
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AccountEntryViewModel(val accountsRepository: AccountsRepository): ViewModel() {
    var accountEntryUiState by mutableStateOf(AccountEntryUiState())
//        private set

    var appChooseUiState by mutableStateOf(AppChooseUiState())
//        private set

    init {
        viewModelScope.launch {
            val usedUidArrayList = arrayListOf<String>()

            accountsRepository.getAllUsedUid()
                .first()
                .forEach {
                    usedUidArrayList.add(it.uid)
                }

            updateUsedUidList(usedUidArrayList.toList())
        }
    }
}

fun AccountEntryViewModel.updateAccountEntryUiState(accountDetails: AccountDetails) {
    accountEntryUiState = accountEntryUiState.updateAccountDetails(accountDetails)
}

fun AccountEntryViewModel.updateUsedUidList(uidList: List<String>) {
    accountEntryUiState = accountEntryUiState.updateUsedUidList(uidList)
}

fun AccountEntryViewModel.chooseUidCandidate(uid: String) {
    accountEntryUiState = accountEntryUiState.chooseUid(uid)
}

fun AccountEntryViewModel.dismissUidCandidate() {
    accountEntryUiState = accountEntryUiState.clearUidCandidateList()
}

fun AccountEntryViewModel.generateSecurePasswd() {
    accountEntryUiState = accountEntryUiState.updateGeneratingPasswd()
}

suspend fun AccountEntryViewModel.saveAccount(context: Context) {
    if (accountEntryUiState.isValid) {
        val account = accountEntryUiState.accountDetails.toAccount()
        val accountIcon = accountEntryUiState.accountDetails.appIcon

        // time
        val formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        account.createdAt = formattedTime

        // icon
        account.appIconPath = if (accountIcon != null) {
            storeIcon(context = context, iconName = account.appUrl, iconImageBitmap = accountIcon)
        } else {
            ""
        }

        // account table
        accountsRepository.insertAccount(account)

        // usedUid table
        val usedUid = accountsRepository.getUsedUid(account.uid).firstOrNull()

        if (usedUid == null) {
            accountsRepository.insertUsedUid(UsedUid(uid = account.uid, usedTimes = 1))
        } else {
            accountsRepository.updateUsedUid(usedUid.copy(usedTimes = ++usedUid.usedTimes))
        }
    }
}

fun AccountEntryViewModel.setLoadingLocalDeskAppStatus(status: Boolean) {
    accountEntryUiState = accountEntryUiState.updateReadingStatus(status)
    appChooseUiState = appChooseUiState.updateReadingStatus(status)
}

suspend fun AccountEntryViewModel.initAppChooseUiState(context: Context) {
    appChooseUiState = appChooseUiState.updateLocalDeskAppList(getDeskAppList(context))

    setLoadingLocalDeskAppStatus(false)
}

fun AccountEntryViewModel.updateSearchWord(searchWord: String) {
    setLoadingLocalDeskAppStatus(true)

    appChooseUiState = appChooseUiState.updateSearchWord(searchWord)

    setLoadingLocalDeskAppStatus(false)
}

fun AccountEntryViewModel.chooseApp(localDeskApp: LocalDeskApp) {
    updateAccountEntryUiState(
        accountEntryUiState.accountDetails.copy(
            appName = localDeskApp.appName,
            appUrl = localDeskApp.packageName,
            appIcon = localDeskApp.appIcon
        )
    )
}

/**
 * 获取桌面APP列表
 */
@OptIn(DelicateCoroutinesApi::class)
suspend fun getDeskAppList(context: Context): List<LocalDeskApp> {
    val intent = Intent()
    intent.setAction(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)

    return GlobalScope.async {
        val localDeskAppArrayList: ArrayList<LocalDeskApp> = arrayListOf()

        // 获取package信息
        val resolveInfoList = context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)

        for (info in resolveInfoList) {
            localDeskAppArrayList.add(
                LocalDeskApp(
                    appName = info.loadLabel(context.packageManager).toString(),
                    packageName = info.activityInfo.packageName,
                    appIcon = info.loadIcon(context.packageManager).toBitmap().asImageBitmap()
                )
            )

        }

        return@async localDeskAppArrayList.toList()

    }.await()
}


data class AccountEntryUiState(
    val accountDetails: AccountDetails = AccountDetails(),
    val usedUidList: List<String> = listOf(),
    val uidCandidateList: List<String> = listOf(),
    val isReadingLocalDeskApp: Boolean = false,
    val isValid: Boolean = false
) {
    fun updateAccountDetails(accountDetails: AccountDetails): AccountEntryUiState =
        this.copy(
            accountDetails = accountDetails,
            uidCandidateList = if (accountDetails.uid != this.accountDetails.uid) {
                getUidCandidateList(accountDetails.uid)
            } else {
                this.uidCandidateList
            },
            isValid = doValidate(accountDetails)
        )

    fun updateUsedUidList(usedUidList: List<String>): AccountEntryUiState =
        this.copy(usedUidList = usedUidList)

    fun chooseUid(uid: String): AccountEntryUiState =
        updateAccountDetails(accountDetails = this.accountDetails.copy(uid = uid))
            .clearUidCandidateList()


    fun clearUidCandidateList(): AccountEntryUiState = this.copy(uidCandidateList = listOf())

    fun updateGeneratingPasswd(): AccountEntryUiState =
        updateAccountDetails(
            accountDetails = this.accountDetails.copy(plainPasswd = generatePasswd())
        )


    fun updateReadingStatus(status: Boolean): AccountEntryUiState =
        this.copy(isReadingLocalDeskApp = status)

    private fun doValidate(accountDetails: AccountDetails): Boolean = with(accountDetails) {
        // 输入的appName和plainPasswd不可为空
        appName.isNotBlank() && plainPasswd.isNotBlank()
    }

    private fun getUidCandidateList(currentInputUid: String): List<String> {
        val uidCandidateArrayList = arrayListOf<String>()

        this.usedUidList.forEach {
            if (it.isNotEmpty() && it.contains(currentInputUid)) {
                uidCandidateArrayList.add(it)
            }
        }

        return if (uidCandidateArrayList.size > 3) {
            uidCandidateArrayList.slice(0..2).toList()
        } else {
            uidCandidateArrayList.toList()
        }
    }
}

fun AccountDetails.toAccountEntryUiState(isValid: Boolean): AccountEntryUiState = AccountEntryUiState(
    accountDetails = this,
    isReadingLocalDeskApp = false,
    isValid = isValid
)

data class AppChooseUiState(
    val localDeskAppList: List<LocalDeskApp> = listOf(),
    val targetAppList: List<LocalDeskApp> = listOf(),
    val targetName: String = "",
    val isReadingLocalDeskApp: Boolean = true
) {
    fun updateLocalDeskAppList(localDeskAppList: List<LocalDeskApp>): AppChooseUiState =
        this.copy(
            localDeskAppList = localDeskAppList,
            targetAppList = doSearch(localDeskAppList, this.targetName)
        )

    fun updateReadingStatus(status: Boolean): AppChooseUiState =
        this.copy(isReadingLocalDeskApp = status)

    fun updateSearchWord(searchWord: String): AppChooseUiState =
        this.copy(
            targetName = searchWord,
            targetAppList = doSearch(this.localDeskAppList, searchWord)
        )

    private fun doSearch(localDeskAppList: List<LocalDeskApp>, searchWord: String): List<LocalDeskApp> {
        val targetAppArrayList = arrayListOf<LocalDeskApp>()

        for (localDeskApp in localDeskAppList) {
            if (localDeskApp.appName.lowercase().contains(searchWord.lowercase()) || localDeskApp.packageName.lowercase().contains(searchWord.lowercase())) {
                targetAppArrayList.add(localDeskApp)
            }
        }

        return targetAppArrayList.toList()
    }
}

data class LocalDeskApp (
    val appName: String,
    val packageName: String,
    val appIcon: ImageBitmap
)
