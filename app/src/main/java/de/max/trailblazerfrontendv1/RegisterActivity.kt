package de.max.trailblazerfrontendv1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import de.max.trailblazerfrontendv1.Interfaces.RegisterForm
import de.max.trailblazerfrontendv1.Util.GeneralConstants
import de.max.trailblazerfrontendv1.ui.theme.TrailBlazerFrontendV1Theme

class RegisterActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TrailBlazerFrontendV1Theme(isSystemInDarkTheme() || GeneralConstants.forceDarkMode) {
                RegisterForm(onLoginClicked = { navigateToLoginActivity() })
            }
        }
    }
    fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}