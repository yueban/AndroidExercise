package com.yueban.androidkotlindemo.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yueban.androidkotlindemo.ui.room.db.User

/**
 * @author yueban
 * @date 2018/12/8
 * @email fbzhh007@gmail.com
 */
class ViewModel1(application: Application) : AndroidViewModel(application) {
    private lateinit var users: MutableLiveData<List<User>>

    fun getUsers(): LiveData<List<User>> {
        if (!::users.isInitialized) {
            users = MutableLiveData()
            loadUsers()
        }
        return users
    }

    private fun loadUsers() {
        // "this is mock data"
        val userData = ArrayList<User>()
        userData.add(User(1, "yueban", "fan", null))
        userData.add(User(2, "ranran", "xu", null))
        userData.add(User(3, "alice", "bai", null))
        users.value = userData
    }
}