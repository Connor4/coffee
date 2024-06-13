package com.inno.coffee.data.settings.permissions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.coffee.di.DefaultDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    fun registerUser(username: String, password: String, roleId: Int, permissionId: Long) {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                repository.registerUser(username, password, roleId, permissionId)
            }
        }
    }

    fun authenticateUser(username: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isAuthenticated = repository.authenticateUser(username, password)
            onResult(isAuthenticated)
        }
    }


}