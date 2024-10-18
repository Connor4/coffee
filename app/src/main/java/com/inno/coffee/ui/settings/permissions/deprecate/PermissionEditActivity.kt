package com.inno.coffee.ui.settings.permissions.deprecate

import android.os.Bundle
import androidx.activity.compose.setContent
import com.inno.coffee.ui.base.CoffeeActivity
import com.inno.coffee.ui.theme.CoffeeTheme
import com.inno.coffee.utilities.PERMISSION_KEY_USERNAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionEditActivity : CoffeeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = intent.extras?.getString(PERMISSION_KEY_USERNAME) ?: ""
        setContent {
            CoffeeTheme {
                PermissionEditLayout(
                    modifyUsername = username,
                    onCloseClick = {
                        this@PermissionEditActivity.finish()
                    }
                )
            }
        }
    }
}