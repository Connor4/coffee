package com.inno.coffee.ui.presentation

import android.app.Dialog
import android.app.Presentation
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.DialogFragment
import com.inno.common.utils.Logger

abstract class PresentationFragment(private val context: Context) : DialogFragment() {
    private var presentation: Presentation? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Logger.d("presentation == null: $presentation")
        return if (presentation == null) {
            super.onCreateDialog(savedInstanceState)
        } else {
            presentation!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d("onCreate() called with: savedInstanceState = $savedInstanceState")
        setDisplay()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                InflateCompose()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        Logger.d("onDetach() called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Logger.d("onDestroyView() called")
    }

    @Composable
    abstract fun InflateCompose()

    private fun setDisplay() {
        val display = PresentationDisplayManager.getSecondDisplay()
        display?.let {
            presentation = Presentation(context, it)
        }
    }

}