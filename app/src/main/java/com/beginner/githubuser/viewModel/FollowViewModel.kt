package com.beginner.githubuser.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.beginner.githubuser.api.Client
import com.beginner.githubuser.api.Service
import com.beginner.githubuser.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {

    companion object {
        val TAG: String = MainViewModel::class.java.simpleName
    }

    private val listFollowing = MutableLiveData<ArrayList<User>>()
    private val listFollower = MutableLiveData<ArrayList<User>>()

    fun setFollowing(username: String) {
        Client.getClient().create(Service::class.java).getFollowing(username)
            .enqueue(object : Callback<ArrayList<User>> {
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    if (response.isSuccessful) {
                        val following = response.body()
                        listFollowing.postValue(following!!)
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.d(TAG, t.message.toString())
                }
            })
    }


    fun setFollower(username: String) {
        Client.getClient().create(Service::class.java).getFollower(username)
            .enqueue(object : Callback<ArrayList<User>> {

                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    if (response.isSuccessful) {
                        val follower = response.body()
                        listFollower.postValue(follower!!)
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.d(TAG, t.message.toString())
                }


            })
    }

    fun getFollowing(): LiveData<ArrayList<User>> {
        return listFollowing
    }

    fun getFollower(): LiveData<ArrayList<User>> {
        return listFollower
    }
}