package com.inno.common.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.inno.common.annotation.EMPLOYEE
import com.inno.common.annotation.MANAGER
import com.inno.common.annotation.SUPPORT
import com.inno.common.annotation.UserRole

@Entity(tableName = "users_table")
data class User(
    var username: String,
    var passwordHash: String,
    @UserRole var role: Int,
    var permission: Int,
    var remark: String,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)

val defaultUsers = listOf(
    User(id = 1, username = "manager", passwordHash = "1234", role = MANAGER, permission = 762,
        remark = "Manager"),
    User(id = 2, username = "employee", passwordHash = "1234", role = EMPLOYEE, permission = 746,
        remark = "Employee"),
    User(id = 3, username = "support", passwordHash = "1234", role = SUPPORT, permission = 762,
        remark = "Support")
)

// role id | role name
//    1        manager
//    2        employee
//    3        after-sales support

// permission id |  action
//   1               view statistic
//   2               access formula
//   3               display
//   4               machine parts setting
//   5               drinks working setting
//   6               vat/grind setting
//   7               wash machine
//   8               permission
//   9               maintenance
//   10              state
//   11              machine test
