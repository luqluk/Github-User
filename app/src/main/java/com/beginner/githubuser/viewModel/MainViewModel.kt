package com.beginner.githubuser.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.beginner.githubuser.api.Client
import com.beginner.githubuser.api.Service
import com.beginner.githubuser.model.User
import com.beginner.githubuser.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    companion object {
        val TAG: String = MainViewModel::class.java.simpleName
    }

    private val listUser = MutableLiveData<ArrayList<User>?>()
    var listUsers: ArrayList<User> = ArrayList()
    private val user = MutableLiveData<User?>()
    private val norResponse = MutableLiveData<String>()

    fun setUser(username: String) {

        Client.getClient().create(Service::class.java).getSearchUser(username)
            .enqueue(object : Callback<UserResponse> {

                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        val items = response.body()?.items
                        if (items != null) {
                            if (items.size == 0) {
                                listUser.postValue(items)
                            } else {
                                for (users in items) {
                                    val login = users.login
                                    setDetail(login.toString())
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d(TAG, t.message.toString())
                    norResponse.value = t.message.toString()
                }
            })
    }

    fun setDetail(username: String) {
        Client.getClient().create(Service::class.java).getDetailUser(username)
            .enqueue(object : Callback<User> {


                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val items = response.body()
                        listUsers.add(items!!)
                        listUser.postValue(listUsers)
                        user.postValue(items)
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d(TAG, t.message.toString())
                    norResponse.value = t.message.toString()

                }

            })
    }

    fun getUsers(): MutableLiveData<ArrayList<User>?> {
        return listUser
    }

    fun getDetailUser(): MutableLiveData<User?> {
        return user
    }

    fun throwable(): LiveData<String> {
        return norResponse
    }

}