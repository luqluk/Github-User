package com.beginner.githubuser.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.beginner.githubuser.db.UserDatabase
import com.beginner.githubuser.db.UserFavoriteRepositories
import com.beginner.githubuser.model.User

class FavoriteViewModel(app: Application) : AndroidViewModel(app) {
    val listFavorite: LiveData<List<User>>

    init {
        val userDao = UserDatabase.getDatabase(app).userDao()
        listFavorite = UserFavoriteRepositories.getFavorite(userDao)
    }
}