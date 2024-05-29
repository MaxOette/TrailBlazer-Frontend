package de.max.trailblazerfrontendv1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.fragment.app.FragmentActivity
import de.max.trailblazerfrontendv1.Interfaces.LoginForm
import de.max.trailblazerfrontendv1.Util.datastore.dataStoreReader
import de.max.trailblazerfrontendv1.ui.theme.TrailBlazerFrontendV1Theme

class LoginActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val darkModeEnabled = dataStoreReader(applicationContext = applicationContext, key = "dark_mode", initial = false)
            TrailBlazerFrontendV1Theme(isSystemInDarkTheme() || darkModeEnabled) {
                LoginForm(onRegisterClicked = { navigateToRegisterActivity() }, onPwResetClicked = { navigateToPwResetActivity() }, activity = this as FragmentActivity)
            }
        }
    }

    fun navigateToRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun navigateToPwResetActivity() {
        val intent = Intent(this, ResetPasswordActivity::class.java)
        startActivity(intent)
    }
}