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
import java.time.LocalDate

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

    @Query("Update `user-table` SET averagePeriod = :newPeriod WHERE UserId = :id")
    suspend fun updatePeriod(id: Int, newPeriod :Int):Int

    @Query("Update `user-table` SET averageCycle = :newCycle WHERE UserId = :id")
    suspend fun updateCycle(id: Int, newCycle :Int):Int

    @Query("Update `user-table` SET profilePicture = :picture WHERE UserId = :id")
    suspend fun updatePicture(id: Int, picture :String):Int
}

@Dao
interface UserHistoryDao {
    @Insert
    suspend fun addAUserHistory(UserHistoryEntity: UserHistory)

    @Query("Select * from `user-history-table` ORDER BY id ASC")
    fun getAllUserHistory(): Flow<List<UserHistory>>

    @Query("DELETE FROM 'user-history-table' WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM `user-history-table` WHERE date = :date")
    fun getAUserHistoryByDate(date: LocalDate): Flow<UserHistory>

    @Query("Update `user-history-table` SET weight = :newWeight WHERE date = :date")
    suspend fun updateWeight(date: LocalDate, newWeight :Int) :Int

    @Query("Update `user-history-table` SET mood = :newMood WHERE date = :date")
    suspend fun updateMood(date: LocalDate, newMood :String) :Int

    @Query("Update `user-history-table` SET water = :newWater WHERE date = :date")
    suspend fun updateWater(date: LocalDate, newWater :Int) :Int

}