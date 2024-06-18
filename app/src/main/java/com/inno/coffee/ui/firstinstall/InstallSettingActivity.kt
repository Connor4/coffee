package com.inno.coffee.ui.firstinstall

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.inno.coffee.ui.home.launchMakeCoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import com.inno.common.utils.readFirstInstall
import com.inno.common.utils.saveFirstInstall
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 首次安装页面
 */
class InstallSettingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val firstInstall = runBlocking {
            this@InstallSettingActivity.readFirstInstall()
        }

        if (firstInstall) {
            setContent {
                CoffeeTheme {
                    InstallSetting {
                        lifecycleScope.launch {
                            this@InstallSettingActivity.saveFirstInstall(false)
                        }
                        launchMakeCoffeeActivity(this)
                    }
                }
            }
        } else {
            launchMakeCoffeeActivity(this)
        }
        requestPermissions()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    //=================================start 调试使用 start==============================================
    private val REQUEST_CODE_PERMISSIONS = 1001

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSIONS)
        }
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
//        grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }
//===================================end 调试使用 end ===============================================

}