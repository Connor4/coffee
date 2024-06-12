package com.inno.coffee.ui.firstinstall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.inno.coffee.ui.home.launchMakeCoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme

/**
 * 首次安装页面
 */
class InstallSettingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferencesManager =
            com.inno.common.utils.PreferencesManager.getInstance(this@InstallSettingActivity)
        val firstInstall = preferencesManager.isFirstInstall
        if (true) {
            setContent {
                CoffeeTheme {
                    InstallSetting {
                        preferencesManager.isFirstInstall = false
                        launchMakeCoffeeActivity(this)
                        finish()
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

//    private val REQUEST_CODE_PERMISSIONS = 1001
//
//    // 检查并请求权限
//    private fun requestPermissions() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
//            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                REQUEST_CODE_PERMISSIONS
//            )
//        } else {
//            // 权限已授予，可以执行相应操作
//            loadFromSdCard()
//        }
//    }
//
//    // 处理权限请求结果
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_CODE_PERMISSIONS) {
//            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                // 权限授予
//                loadFromSdCard()
//            } else {
//                // 权限拒绝，处理相应逻辑
//                Toast.makeText(this, "权限被拒绝，无法读取SD卡。", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    // 示例方法，显示如何调用请求权限方法
//    private fun onSomeEventTriggered() {
//        requestPermissions()
//    }


}