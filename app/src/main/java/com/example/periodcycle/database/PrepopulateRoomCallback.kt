package com.example.periodcycle.database

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.periodcycle.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray

class PrepopulateRoomCallback(private val context: Context) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        Log.d("User App", "onCreate called — Databasssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssase is being created")
        CoroutineScope(Dispatchers.IO).launch {
            prePopulateUsers(context)
        }
    }
}

suspend fun prePopulateUsers(context: Context) {
    try {
        val userDao = UserDatabase.getDatabase(context).userDao
        val userList: JSONArray =
            context.resources.openRawResource(R.raw.userprepopulate).bufferedReader().use {
                JSONArray(it.readText())
            }

        userList.takeIf { it.length() > 0 }?.let { list ->
            for (index in 0 until list.length()) {
                val userObj = list.getJSONObject(index)
                userDao.addAUser(
                    UserData(
                        userObj.getInt("UserId"),
                        userObj.getString("name"),
                        userObj.getInt("averagePeriod"),
                        userObj.getInt("averageCycle")
                    )
                )

            }
            Log.e("User App", "successfully pre-populated users into database")
        }
    } catch (exception: Exception) {
        Log.e(
            "User App",
            exception.localizedMessage ?: "failed to pre-populate users into database"
        )
    }
}
