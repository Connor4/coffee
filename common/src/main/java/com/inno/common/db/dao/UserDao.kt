package com.inno.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.inno.common.db.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users_table WHERE username = :username LIMIT 1")
    suspend fun getUserByUserName(username: String): User?

    @Update
    suspend fun updateUser(user: User): Int

    @Query("DELETE FROM users_table WHERE id = :id")
    suspend fun deleteUser(id: Int): Int

    @Query("UPDATE users_table SET role = :roleId WHERE id = :id")
    suspend fun updateUserRoleId(id: Int, roleId: Long)

    @Query("UPDATE users_table SET permission = :permissionId WHERE id = :id")
    suspend fun updateUserPermissionId(id: Int, permissionId: Int)

    @Query("UPDATE users_table SET passwordHash = :password WHERE username = :username")
    suspend fun updateUserPassword(username: String, password: String)

    @Query("SELECT * FROM users_table ORDER BY id ASC")
    fun getAllUserFlow(): Flow<List<User>>

    @Query("SELECT * FROM users_table ORDER BY id ASC")
    fun getAllUser(): List<User>

}