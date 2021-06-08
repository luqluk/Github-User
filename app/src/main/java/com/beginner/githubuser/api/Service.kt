package com.beginner.githubuser.api

import com.beginner.githubuser.BuildConfig
import com.beginner.githubuser.model.User
import com.beginner.githubuser.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Service {

    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    fun getSearchUser(
        @Query("q") username: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<User>

    @GET("users/{username}")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    fun getUser(
        @Path("username") username: String
    ): User

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.TOKEN}")
    fun getFollower(
        @Path("username") username: String
    ): Call<ArrayList<User>>
}