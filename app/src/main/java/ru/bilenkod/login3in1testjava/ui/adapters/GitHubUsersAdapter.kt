package ru.bilenkod.login3in1testjava.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.github_user_item.view.*
import ru.bilenkod.login3in1testjava.R
import ru.bilenkod.login3in1testjava.data.githubusersearch.model.GitHubUser
import ru.bilenkod.login3in1testjava.ui.graphics.CircleTransform
import java.util.*

class GitHubUsersAdapter
    : PagedListAdapter<GitHubUser, GitHubUsersAdapter.UsersViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.github_user_item, parent, false)
        return UsersViewHolder(v)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val currentItem = getItem(position)

        holder.name.text = currentItem?.login
        holder.id.text = String
                .format(Locale.getDefault(), "ID: %d", currentItem?.id)
        Picasso.get()
                .load(currentItem?.avatarUrl)
                .transform(CircleTransform())
                .placeholder(R.drawable.ic_default_avatar)
                .into(holder.img)
    }

    class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView = itemView.userAvatar
        var name: TextView = itemView.userNickname
        var id: TextView = itemView.userId
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GitHubUser>() {
            override fun areItemsTheSame(oldItem: GitHubUser, newItem: GitHubUser): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GitHubUser, newItem: GitHubUser): Boolean {
                return oldItem.login == newItem.login &&
                        oldItem.avatarUrl == newItem.avatarUrl
            }
        }
    }
}