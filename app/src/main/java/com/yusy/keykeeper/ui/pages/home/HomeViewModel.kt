package com.yusy.keykeeper.ui.pages.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusy.keykeeper.data.account.AccountsRepository
import com.yusy.keykeeper.ui.components.accountcard.AccountPreview
import com.yusy.keykeeper.ui.components.accountcard.toAccountPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeViewModel(accountsRepository: AccountsRepository): ViewModel() {
    var homeUiState by mutableStateOf(
        HomeUiState(
            accountPreviewListFlow = accountsRepository.getAllAccountsStream().map { accountList ->
                accountList.map { account ->
                    account.toAccountPreview()
                }
            }
        )
    )

    init {
        viewModelScope.launch {
            updateAccountPreviewList()
        }
    }
}

suspend fun HomeViewModel.updateAccountPreviewList() {
    homeUiState = homeUiState.updateAccountPreviewList()
}

fun HomeViewModel.updateSearchWord(searchWord: String) {
    homeUiState = homeUiState.updateSearchWord(searchWord)
}

fun HomeViewModel.updateSortType(sortType: SortType) {
    homeUiState = homeUiState.updateSortType(sortType)
}

data class HomeUiState(
    var accountPreviewListFlow: Flow<List<AccountPreview>>,
    var accountPreviewList: List<AccountPreview> = listOf(),
    var searchWord: String = "",
    val sortTypeList: List<SortType> = listOf(SortType.ByNameASC, SortType.ByNameDESC, SortType.ByCreatedTimeASC, SortType.ByCreatedTimeDESC),
    val currentSortType: SortType = SortType.ByCreatedTimeDESC,
    var resultAccountPreviewList: List<AccountPreview> = listOf(),
) {
    suspend fun updateAccountPreviewList(): HomeUiState {
        val accountPreviewList = this.accountPreviewListFlow.first()
        return this.copy(
            accountPreviewList = accountPreviewList,
            searchWord = "",
            resultAccountPreviewList = accountPreviewList
        )
    }

    fun updateSearchWord(searchWord: String): HomeUiState =
        this.copy(
            searchWord = searchWord,
            resultAccountPreviewList = doSearch(this.accountPreviewList, searchWord)
        )

    fun updateSortType(sortType: SortType): HomeUiState =
        this.copy(
            currentSortType = sortType,
            resultAccountPreviewList = doSort(sortType)
        )

    private fun doSearch(accountPreviewList: List<AccountPreview>, targetName: String): List<AccountPreview> {
        if (targetName.isEmpty()) {
            return accountPreviewList
        }

        val results = arrayListOf<AccountPreview>()

        accountPreviewList.forEach {
            Log.i("search", "accountPreviewList: " + it.appName)
            if (it.appName.lowercase().contains(targetName.lowercase()) || it.appUrl.lowercase().contains(targetName.lowercase())) {
                results.add(it)
            }
        }

        return results.toList()
    }

    private fun doSort(sortType: SortType): List<AccountPreview> =
        when(sortType) {
            SortType.ByNameASC ->
                this.resultAccountPreviewList.sortedBy { it.appName }
            SortType.ByNameDESC ->
                this.resultAccountPreviewList.sortedByDescending { it.appName }
            SortType.ByCreatedTimeASC ->
                this.resultAccountPreviewList.sortedBy { it.createdAt }
            SortType.ByCreatedTimeDESC ->
                this.resultAccountPreviewList.sortedByDescending { it.createdAt }
            else ->
                this.resultAccountPreviewList
        }
}

enum class SortType(val text: String) {
    ByNameASC("根据名称排序(递增)"),
    ByNameDESC("根据名称排序(递增)"),
    ByCreatedTimeASC("根据创建时间排序(递增)"),
    ByCreatedTimeDESC("根据创建时间排序(递减)")
}