package com.beginner.consumerapp

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.beginner.consumerapp.adapter.UserAdapter
import com.beginner.consumerapp.data.User
import com.beginner.consumerapp.util.toListUserFavorite

class MainActivity : AppCompatActivity() {
    private var users = ArrayList<User>()
    private var userAdapter = UserAdapter(users)
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptylayout: LinearLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.rv)
        swipeRefreshLayout = findViewById(R.id.swipe)
        emptylayout = findViewById(R.id.empty)

        recyclerView.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(ArrayList())
        recyclerView.adapter = userAdapter

        fetchData()

        swipeRefreshLayout.setOnRefreshListener {
            fetchData()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun fetchData() {
        val tableName = "users_favorite"
        val authority = "com.beginner.githubuser"

        val uri: Uri = Uri.Builder()
            .scheme("content")
            .authority(authority)
            .appendPath(tableName)
            .build()

        val contentResolver = this.contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)

        if ((cursor != null) && (cursor.count > 0)) {
            getCursor(cursor)
            emptylayout.visibility = View.GONE
        } else {
            getCursor(cursor)
            emptylayout.visibility = View.VISIBLE
        }
    }

    private fun getCursor(cursor: Cursor?) {
        cursor?.let {
            userAdapter.setUser(it.toListUserFavorite())
        }

    }
}
