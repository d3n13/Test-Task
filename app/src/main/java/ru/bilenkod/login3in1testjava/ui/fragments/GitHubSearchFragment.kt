package ru.bilenkod.login3in1testjava.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_git_hub_search.view.*
import ru.bilenkod.login3in1testjava.R
import ru.bilenkod.login3in1testjava.ui.SharedViewModel
import ru.bilenkod.login3in1testjava.ui.adapters.GitHubUsersAdapter

class GitHubSearchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_git_hub_search, container, false)
        val recyclerView = v.recyclerViewGitHubUserList
        val adapter = GitHubUsersAdapter()

        recyclerView.adapter = adapter
        recyclerView.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.setHasFixedSize(true)

        val myViewModel = ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)
        myViewModel.usersList.observe(viewLifecycleOwner, Observer { gitHubUsers ->
            adapter.submitList(gitHubUsers)
        })

        return v
    }
}