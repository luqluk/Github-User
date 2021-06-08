package com.beginner.githubuser.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.beginner.githubuser.R
import com.beginner.githubuser.adapter.UserAdapter
import com.beginner.githubuser.databinding.ActivityFavoriteBinding
import com.beginner.githubuser.model.User
import com.beginner.githubuser.viewModel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private var users = ArrayList<User>()
    private var userAdapter = UserAdapter(users)
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.favorite)

        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.setHasFixedSize(true)

        userAdapter.notifyDataSetChanged()
        binding.rv.adapter = userAdapter

        observeFavorite()
        userAdapter.setUser(users)
    }

    private fun observeFavorite() {
        favoriteViewModel.listFavorite.observe(this, {
            it?.let { user ->
                if (!user.isNullOrEmpty()) {
                    users.addAll(user)
                    userAdapter.setUser(user)
                    binding.empty.visibility = View.GONE
                } else {
                    binding.rv.visibility = View.GONE
                    binding.empty.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.menu_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}