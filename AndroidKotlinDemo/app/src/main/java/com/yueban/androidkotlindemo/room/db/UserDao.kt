package com.yueban.androidkotlindemo.room.db

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

/**
 * @author yueban
 * @date 2018/12/6
 * @email fbzhh007@gmail.com
 */
@Dao
interface UserDao {
    @Query("select * from user")
    fun _getAll(): List<User>

    @Query("select * from user")
    fun getAllByCursor(): Cursor

    @Query("select * from user")
    fun getAll(): LiveData<List<User>>

    @Query("select * from user where id in (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("select * from user where firstName like :name or lastName like :name limit 1")
    fun findByName(name: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Insert
    fun insertAllWithResult(vararg users: User): List<Long>

    @Insert
    fun insert(user: User)

    /**
     * @return row_id
     */
    @Insert
    fun insertWithResult(user: User): Long

    @Update
    fun update(user: User)

    @Update
    fun updateAll(vararg users: User)

    /**
     * @return number of rows updated in database
     */
    @Update
    fun updateAllWithResult(vararg users: User): Int

    @Delete
    fun delete(user: User)

    @Delete
    fun deleteAll(vararg user: User)

    /**
     * @return number of rows removed from database
     */
    @Delete
    fun deleteAllWithResult(vararg user: User): Int

    @Query("select firstName, lastName from user")
    fun loadFullName(): List<NameTuple>

    @Query("select * from user where birthday between :from and :to")
    fun findUsersBornBetweenDates(from: Date, to: Date): List<User>
}