package de.max.trailblazerfrontendv1.Interfaces

//import androidx.compose.foundation.layout.FlowRowScopeInstance.weight
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.max.trailblazerfrontendv1.Api.ResetPasswordAPI
import de.max.trailblazerfrontendv1.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun PasswordResetForm(onBackClicked: () -> Unit) {

    var formInput by remember { mutableStateOf(FormInput()) }
    val context = LocalContext.current

    Surface (modifier =  Modifier.fillMaxSize()) {

        Column(modifier = Modifier
            .padding(top = 30.dp)
            .padding(all = 30.dp)) {

            //Reset-Code-Abschnitt
            Text(
                text = "Code anfordern",
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Für welchen Account soll das Passwort zurückgesetzt werden?")
            Spacer(modifier = Modifier.height(16.dp))
            ResetPwEmailField(value = formInput.email,
                onChange = { data -> formInput = formInput.copy(email = data) }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { requestResetCode(formInput.email, context) },
                enabled = formInput.email.isNotEmpty(),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reset-Code anfordern")
            }

            Spacer(modifier = Modifier.height(32.dp))

            //Passwort-Setzen-Abschnitt
            Text(
                text = "Passwort ändern",
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Gib ein neues Passwort an und bestätige es mit dem Reset-Code, den wir an deine E-Mail Adresse gesendet haben.")
            Spacer(modifier = Modifier.height(16.dp))
            ResetPwPasswordField(value = formInput.password,
                onChange = { data -> formInput = formInput.copy(password = data) })
            Spacer(modifier = Modifier.height(16.dp))
            ResetPwCodeField(value = formInput.pin,
                onChange = { data -> formInput = formInput.copy(pin = data) }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { setNewPassword(formInput, context) },
                enabled = formInput.isNotEmpty(),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Neues Passwort setzen")
            }
        }
    }
}

fun requestResetCode(email : String, context: Context) {
    GlobalScope.launch(Dispatchers.IO) {
        try {
            ResetPasswordAPI.passwordResetService.requestResetCode(email)

        }catch(e: Exception){
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Fehler ${e.message}. Bitte Eingaben überprüfen!", Toast.LENGTH_LONG)
                    .show()
            }
            println("error occured during login ${e.message}")
        }
    }
}

fun setNewPassword(formInput: FormInput, context: Context) {
    if (formInput.isNotEmpty()) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                ResetPasswordAPI.passwordResetService.setNewPassword(formInput)

                withContext(Dispatchers.Main) {
                    context.startActivity(Intent(context, LoginActivity::class.java))
                    (context as Activity).finish()
                }
            }catch(e: Exception){
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Fehler ${e.message}. Bitte Eingaben überprüfen!", Toast.LENGTH_LONG)
                        .show()
                }
                println("error occured during login ${e.message}")
            }
        }
    } else {
            Toast.makeText(context, "Bitte alle Felder ausfüllen: E-Mail, Passwort, Reset-Code", Toast.LENGTH_LONG).show()
    }
}

@Composable
fun ResetPwEmailField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "E-Mail",
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
        label = { Text(label) },
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )
}

@Composable
fun ResetPwPasswordField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Neues Passwort",
) {
    val focusManager = LocalFocusManager.current
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

    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier.fillMaxWidth(),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}

@Composable
fun ResetPwCodeField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Reset-Code",
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Numbers,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }
    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.NumberPassword),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() }
        ),
        label = { Text(label) },
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )
}

data class FormInput(
    var email: String = "",
    var password: String = "",
    var pin: String = ""
) {
    fun isNotEmpty(): Boolean {
        return email.isNotEmpty() && password.isNotEmpty() && pin.isNotEmpty()
    }
}