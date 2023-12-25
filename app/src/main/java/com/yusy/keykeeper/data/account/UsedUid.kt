package com.yusy.keykeeper.data.account

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UsedUid")
data class UsedUid(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    var uid: String,
    var usedTimes: Int
)