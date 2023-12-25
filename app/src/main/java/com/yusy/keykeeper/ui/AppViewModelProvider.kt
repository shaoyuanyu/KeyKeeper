package com.yusy.keykeeper.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yusy.keykeeper.KeyKeeperApplication
import com.yusy.keykeeper.ui.pages.account.AccountEditViewModel
import com.yusy.keykeeper.ui.pages.account.AccountEntryViewModel
import com.yusy.keykeeper.ui.pages.home.HomeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // initializer for AccountEntryViewModel
        initializer {
            AccountEntryViewModel(keyKeeperApplication().container.accountsRepository)
        }

        // initializer for AccountEditViewModel
        initializer {
            AccountEditViewModel(keyKeeperApplication().container.accountsRepository)
        }

        // initializer for HomeViewModel
        initializer {
            HomeViewModel(keyKeeperApplication().container.accountsRepository)
        }
    }
}

fun CreationExtras.keyKeeperApplication(): KeyKeeperApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as KeyKeeperApplication)
