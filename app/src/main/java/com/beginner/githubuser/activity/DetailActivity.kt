package com.beginner.githubuser.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.beginner.githubuser.R
import com.beginner.githubuser.adapter.SectionPagerAdapter
import com.beginner.githubuser.databinding.ActivityDetailBinding
import com.beginner.githubuser.model.User
import com.beginner.githubuser.viewModel.DetailViewModel
import com.beginner.githubuser.viewModel.MainViewModel
import com.beginner.githubuser.widget.GithubUserWidget
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.shashank.sony.fancytoastlib.FancyToast
import kotlin.math.abs

class DetailActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDetailBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewPager: ViewPager2
    private lateinit var username: String

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var githubUserWidget: GithubUserWidget
    private lateinit var userEntity: User
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        username = user.login.toString()
        setViewPager(username)
        supportActionBar?.title = user.login
        userEntity = user
        getViewModel()
        mainViewModel.setDetail(user.login.toString())
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        observeDetail()

        binding.profile.setOnClickListener {
            val img = AlertDialog.Builder(this)
            val show = ImageView(this)
            Glide.with(this)
                .load(user.avatar_url)
                .into(show)
            img.setCustomTitle(show)
            val dialog: Dialog = img.create()
            dialog.show()
        }
        binding.followersCount.setOnClickListener {
            viewPager.currentItem = 0
        }
        binding.followingCount.setOnClickListener {
            viewPager.currentItem = 1
        }

        binding.btnFavorite.setOnClickListener {
            addOrRemoveFavorite()
        }
    }

    private fun observeDetail() {
        detailViewModel.data(username).observe(this, {
            isFavorite = it
            changeFavorite(it)
        })
        detailViewModel.isFavorite.observe(this, { fav ->
            isFavorite = fav
            changeFavorite(fav)
        })
    }

    private fun addOrRemoveFavorite() {
        githubUserWidget = GithubUserWidget()
        if (!isFavorite) {
            detailViewModel.addFavorite(userEntity)
            githubUserWidget.refreshWidget(applicationContext)
            FancyToast.makeText(
                this,
                resources.getString(R.string.favorite_add, userEntity.login),
                Toast.LENGTH_SHORT,
                FancyToast.DEFAULT,
                false
            ).show()
        } else {
            detailViewModel.removeFavorite(userEntity)
            githubUserWidget.refreshWidget(applicationContext)
            FancyToast.makeText(
                this,
                resources.getString(R.string.favorite_remove, userEntity.login),
                Toast.LENGTH_SHORT,
                FancyToast.ERROR,
                false
            ).show()
        }
    }

    private fun changeFavorite(condition: Boolean) {
        if (condition) {
            binding.btnFavorite.setImageResource(R.drawable.favorite)

        } else {
            binding.btnFavorite.setImageResource(R.drawable.unfavorite)
        }
    }

    private fun setViewPager(username: String) {
        val sectionPagerAdapter = SectionPagerAdapter(this)
        sectionPagerAdapter.username = username
        viewPager = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])

        }.attach()
    }

    private fun getViewModel() {
        showLoading(true)
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MainViewModel::class.java)

        mainViewModel.getDetailUser().observe(this, { user ->
            if (user != null) {
                userEntity = user
                val stringFollower = user.followers.formatFollow()
                val stringFollowing = user.following.formatFollow()

                with(binding) {

                    profile.loadImage(user.avatar_url)
                    tvName.text = user.name
                    tvCompany.text = user.company
                    tvLocation.text = user.location
                    followersCount.text = stringFollower
                    followingCount.text = stringFollowing
                    reposCount.text = user.public_repos.toString()

                    if (user.location == null) {
                        tvLocation.visibility = View.GONE
                    }
                    if (user.company == null) {
                        tvCompany.visibility = View.GONE
                    }

                }

                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.shimmerViewContainer.visibility = View.VISIBLE
            binding.shimmerViewContainer.startShimmer()
            binding.layout.visibility = View.INVISIBLE
        } else {
            binding.shimmerViewContainer.visibility = View.GONE
            binding.shimmerViewContainer.stopShimmer()
            binding.layout.visibility = View.VISIBLE


        }
    }

    private fun Int.formatFollow(): String {
        return when {
            abs(this / 1000000) >= 1 -> {
                (this / 1000000).toString() + "m"
            }
            abs(this / 1000) >= 1 -> {
                (this / 1000).toString() + "k"
            }
            else -> {
                this.toString()
            }
        }
    }

    private fun ImageView.loadImage(url: String?) {
        Glide.with(context)
            .load(url)
            .apply(RequestOptions()).override(500, 500)
            .centerCrop()
            .into(this)
    }

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_detail, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}