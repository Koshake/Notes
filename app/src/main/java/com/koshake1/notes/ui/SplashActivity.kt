package com.koshake1.notes.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.koshake1.notes.R
import com.koshake1.notes.databinding.ActivitySplashBinding
import com.koshake1.notes.errors.NoAuthException
import com.koshake1.notes.viewmodel.SplashViewModel
import com.koshake1.notes.viewmodel.SplashViewState
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val RC_SIGN_IN = 458

class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModel<SplashViewModel>()

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySplashBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        viewModel.observeViewState().observe(this) {
            when (it) {
                is SplashViewState.Error -> renderError(it.error)
                SplashViewState.Auth -> renderData()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode != RC_SIGN_IN -> return
            resultCode != Activity.RESULT_OK -> finish()
            resultCode == Activity.RESULT_OK -> renderData()
        }
    }

    private fun startMainActivity() {
        startActivity(MainActivity.getStartIntent(this))
        finish()
    }

    private fun renderData() {
        startMainActivity()
    }

    private fun renderError(error: Throwable) {
        when (error) {
            is NoAuthException -> startLoginActivity()
            else -> error.message?.let { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        }
    }

    private fun startLoginActivity() {
        val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.ic_launcher_foreground)
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN,
        )
    }
}