package com.inno.coffee.ui.firstinstall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.inno.coffee.ui.home.launchMakeCoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import com.inno.common.utils.CoffeeDataStore
import com.inno.common.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.util.Calendar

/**
 * 首次安装页面
 */
class InstallSettingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val firstInstall = runBlocking {
            CoffeeDataStore.getFirstInstall(this@InstallSettingActivity)
        }

        if (firstInstall) {
            setContent {
                CoffeeTheme {
                    InstallSetting { language, date, hour, min ->
                        launchMakeCoffeeActivity(this)
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                CoffeeDataStore.saveFirstInstall(this@InstallSettingActivity)
                                CoffeeDataStore.saveMachineLanguage(this@InstallSettingActivity,
                                    language)
                                val calendar = Calendar.getInstance().apply {
                                    timeInMillis = date
                                    set(Calendar.HOUR_OF_DAY, hour)
                                    set(Calendar.MINUTE, min)
                                    set(Calendar.SECOND, 0)
                                    set(Calendar.MILLISECOND, 0)
                                }
                                val timeInMillis = calendar.timeInMillis
//                        val dateFormat =
//                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//                        val dateString = dateFormat.format(timeInMillis)
                                setSystemTimeRoot(timeInMillis)
                            }
                        }
                    }
                }
            }
        } else {
            launchMakeCoffeeActivity(this)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    private fun setSystemTimeRoot(timeInMillis: Long) {
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