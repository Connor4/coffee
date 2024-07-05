package com.inno.coffee.data.settings.permissions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inno.common.db.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
) : ViewModel() {
    private val _registerResult = MutableStateFlow<Boolean?>(false)
    val registerResult: StateFlow<Boolean?> = _registerResult
    private val _authenticateResult = MutableStateFlow<Boolean?>(false)
    val authenticateResult = _authenticateResult
    private val _updateResult = MutableStateFlow<Boolean?>(false)
    val updateResult = _updateResult
    private val _deleteResult = MutableStateFlow<Boolean?>(false)
    val deleteResult = _deleteResult

    val userList: StateFlow<List<User>> = repository.getAllUser().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    fun registerUser(username: String, password: String, roleId: Int, permissionId: Int) {
        viewModelScope.launch {
            val register = repository.registerUser(username, password, roleId, permissionId)
            _registerResult.value = register
        }
    }

    fun authenticateUser(username: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isAuthenticated = repository.authenticateUser(username, password)
            _authenticateResult.value = isAuthenticated
            onResult(isAuthenticated)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            val update = repository.updateUser(user)
            _updateResult.value = update
        }
    }

    // TODO need check role first
    fun deleteUser(user: User) {
        viewModelScope.launch {
            val deleteUser = repository.deleteUser(user)
            _deleteResult.value = deleteUser
        }
    }

}