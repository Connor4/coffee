package com.inno.coffee.ui.settings.permissions

import android.os.Bundle
import androidx.activity.compose.setContent
import com.inno.coffee.ui.base.CoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionActivity : CoffeeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeTheme {
//                PermissionLayout() {
//                    this@PermissionActivity.finish()
//                }
//                PermissionEditLayout() {
//                    this@PermissionActivity.finish()
//                }
                PermissionUserListLayout() {
                    this@PermissionActivity.finish()
                }
            }
        }
    }
}