package de.max.trailblazerfrontendv1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import de.max.trailblazerfrontendv1.Interfaces.LoginForm
import de.max.trailblazerfrontendv1.ui.theme.TrailBlazerFrontendV1Theme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TrailBlazerFrontendV1Theme {
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
    TrailBlazerFrontendV1Theme {
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