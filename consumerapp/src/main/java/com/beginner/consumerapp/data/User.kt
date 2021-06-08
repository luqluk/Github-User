package com.beginner.consumerapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User(
    val login: String? = null,
    val avatar_url: String? = null,
    val location: String? = null,
) : Parcelable
