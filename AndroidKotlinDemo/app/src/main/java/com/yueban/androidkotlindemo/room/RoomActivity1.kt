package com.yueban.androidkotlindemo.room

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yueban.androidkotlindemo.R
import com.yueban.androidkotlindemo.room.db.DBHolder
import kotlinx.android.synthetic.main.activity_room_1.*

/**
 * @author yueban
 * @date 2018/12/6
 * @email fbzhh007@gmail.com
 */
class RoomActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_1)

        message.text = "RoomActivity1"

        val db = DBHolder.get(this)
//        db.userDao().insertAll()
    }
}