package com.beginner.githubuser.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = TABLE_NAME)
@Parcelize
data class User(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val login: String? = null,
    val name: String? = null,
    val avatar_url: String? = null,
    val public_repos: Int = 0,
    val company: String? = null,
    val location: String? = null,
    val followers: Int = 0,
    val following: Int = 0
) : Parcelable

const val TABLE_NAME = "users_favorite"
