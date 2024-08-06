package com.inno.coffee.function.presentation

import android.app.Presentation
import android.content.Context
import android.os.Bundle
import android.view.Display
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

class DisplayPresentation(context: Context, display: Display,
    private val content: @Composable () -> Unit) : Presentation(context, display) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val composeView = ComposeView(context)
        composeView.setContent {
            content()
        }
        val viewModelStore = ViewModelStore()
        val viewModelStoreOwner = object : ViewModelStoreOwner {
            override val viewModelStore: ViewModelStore
                get() = viewModelStore
        }
        val lifecycleOwner = DisplayLifeCycleOwner()
        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        composeView.setViewTreeLifecycleOwner(lifecycleOwner)
        composeView.setViewTreeViewModelStoreOwner(viewModelStoreOwner)
        composeView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)
        setContentView(composeView)
    }

}