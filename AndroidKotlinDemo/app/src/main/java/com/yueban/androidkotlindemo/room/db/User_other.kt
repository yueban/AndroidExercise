package com.yueban.androidkotlindemo.room.db

import android.graphics.Bitmap
import androidx.room.*

/**
 * @author yueban
 * @date 2018/12/6
 * @email fbzhh007@gmail.com
 */
/********** primaryKeys, ignore **********/
@Entity(primaryKeys = ["firstName", "lastName"], tableName = "User_new")
data class User2(
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "first_name") var firstName: String?,
    @ColumnInfo(name = "last_name") var lastName: String?,
    @Ignore var picture: Bitmap?
)


/********** ignore **********/
open class User3 {
    var picture: Bitmap? = null
}

@Entity(ignoredColumns = ["picture"])
data class RemoteUser(
    var hasVpn: Boolean
) : User3()


/********** fts **********/
@Fts4(languageId = "lid")
@Entity
data class User4(
    /* Specifying a primary key for an FTS-table-backed entity is optional, but
       if you include one, it must use this type and column name. */
    @PrimaryKey @ColumnInfo(name = "rowid") var id: Int,
    @ColumnInfo(name = "first_name") var firstName: String?,
    @ColumnInfo(name = "lid") var languageId: Int
)


/********** indices **********/
@Entity(indices = [Index(value = ["lastName", "address"])])
data class User5(
    @PrimaryKey var id: Int,
    var firstName: String?,
    var address: String?,
    var lastName: String?
)

@Entity(
    indices = [Index(
        value = ["first_name", "last_name"],
        unique = true
    )]
)
data class User6(
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "first_name") var firstName: String?,
    @ColumnInfo(name = "last_name") var lastName: String?,
    @Ignore var picture: Bitmap?
)


/********** foreignKey **********/
@Entity
data class User7(
    @PrimaryKey var id: Int,
    var firstName: String?,
    var lastName: String?
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User7::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE // delete all books on userId when User(userId) is deleted
        )]
)
data class Book(
    var bookId: Int,
    var title: String?,
    var userId: Int
)


/********** embedded **********/
data class Address(
    var street: String?,
    var state: String?,
    var city: String?,
    @ColumnInfo(name = "post_code") var postCode: Int
)

@Entity
data class User8(
    @PrimaryKey var id: Int,
    @ColumnInfo var firstName: String?,
    @Embedded var address: Address
)


/********** Database view **********/
@Entity
data class User9(
    @PrimaryKey var id: Int,
    var firstName: String?,
    var lastName: String?
)

@DatabaseView(
    "SELECT user.id, user.firstName as name FROM user"
)
data class UserDetail(
    var id: Long,
    var name: String?
)