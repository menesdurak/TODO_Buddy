package com.menesdurak.todobuddy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.menesdurak.todobuddy.R
import com.menesdurak.todobuddy.databinding.ActivityMainBinding
import com.menesdurak.todobuddy.ui.home.HomeFragmentDirections

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navHostFragment : NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        auth = Firebase.auth
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                val bundle = Bundle()
                bundle.putString("email", auth.currentUser?.email)
                navHostFragment.navController.navigate(R.id.homeFragment, bundle)
            }
            R.id.sign_out -> {
                auth.signOut()
                navHostFragment.navController.navigate(R.id.loginFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    fun hideActionBar() {
        supportActionBar?.hide()
    }

    fun showActionBar() {
        supportActionBar?.show()
    }
}