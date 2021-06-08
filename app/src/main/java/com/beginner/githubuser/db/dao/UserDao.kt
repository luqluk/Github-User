package com.beginner.githubuser.db.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.beginner.githubuser.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    @Query("SELECT * from users_favorite ORDER BY login ASC")
    fun getUserList(): LiveData<List<User>>

    @Query("SELECT * from users_favorite WHERE login = :username")
    fun getUserDetail(username: String): User?

    @Query("SELECT * from users_favorite ORDER BY login ASC")
    fun getListProvider(): Cursor

    @Query("SELECT * from users_favorite ORDER BY login ASC")
    fun getWidgetFavorite(): List<User>

    @Delete
    suspend fun deleteUser(user: User): Int
}