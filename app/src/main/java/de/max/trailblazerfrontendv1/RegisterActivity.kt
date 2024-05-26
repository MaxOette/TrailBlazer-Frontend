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
import androidx.fragment.app.FragmentActivity
import de.max.trailblazerfrontendv1.Interfaces.RegisterForm
import de.max.trailblazerfrontendv1.Util.GeneralConstants
import de.max.trailblazerfrontendv1.Util.datastore.dataStoreReader
import de.max.trailblazerfrontendv1.ui.theme.TrailBlazerFrontendV1Theme

class RegisterActivity : FragmentActivity () {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val darkModeEnabled = dataStoreReader(applicationContext = applicationContext, key = "dark_mode", initial = false)
            TrailBlazerFrontendV1Theme(isSystemInDarkTheme() || darkModeEnabled) {
                RegisterForm(onLoginClicked = { navigateToLoginActivity() }, activity = this as FragmentActivity)
            }
        }
    }
    fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}