package com.beginner.githubuser.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.beginner.githubuser.db.UserDatabase
import com.beginner.githubuser.db.UserFavoriteRepositories
import com.beginner.githubuser.model.User
import kotlinx.coroutines.launch

class DetailViewModel(app: Application) : AndroidViewModel(app) {
    private val repository: UserFavoriteRepositories
    private val allUsers: LiveData<List<User>>

    init {
        val userDao = UserDatabase.getDatabase(app).userDao()
        repository = UserFavoriteRepositories(userDao)
        allUsers = repository.allUsers
    }

    fun data(username: String): LiveData<Boolean> = repository.getUser(username)

    fun addFavorite(user: User) = viewModelScope.launch {
        repository.insert(user)
    }

    fun removeFavorite(
        user: User
    ) = viewModelScope.launch {
        repository.delete(user)
    }

    val isFavorite: LiveData<Boolean> = repository.isFavorite
}