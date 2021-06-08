package com.beginner.githubuser.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.beginner.githubuser.R
import com.beginner.githubuser.adapter.UserAdapter
import com.beginner.githubuser.databinding.ActivityMainBinding
import com.beginner.githubuser.model.User
import com.beginner.githubuser.viewModel.MainViewModel
import com.shashank.sony.fancytoastlib.FancyToast


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var users = ArrayList<User>()
    private var userAdapter = UserAdapter(users)
    private lateinit var mainViewModel: MainViewModel
    private lateinit var imm: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        actionBar?.elevation = 0F
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.setHasFixedSize(true)


        imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        userAdapter.notifyDataSetChanged()
        binding.rv.adapter = userAdapter
        getViewModel()
        mainViewModel.setUser("Github")

        binding.search.setOnEditorActionListener(OnEditorActionListener { query, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (query.text.isEmpty()) {
                    FancyToast.makeText(
                        this,
                        getString(R.string.caution_input),
                        Toast.LENGTH_SHORT,
                        FancyToast.DEFAULT,
                        false
                    )
                        .show()
                } else {
                    performSearch(query)
                }
                return@OnEditorActionListener true
            }
            false
        })

        binding.textLayout.setEndIconOnClickListener {
            if (binding.search.text?.length == 0) {
                binding.textLayout.visibility = View.GONE
                imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            } else {
                binding.search.setText("")
            }
        }
    }

    private fun performSearch(query: TextView) {
        binding.textLayout.clearFocus()
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        mainViewModel.listUsers.clear()
        userAdapter.listUser.clear()
        mainViewModel.setUser(query.text.toString())
        userAdapter.notifyDataSetChanged()

        binding.empty.visibility = View.GONE
        binding.noInternet.visibility = View.GONE
        showLoading(true)
    }

    private fun getViewModel() {
        showLoading(true)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MainViewModel::class.java)

        mainViewModel.getUsers().observe(this, { users ->

            if (users != null) {
                userAdapter.setUser(users)
                showLoading(false)
                binding.empty.visibility = View.GONE
            }
            if (users?.size == 0) {
                with(binding) {
                    shimmerViewContainer.stopShimmer()
                    shimmerViewContainer.visibility = View.GONE
                    empty.visibility = View.VISIBLE
                }
            }
        })

        mainViewModel.throwable().observe(this, { response ->
            FancyToast.makeText(this, response, Toast.LENGTH_SHORT, FancyToast.ERROR, false).show()
            binding.noInternet.visibility = View.VISIBLE
            binding.rv.visibility = View.GONE
            showLoading(false)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                binding.textLayout.visibility = View.VISIBLE
                binding.textLayout.requestFocus()
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }
            R.id.menu_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }

            R.id.menu_fav -> {
                startActivity(Intent(this, FavoriteActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.shimmerViewContainer.visibility = View.VISIBLE
            binding.shimmerViewContainer.startShimmer()
        } else {
            binding.shimmerViewContainer.stopShimmer()
            binding.shimmerViewContainer.visibility = View.GONE
        }
    }
}