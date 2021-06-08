package com.beginner.githubuser.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.beginner.githubuser.R
import com.beginner.githubuser.activity.DetailActivity
import com.beginner.githubuser.activity.DetailActivity.Companion.EXTRA_USER
import com.beginner.githubuser.model.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(val listUser: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    fun setUser(users: List<User>) {
        listUser.clear()
        listUser.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ListViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
    )

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    inner class ListViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        fun bind(user: User) {
            val username = view.findViewById<TextView>(R.id.username)
            val location = view.findViewById<TextView>(R.id.location)
            val avatar = view.findViewById<CircleImageView>(R.id.avatar)
            val card = view.findViewById<CardView>(R.id.card_view)

            card.animation = AnimationUtils.loadAnimation(itemView.context, R.anim.alpha)

            username.text = user.login
            location.text = user.location

            Glide.with(itemView.context)
                .load(user.avatar_url)
                .apply(RequestOptions()).override(55, 55)
                .into(avatar)

            itemView.setOnClickListener {
                val mIntent = Intent(itemView.context, DetailActivity::class.java).apply {
                    putExtra(EXTRA_USER, user)
                }
                it.context.startActivity(mIntent)
            }
        }
    }

}