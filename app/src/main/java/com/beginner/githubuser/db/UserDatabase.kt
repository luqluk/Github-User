package com.beginner.githubuser.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.beginner.githubuser.db.dao.UserDao
import com.beginner.githubuser.model.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            val mInstance = INSTANCE
            if (mInstance != null)
                return mInstance

            synchronized(this) {
                val builder = Room.databaseBuilder(
                    context.applicationContext, UserDatabase::class.java, "database_user"
                )
                    .build()
                INSTANCE = builder
                return builder
            }
        }
    }
}