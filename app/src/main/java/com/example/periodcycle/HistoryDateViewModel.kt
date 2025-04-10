package com.example.periodcycle
import android.app.Application
import androidx.compose.runtime.mutableStateOf
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

    fun GetName(id: Int) : String{
        return dao.getNameById(id = id)
    }

    fun GetPeriod(id: Int): Int{
        return dao.getPeriodById(id = id)
    }

    fun GetCycle(id: Int): Int{
        return dao.getCycleById(id = id)
    }
}

