package com.beginner.githubuser.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beginner.githubuser.R
import com.beginner.githubuser.activity.DetailActivity.Companion.EXTRA_USER
import com.beginner.githubuser.adapter.UserAdapter
import com.beginner.githubuser.model.User
import com.beginner.githubuser.viewModel.FollowViewModel
import com.facebook.shimmer.ShimmerFrameLayout

/**
 * A simple [Fragment] subclass.
 */
class FollowerFragment : Fragment() {

    private lateinit var followViewModel: FollowViewModel
    private val listFollower = ArrayList<User>()
    private var userAdapter = UserAdapter(listFollower)
    private var username: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var shimmer: ShimmerFrameLayout
    private lateinit var empty: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_follower, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username = arguments?.getString(EXTRA_USER)
        recyclerView = view.findViewById(R.id.following_recyle)
        shimmer = view.findViewById(R.id.shimmer_view_container)
        empty = view.findViewById(R.id.empty)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        userAdapter = UserAdapter(listFollower)
        recyclerView.adapter = userAdapter

        getViewModel()
    }

    private fun getViewModel() {
        followViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowViewModel::class.java)
        followViewModel.setFollower(username.toString())
        followViewModel.getFollower().observe(requireActivity(), { followers ->
            if (followers != null) {
                userAdapter.setUser(followers)
                showLoading(false)
            }

            if (followers.size == 0) {
                empty.visibility = View.VISIBLE
            }

        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            shimmer.visibility = View.VISIBLE
            shimmer.startShimmer()
        } else {
            shimmer.stopShimmer()
            shimmer.visibility = View.GONE
        }
    }
}


