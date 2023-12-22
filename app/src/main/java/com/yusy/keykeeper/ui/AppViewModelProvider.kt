package com.yusy.keykeeper.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yusy.keykeeper.KeyKeeperApplication
import com.yusy.keykeeper.ui.pages.account.AccountEntryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // initializer for AccountEntryViewModel
        initializer {
            AccountEntryViewModel(keyKeeperApplication().container.accountsRepository)
        }
    }
}

fun CreationExtras.keyKeeperApplication(): KeyKeeperApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as KeyKeeperApplication)
