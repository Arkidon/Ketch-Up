package com.ketchup.app.database
import androidx.room.*


@Entity(tableName = Users.TABLE_NAME)
data class Users(

    @PrimaryKey @ColumnInfo(name = "Username") val username: String,
    @ColumnInfo(name = "User_id") val id : Int,
    @ColumnInfo(name = "Password") val password: String

){
    companion object{
        const val TABLE_NAME = "Users"
    }
}
@Dao
interface UserDao{

    @Insert
     fun insert(users: Users)

    @Update
     fun update(vararg users: Users)

    @Delete
   fun delete(vararg users: Users)

    @Query("SELECT * FROM " + Users.TABLE_NAME )
     fun getAllUsers(): List<Users>

    @Query("SELECT * FROM " + Users.TABLE_NAME + " WHERE Username = :username")
    fun findByName(username: String): Users
}