package ru.bilenkod.login3in1testjava.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.app_bar_main2.*
import kotlinx.android.synthetic.main.nav_header_main2.view.*
import ru.bilenkod.login3in1testjava.R
import ru.bilenkod.login3in1testjava.ui.SharedViewModel
import ru.bilenkod.login3in1testjava.ui.fragments.GitHubSearchFragment
import ru.bilenkod.login3in1testjava.ui.fragments.LoginFragment
import ru.bilenkod.login3in1testjava.ui.graphics.CircleTransform

class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        LoginFragment.LoginFragmentListener {

    lateinit var sharedViewModel: SharedViewModel
    private lateinit var sidebarImage: ImageView
    private lateinit var sidebarName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        connectViewModelObservers()
    }

    private fun setupUI() {
        setContentView(R.layout.activity_main2)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        val hView = navigationView.getHeaderView(0)
        sidebarName = hView.sidebarName as TextView
        sidebarImage = hView.imageViewSidebar as ImageView
    }

    private fun connectViewModelObservers() {

        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)

        sharedViewModel.imageUrl.observe(this, Observer { s ->
            Picasso.get()
                    .load(s)
                    .placeholder(R.drawable.ic_default_avatar)
                    .transform(CircleTransform()).into(sidebarImage)
        })

        sharedViewModel.userName.observe(this, Observer { s -> sidebarName.text = s })

        sharedViewModel.isLoggedIn.observe(this, Observer { loggedIn ->
            swapUILoginState(loggedIn)
        })
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchItem.collapseActionView()
                sharedViewModel.provideUserList(query, false)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                sharedViewModel.provideUserList(query, true)
                return false
            }
        })
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nav_logout) {
            sharedViewModel.logout()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun swapUILoginState(isLoggedIn: Boolean) {
        if (isLoggedIn)
            applyLoggedInUiState()
        else
            applyNotLoggedInUiState()
    }

    private fun applyNotLoggedInUiState() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_for_fragment, LoginFragment()).commit()
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        toolbar.visibility = View.GONE
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun applyLoggedInUiState() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_for_fragment, GitHubSearchFragment()).commit()
        toolbar.visibility = View.VISIBLE
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        sharedViewModel.handleOnActivityResult(requestCode, resultCode, data)
    }

    override fun initLoginVia(apiKey: Int) {
        sharedViewModel.initLoginViaFrom(apiKey, this)
    }
}
