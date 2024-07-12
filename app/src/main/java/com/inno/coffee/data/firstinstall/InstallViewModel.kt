package com.inno.coffee.data.firstinstall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import com.inno.coffee.utilities.CoffeeDataStore
import com.inno.common.db.entity.defaultUsers
import com.inno.common.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class InstallViewModel @Inject constructor(
    private val repository: InstallRepository,
    private val dataStore: CoffeeDataStore,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    fun insertDefaultUsers() {
        viewModelScope.launch {
            defaultUsers.forEach {
                repository.registerUser(it.username, it.passwordHash, it.role, it.permission,
                    it.remark)
            }
        }
    }

    fun finishSetting(language: String, date: Long, hour: Int, min: Int) {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                dataStore.saveMachineLanguage(language)

                val calendar = Calendar.getInstance().apply {
                    timeInMillis = date
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, min)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                val timeInMillis = calendar.timeInMillis

                try {
                    val process = Runtime.getRuntime().exec("su")
                    val outputStream = DataOutputStream(process.outputStream)
                    outputStream.writeBytes("date -s @${timeInMillis / 1000}\n")
                    outputStream.writeBytes("exit\n")
                    outputStream.flush()
                    process.waitFor()
                    Logger.d("SetTime", "System time set to $timeInMillis using root")
                } catch (e: Exception) {
                    e.printStackTrace()
                    Logger.e("SetTime", "Failed to set system time with root: ${e.message}")
                }
            }
        }
    }

}