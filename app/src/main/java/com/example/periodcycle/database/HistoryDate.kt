package com.example.periodcycle.database

import android.graphics.Bitmap
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateConverter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it, formatter) }
    }
}

@Entity(tableName = "history-table")
data class HistoryDate(
    @PrimaryKey (autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "datestart") val startDate: LocalDate,
    @ColumnInfo(name = "dateend") val endDate: LocalDate,
)

@Entity(tableName = "user-table")
data class UserData(
    @PrimaryKey @NonNull @ColumnInfo (name = "UserId")
    val UserId: Int = 0,
    @ColumnInfo (name = "name") val name: String,
    @ColumnInfo (name = "averagePeriod") val averagePeriod: Int,
    @ColumnInfo (name = "averageCycle")val averageCycle: Int,
    @ColumnInfo (name = "profilepicture")val profilePicture: String? = null,
    )


@Entity(tableName = "user-history-table")
data class UserHistory(
    @PrimaryKey (autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "weight") val weight: Int,
    @ColumnInfo(name = "mood") val mood: String,
    @ColumnInfo(name = "water") val water: Int,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = "bloodFlow") val bloodFlow: Int,
    )