import android.content.Context
import android.util.Base64
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.charset.Charset
import java.util.UUID
import java.util.concurrent.Executor

class BiometricAuthHelper(private val context: Context) {

    private var email: String = ""
    private var cipher: String = UUID.randomUUID().toString()
    private val executor: Executor = ContextCompat.getMainExecutor(context)
    private var biometricPrompt: BiometricPrompt? = null
    private var promptInfo: BiometricPrompt.PromptInfo? = null
    private val sharedPreferencesName = "secure_prefs"
    private val keyAlias = "secure_key"

    init {
        biometricPrompt = BiometricPrompt(
            context as FragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(context, "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
                    val sharedPreferences = EncryptedSharedPreferences.create(
                        sharedPreferencesName,
                        masterKeyAlias,
                        context,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                    )
                    if(!sharedPreferences.contains(email)) {
                        generateAndStoreToken()
                        Toast.makeText(context, "Authentication succeeded!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Funktion zur Zeit nicht verfügbar!", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()
    }

    fun authenticate(email: String): String {
        this.email = email
        promptInfo?.let { info ->
            biometricPrompt?.authenticate(info)
        }
        return cipher
    }

    private fun generateAndStoreToken() {
        CoroutineScope(Dispatchers.IO).launch {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            val sharedPreferences = EncryptedSharedPreferences.create(
                sharedPreferencesName,
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            with(sharedPreferences.edit()) {
                putString(
                    keyAlias,
                    Base64.encodeToString(
                        cipher.toByteArray(Charset.defaultCharset()),
                        Base64.DEFAULT
                    )
                )
                putString("email", email)
                apply()
            }
            withContext(Dispatchers.Main) {
                println("Token gespeichert")
            }
        }
    }
}