package com.yueban.androidkotlindemo.ui.room.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * @author yueban
 * @date 2018/12/6
 * @email fbzhh007@gmail.com
 */
@Entity
data class User(
    @PrimaryKey var id: Int,
    var firstName: String?,
    var lastName: String?,
    var birthday: Date?
)