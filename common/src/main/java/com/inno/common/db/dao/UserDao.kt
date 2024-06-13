package com.inno.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.inno.common.db.entity.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users_table WHERE username = :username LIMIT 1")
    suspend fun getUserByUserName(username: String): User?

    @Update
    suspend fun updateUserPwdHash(user: User)

    @Query("DELETE FROM users_table WHERE id = :id")
    suspend fun deleteUser(id: Int)

    @Query("UPDATE users_table SET roleId = :roleId WHERE id = :id")
    suspend fun updateUserRoleId(id: Int, roleId: Long)

    @Query("UPDATE users_table SET permissionId = :permissionId WHERE id = :id")
    suspend fun updateUserPermissionId(id: Int, permissionId: Int)

}