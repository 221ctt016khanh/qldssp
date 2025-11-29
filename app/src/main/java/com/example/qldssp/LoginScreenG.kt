import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginScreenG(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val webClientId = "298917524700-07gdoi0g3jv3o4cle35m7kdcth6cbhcm.apps.googleusercontent.com" // replace with your Firebase OAuth2 client ID

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        val coroutineScope = rememberCoroutineScope()

        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        val cm = CredentialManager.create(context)

                        val googleOption = GetGoogleIdOption.Builder()
                            .setServerClientId(webClientId)
                            .setFilterByAuthorizedAccounts(false)
                            .setAutoSelectEnabled(true)
                            .build()

                        val request = GetCredentialRequest.Builder()
                            .addCredentialOption(googleOption)
                            .build()

                        // suspend function call
                        val result: GetCredentialResponse = cm.getCredential(context, request)

                        val credential = result.credential
                        println(credential)
                        if (credential.type== GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            val googleCred= GoogleIdTokenCredential.createFrom(credential.data)
                            val idToken = googleCred.idToken
                            println(idToken)
                            if (idToken != null) {
                                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                                auth.signInWithCredential(firebaseCredential)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            println("User ID: ${auth.currentUser?.uid}")
                                            navController.navigate("mainScreen") {
                                                popUpTo("loginScreen") { inclusive = true }
                                            }
                                        } else {
                                            Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(context, "No ID token received", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Not a Google credential", Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: Exception) {
                        Toast.makeText(context, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign in with Google")
        }

    }}