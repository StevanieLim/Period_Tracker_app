package com.example.periodcycle
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.periodcycle.database.HistoryDate
import com.example.periodcycle.database.HistoryDateDatabase
import com.example.periodcycle.database.UserData
import com.example.periodcycle.database.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.periodcycle.database.SharedPreferences
import com.example.periodcycle.database.SharedPreferences2
import com.example.periodcycle.database.UserHistory
import com.example.periodcycle.database.UserHistoryDao
import com.example.periodcycle.database.UserHistoryDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class HistoryDateViewModel(application: Application) : AndroidViewModel(application) {
    private val db = HistoryDateDatabase.getDatabase(application)
    private val dao = db.historyDateDao()

    val allDates: Flow<List<HistoryDate>>  = dao.getAllHistory()

    fun saveDate(date: LocalDate, date2:LocalDate) {
        viewModelScope.launch {
            dao.addAHistory(HistoryDate(startDate = date, endDate = date2))
        }
    }

    fun deleteDate(id : Long) {
        viewModelScope.launch {
            dao.deleteById(id = id)
        }
    }
}

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val db = UserDatabase.getDatabase(application)
    private val dao = db.userDao

    val allUser: Flow<List<UserData>>  = dao.getUser()  // Flow automatically updates UI

    fun saveUser(id : Int, name : String, period : Int, Cycle : Int) {
        viewModelScope.launch {
            dao.addAUser(UserData(UserId = id, name = name, averagePeriod = period, averageCycle = Cycle))
        }
    }

    fun UpdateUser(id:Int, name : String, period : Int, Cycle : Int) {
        viewModelScope.launch {
            dao.update(UserData(UserId = id, name = name, averagePeriod = period, averageCycle = Cycle))
        }
    }

    fun UpdateName(id:Int, newName: String){
        viewModelScope.launch {
            dao.updateName(id = id , newName = newName )
        }
    }

    fun UpdataPeriod(id: Int, newPeriod: Int){
        viewModelScope.launch {
            dao.updatePeriod(id = id, newPeriod= newPeriod)
        }
    }

    fun UpdataCycle(id: Int, newCycle: Int){
        viewModelScope.launch {
            dao.updateCycle(id = id, newCycle = newCycle)
        }
    }

    fun saveUserOnce(context: Context) {
        if (!SharedPreferences.isUserSaved(context)) {
            viewModelScope.launch {
                saveUser(1, "user", 7, 30)  // Call suspend function within a coroutine
            }
            SharedPreferences.setUserSaved(context, true)
        }
    }

//    suspend fun haveSaveUser(id: Int, name: String, cycle: Int, period: Int) {
//        val user = UserData(UserId = id, name = name, averageCycle = cycle, averagePeriod = period)
//        dao.addAUser(user)
//    }
}

class UserHistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val db = UserHistoryDatabase.getDatabase(application)
    private val dao = db.userHistoryDao()

    val allUserHistory: Flow<List<UserHistory>>  = dao.getAllUserHistory()

    fun saveInfo(weight: Int, mood: String, water : Int) {
        viewModelScope.launch {
            dao.addAUserHistory(UserHistory(weight = weight, mood = mood, water = water, date = LocalDate.now()))
        }
    }




    private val _userHistory = MutableStateFlow<UserHistory?>(null)
    val userHistory: StateFlow<UserHistory?> = _userHistory.asStateFlow()

    fun getUserHistory() {
        viewModelScope.launch {
            dao.getAUserHistoryByDate(date = LocalDate.now()).collect { history ->
                _userHistory.value = history
            }
        }
    }


    fun GetUserHistory(){
        viewModelScope.launch {
            dao.getAUserHistoryByDate(date = LocalDate.now())
        }
    }

    fun UpdataWeight(newWeight: Int){
        viewModelScope.launch {
            dao.updateWeight(newWeight = newWeight, date = LocalDate.now())
        }
    }

    fun UpdataMood(newMood: String){
        viewModelScope.launch {
            dao.updateMood(newMood = newMood, date = LocalDate.now())
        }
    }

    fun UpdataWater(newWater: Int){
        viewModelScope.launch {
            dao.updateWater(newWater = newWater, date = LocalDate.now())
        }
    }

    fun deleteDate(id : Long) {
        viewModelScope.launch {
            dao.deleteById(id = id)
        }
    }

    fun saveUserHistoryOnce(context: Context) {
        if (!SharedPreferences2.isUserSaved(context)) {
            Log.d("SAVE_CHECK", "Saving user history...")
            viewModelScope.launch {
                try {
//                    haveSaveUser(50, "Happy", 3)
                    saveInfo(50,"happy",3)
                    Log.d("SAVE_CHECK", "Data saved successfully!")
                    SharedPreferences2.setUserSaved(context, true)
                } catch (e: Exception) {
                    Log.e("SAVE_CHECK", "Failed to save data: ${e.message}")
                }
            }
        } else {
            Log.d("SAVE_CHECK", "User already saved.")
        }
    }
//check by add yesterday date
//    suspend fun haveSaveUser(weight: Int, mood: String, water: Int) {
//        val user = UserHistory(weight = weight, mood = mood, water=water, date = LocalDate.now().minusDays(1))
//        dao.addAUserHistory(user)
//        _userHistory.value = user
//        Log.d("SAVE_CHECK", "User inserted: $user")
//    }
}
