package com.yusy.keykeeper.data.account

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UsedUidDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(usedUid: UsedUid)

    @Update
    suspend fun update(usedUid: UsedUid)

    @Delete
    suspend fun delete(usedUid: UsedUid)

    @Query("SELECT * from UsedUid ORDER BY usedTimes DESC")
    fun getAllUsedUid(): Flow<List<UsedUid>>

    @Query("SELECT * from UsedUid WHERE uid = :uid")
    fun getUsedUid(uid: String): Flow<UsedUid?>
}