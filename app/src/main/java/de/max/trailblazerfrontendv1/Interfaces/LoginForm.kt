package de.max.trailblazerfrontendv1.Interfaces

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.RoundedCorner
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowRowScopeInstance.weight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import de.max.trailblazerfrontendv1.ui.theme.TrailBlazerFrontendV1Theme
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Shapes
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import de.max.trailblazerfrontendv1.Api.LoginApi
import de.max.trailblazerfrontendv1.Api.LoginUserData
import de.max.trailblazerfrontendv1.LoginActivity
import de.max.trailblazerfrontendv1.MainActivity
import de.max.trailblazerfrontendv1.Util.GeneralConstants
import de.max.trailblazerfrontendv1.Util.UserConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun LoginForm(onRegisterClicked: () -> Unit, onPwResetClicked: () -> Unit) {
    Surface {

        val navController = rememberNavController()
        var credentials by remember { mutableStateOf(Credentials()) }
        val context = LocalContext.current

        Text(
            text = "Anmelden",
            modifier = Modifier
                .padding(all = 30.dp)
                .padding(top = 70.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)

        ) {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ){
                LoginField(
                    value = credentials.login,
                    onChange = { data -> credentials = credentials.copy(login = data) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                PasswordField(
                    credentials,
                    context,
                    value = credentials.pwd,
                    onChange = { data -> credentials = credentials.copy(pwd = data) },
                    submit = { checkCredentials(credentials, context) },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(text = buildAnnotatedString {
                    append("Passwort vergessen? ")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("Zurücksetzen")
                    }
                }, modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable { onPwResetClicked() }
                )
                /*
                Spacer(modifier = Modifier.height(16.dp))
                LabeledCheckbox(
                    "angemeldet bleiben",
                    onCheckChanged = {
                        credentials = credentials.copy(remember = credentials.remember)
                    },
                    isChecked = credentials.remember
                ) */
                Spacer(modifier = Modifier.height(28.dp))
                Button(
                    onClick = { checkCredentials(credentials, context) },
                    enabled = credentials.isNotEmpty(),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }

                Spacer(modifier = Modifier.height(16.dp))
                /*
                Button(
                    onClick = { adminLogin(context)},
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text("Admin")
                } */
            }
            Text(
                text = buildAnnotatedString {
                    append("Du hast noch keinen Account? ")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("Registrieren")
                    }
                },
                modifier = Modifier
                    .clickable { onRegisterClicked() }
                    .padding(8.dp)
                    .padding(bottom = 16.dp)

            )
        }

    }
}

fun adminLogin(context: Context){
    val loginUserData = LoginUserData (
//        email = "datev@test.de",
//        password = "password123."
//        email= "email@email.de",
//        password = "password123."
        email = "test1@test.de",
        password = "password123."
    )
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val response = LoginApi.loginService.postLoginUser("UserPasswordAuth", loginUserData)
            println(response)
            UserConstants.refreshToken = response.refresh_token
            UserConstants.accessToken = response.token
            UserConstants.email = response.email

            withContext(Dispatchers.Main) {
                context.startActivity(Intent(context, MainActivity::class.java))
                (context as Activity).finish()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error occured: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
            println("error occured during login ${e.message}")
        }
    }
}


fun checkCredentials(creds: Credentials, context: Context) {
    if(creds.isNotEmpty()){
        val loginUserData = LoginUserData (
            email = creds.login,
            password = creds.pwd
        )
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = LoginApi.loginService.postLoginUser("UserPasswordAuth", loginUserData)
                println("###################")
                println(response)
                UserConstants.refreshToken = response.refresh_token
                UserConstants.accessToken = response.token
                UserConstants.username = response.email //TODO: Wenn das BE die Daten mal richtigrum zurückliefert, wieder anpassen.
                //UserConstants.email = response.email

                withContext(Dispatchers.Main) {
                    context.startActivity(Intent(context, MainActivity::class.java))
                    (context as Activity).finish()
                }
            }catch(e: Exception){
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Zugangsdaten inkorrekt: ${e.message}", Toast.LENGTH_LONG)
                        .show()
                }
                println("error occured during login ${e.message}")
            }

        }
    }else{
        Toast.makeText(context, "Zugangsdaten inkorrekt", Toast.LENGTH_SHORT).show()
    }
}

data class Credentials(
    var login: String = "",
    var pwd: String = "",
    var remember: Boolean = false
) {
    fun isNotEmpty(): Boolean {
        return login.isNotEmpty() && pwd.isNotEmpty()
    }
}

@Composable
fun LabeledCheckbox(
    label: String,
    onCheckChanged: () -> Unit,
    isChecked: Boolean
) {
    Row(
        Modifier
            .clickable(
                onClick = onCheckChanged
            )
            .padding(4.dp)
    ) {
        Checkbox(checked = isChecked, onCheckedChange = null)
        Spacer(Modifier.size(6.dp))
        Text(label)
    }
}

@Composable
fun LoginField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "E-Mail",
    placeholder: String = "Gib Deine E-Mail-Adresse ein"
) {

    val focusManager = LocalFocusManager.current
    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Email,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }

    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        //placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )
}

@Composable
fun PasswordField(
    creds: Credentials,
    context: Context,
    value: String,
    onChange: (String) -> Unit,
    submit: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Passwort",
    placeholder: String = "Gib Dein Passwort ein"
) {

    var isPasswordVisible by remember { mutableStateOf(false) }

    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Lock,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }
    val trailingIcon = @Composable {
        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
            Icon(
                if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )

        }
    }
    val keyboardController = LocalSoftwareKeyboardController.current


    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                checkCredentials(creds, context)
            }
        ),
        //placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}