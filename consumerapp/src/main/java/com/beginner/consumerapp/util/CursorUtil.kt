package com.beginner.consumerapp.util

import android.database.Cursor
import com.beginner.consumerapp.data.User

fun Cursor.toListUserFavorite() : ArrayList<User> {
    val userFavoriteList = ArrayList<User>()

    apply {
        while (moveToNext()) {
            userFavoriteList.add(
                toUserFavoriteEntity()
            )
        }
    }

    return userFavoriteList
}

fun Cursor.toUserFavoriteEntity() : User =
    User(
        getString(getColumnIndexOrThrow("login")),
        getString(getColumnIndexOrThrow("avatar_url")),
        getString(getColumnIndexOrThrow("location")),
    )