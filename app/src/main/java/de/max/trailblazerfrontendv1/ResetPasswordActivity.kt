package de.max.trailblazerfrontendv1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import de.max.trailblazerfrontendv1.Interfaces.PasswordResetForm
import de.max.trailblazerfrontendv1.Util.datastore.dataStoreReader
import de.max.trailblazerfrontendv1.ui.theme.TrailBlazerFrontendV1Theme

class ResetPasswordActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val darkModeEnabled = dataStoreReader(applicationContext = applicationContext, key = "dark_mode", initial = false)
            TrailBlazerFrontendV1Theme(isSystemInDarkTheme() || darkModeEnabled) {
                PasswordResetForm(onBackClicked = { navigateToLoginActivity() })
            }
        }
    }
    fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}