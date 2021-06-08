package com.beginner.githubuser.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.beginner.githubuser.R
import com.beginner.githubuser.db.UserDatabase
import com.beginner.githubuser.db.dao.UserDao
import com.beginner.githubuser.model.User
import com.bumptech.glide.Glide

class WidgetViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {
    private lateinit var users: List<User>
    private lateinit var userDao: UserDao

    override fun onCreate() {
        userDao = UserDatabase.getDatabase(context).userDao()
    }

    override fun onDataSetChanged() {
        users = userDao.getWidgetFavorite()
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int = users.size

    override fun getViewAt(position: Int): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_item)

        if (users.isNotEmpty()) {

            val bitmap = Glide.with(context)
                .asBitmap()
                .load(users[position].avatar_url)
                .submit(512, 512)
                .get()

            views.setImageViewBitmap(R.id.image_view, bitmap)

            val extras = bundleOf(
                GithubUserWidget.EXTRA_ITEM to users[position].login
            )
            val fillInIntent = Intent()
            fillInIntent.putExtras(extras)
            views.setOnClickFillInIntent(R.id.image_view, fillInIntent)
        }
        return views
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = true
}
