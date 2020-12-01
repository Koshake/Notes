package com.koshake1.notes.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.koshake1.notes.R

class MainActivity : AppCompatActivity(), LogoutDialog.LogoutListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.inTransaction {
                add(R.id.fragment_container, MainFragment())
            }
        }
    }

    override fun onLogout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                startActivity(Intent(this, SplashActivity::class.java))
                finish()
            }
    }

    fun navigateTo(fragment: Fragment) {
        supportFragmentManager.inTransaction {
            add(R.id.fragment_container, fragment)
        }
    }

    companion object {
        fun getStartIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}