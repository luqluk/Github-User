package com.beginner.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.beginner.consumerapp.R
import com.beginner.consumerapp.data.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(private val listUser: ArrayList<User>) :
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


            username.text = user.login
            location.text = user.location

            Glide.with(itemView.context)
                .load(user.avatar_url)
                .apply(RequestOptions()).override(55, 55)
                .into(avatar)

            itemView.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    user.login,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}