package com.beginner.githubuser.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.beginner.githubuser.db.dao.UserDao
import com.beginner.githubuser.model.User
import kotlinx.coroutines.Dispatchers

class UserFavoriteRepositories(private val userDao: UserDao) {

    private val favorite: MutableLiveData<Boolean> = MutableLiveData()
    val allUsers: LiveData<List<User>> = userDao.getUserList()


    fun getUser(username: String) = liveData(Dispatchers.IO) {
        val user = userDao.getUserDetail(username)
        if (user != null) {
            favorite.postValue(true)
        } else {
            favorite.postValue(false)
            emitSource(favorite)
        }
    }

    suspend fun insert(user: User) {
        userDao.insertUser(user)
        favorite.value = true
    }

    suspend fun delete(user: User) {
        userDao.deleteUser(user)
        favorite.value = false
    }

    val isFavorite: LiveData<Boolean> = favorite

    companion object {
        fun getFavorite(userDao: UserDao) = userDao.getUserList()
    }
}