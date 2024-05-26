package de.max.trailblazerfrontendv1.Util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Base64
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import de.max.trailblazerfrontendv1.Api.FingerprintLoginApi
import de.max.trailblazerfrontendv1.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.charset.Charset
import java.util.concurrent.Executor

class BiometricLoginHelper(private val context: Context) {

    private val executor: Executor = ContextCompat.getMainExecutor(context)
    private var biometricPrompt: BiometricPrompt? = null
    private var promptInfo: BiometricPrompt.PromptInfo? = null
    private val sharedPreferencesName = "secure_prefs"
    private val keyAlias = "secure_key"

    data class FingerprintLoginData(
        var email: String,
        var cipher: String
    )

    init {
        biometricPrompt = BiometricPrompt(
            context as FragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(context, "Biometrie-Vorgang abgebrochen", Toast.LENGTH_LONG)
                        .show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    accessCredsAndLogin()
                    Toast.makeText(context, "Fingerabdruck erfolgreich erkannt", Toast.LENGTH_LONG).show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(context, "Fingerabdruck-Erkennung fehlgeschlagen", Toast.LENGTH_LONG).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometrie-Login")
            .setSubtitle("Halte deinen Finger an den Sensor.")
            .setNegativeButtonText("Abbrechen")
            .build()
    }

    fun authenticate(): String {
        promptInfo?.let { info ->
            biometricPrompt?.authenticate(info)
        }
        return ""
    }

    private fun accessCredsAndLogin() {
        CoroutineScope(Dispatchers.IO).launch {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            val sharedPreferences = EncryptedSharedPreferences.create(
                sharedPreferencesName,
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            val tokenBase64 = sharedPreferences.getString(keyAlias, null)
            val token = tokenBase64?.let {
                String(Base64.decode(it, Base64.DEFAULT), Charset.defaultCharset())
            }
            val email = sharedPreferences.getString("email", null)
            withContext(Dispatchers.Main) {
                if (token != null && email != null) {
                    println("Accessed Token: $token, accessed email: $email")
                    val response = FingerprintLoginApi.fingerprintLogin.fingerprintLogin(
                        FingerprintLoginData(
                            email = email,
                            cipher = token
                        )
                    )
                    UserConstants.accessToken = response.token
                    UserConstants.refreshToken = response.refresh_token
                    UserConstants.email = response.email
                    UserConstants.username = response.username
                    context.startActivity(Intent(context, MainActivity::class.java))
                    (context as Activity).finish()
                } else {
                    println("Kein Token gefunden")
                }
            }

        }
    }
}