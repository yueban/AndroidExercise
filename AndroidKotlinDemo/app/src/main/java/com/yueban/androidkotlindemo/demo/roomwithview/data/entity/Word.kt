package com.yueban.androidkotlindemo.demo.roomwithview.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author yueban
 * @date 2018/12/9
 * @email fbzhh007@gmail.com
 */
@Entity
data class Word(
    @PrimaryKey val word: String
)