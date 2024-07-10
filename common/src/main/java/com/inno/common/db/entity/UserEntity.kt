package com.inno.common.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.inno.common.annotation.UserModule
import com.inno.common.annotation.UserRole

@Entity(tableName = "users_table")
data class User(
    var username: String,
    var passwordHash: String,
    @UserRole var role: Int,
    @UserModule var permission: Int,
    var remark: String,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
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
//   9               maintainance
//   10              state
//   11              machine test
