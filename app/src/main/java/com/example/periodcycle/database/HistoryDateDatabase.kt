package com.example.periodcycle.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.periodcycle.database.HistoryDate
import com.example.periodcycle.database.HistoryDateDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [HistoryDate::class],
    version = 1)
@TypeConverters(LocalDateConverter::class)
abstract class HistoryDateDatabase : RoomDatabase() {
    abstract fun historyDateDao(): HistoryDateDao

    companion object {
        private var INSTANCE: HistoryDateDatabase? = null

        fun getDatabase(context: Context): HistoryDateDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryDateDatabase::class.java,
                    "dates_db"
                )
                    .build()
            }
            return INSTANCE!!
        }

    }
}

@Database(
    entities = [UserData::class],
    version = 11,
    exportSchema = true)
abstract class UserDatabase : RoomDatabase() {
    abstract val userDao: UserDao

    companion object {
        private var DB_INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            synchronized(this) {
                var instance = DB_INSTANCE
                if (instance == null) {
                    DB_INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "user_db"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(PrepopulateRoomCallback(context))
                        .build()
                    instance = DB_INSTANCE
                }
                return instance!!
            }

        }
    }
}


//@Database(
//    entities = [UserData::class],
//    version = 1)
//abstract class UserDatabase : RoomDatabase() {
//    abstract fun userDao(): UserDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: UserDatabase? = null
//
//        fun getInstance(context: Context): UserDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    UserDatabase::class.java,
//                    "user-database"
//                ).addCallback(object : Callback() {
//                    override fun onCreate(db: SupportSQLiteDatabase) {
//                        super.onCreate(db)
//                        CoroutineScope(Dispatchers.IO).launch {
//                            getInstance(context).userDao().saveUser(UserData())
//                        }
//                    }
//                })
//                    .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}
