package com.example.periodcycle.database

import android.provider.SyncStateContract.Helpers.insert
import androidx.lifecycle.LiveData
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDateDao {
    @Insert
    suspend fun addAHistory(HistoryEntity: HistoryDate)
    @Query("Select * from `history-table`")
    fun getAllHistory(): Flow<List<HistoryDate>>

    @Query("DELETE FROM 'history-table' WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("Select * from `history-table` where id=:id")
    fun getAHistoryById(id:Long): Flow<HistoryDate>
}

@Dao
interface UserDao {
    @Insert
    suspend fun addAUser(UserEntity: UserData)

    @Query("Select * from `user-table` ")
    fun getUser(): Flow<List<UserData>>

    @Update
    suspend fun update (UserEntity: UserData)

    @Query("DELETE FROM `user-table` WHERE UserId = :id")
    suspend fun deleteById(id: Int)

    @Query("Select * from `user-table` where UserId=:id")
    fun getAUserById(id:Int): Flow<UserData>

    @Query("UPDATE `user-table` SET name = :newName WHERE UserId = :id")
    suspend fun updateName(id: Int, newName: String): Int
}