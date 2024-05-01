package de.max.trailblazerfrontendv1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.max.trailblazerfrontendv1.Interfaces.LoginForm
import de.max.trailblazerfrontendv1.Util.datastore.dataStoreReader
import de.max.trailblazerfrontendv1.ui.theme.TrailBlazerFrontendV1Theme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val darkModeEnabled = dataStoreReader(applicationContext = applicationContext, key = "dark_mode", initial = false)
            TrailBlazerFrontendV1Theme(isSystemInDarkTheme() || darkModeEnabled) {
                LoginForm(onRegisterClicked = { navigateToRegisterActivity() })
            }
        }
    }

    fun navigateToRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
@Preview(showBackground = true, device = "id:Nexus One", showSystemUi = true)
@Composable
fun LoginPreview() {
    TrailBlazerFrontendV1Theme(isSystemInDarkTheme()) {
        LoginForm(onRegisterClicked = { })
    }
}

@Preview(showBackground = true, device = "id:Nexus One", showSystemUi = true)
@Composable
fun LoginPreviewDark() {
    TrailBlazerFrontendV1Theme(darkTheme = true) {
        LoginForm(onRegisterClicked = { })
    }
}