package hr.algebra.caffiato

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hr.algebra.caffiato.api.IukService
import hr.algebra.caffiato.api.UserItem
import hr.algebra.caffiato.databinding.ActivityRegisterBinding
import hr.algebra.caffiato.framework.startActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        var id = 0

        binding.btnRegister.setOnClickListener {
            if (formFilled()) {
                if (isEmailValid(binding.etRegEmail.text.toString().trim())
                    && isPasswordValid(binding.etRegPassword.text.toString().trim())
                    && isCbChecked()
                ) {
                    val email = binding.etRegEmail.text.toString().trim()
                    val username = binding.etUsername.text.toString().trim()
                    val password = binding.etRegPassword.text.toString().trim()

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success")
                                val user = auth.currentUser
                                //add to database and get id
                                val userItem = UserItem(
                                    null,
                                    email,
                                    username,
                                    username,
                                    "",
                                    "2022-07-17T19:49:06.538Z",
                                    "",
                                    0,
                                    null,
                                    null
                                )

                                IukService(this).addUser(userItem) {
                                    if (it?.idUserCaffe != null) {
                                        id = it.idUserCaffe
                                        println(id)
                                    } else {
                                        println("Error saving in base")
                                    }
                                }


                                Toast.makeText(
                                    this,
                                    "You were registered successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                /*val intent =  Intent(this, MainActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    putExtra("points", 0)
                                }*/
                                startActivity<MainActivity>()


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext, "Wrong email/password. Try again.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                }
            }
        }
        binding.tvRegisterToLogin.setOnClickListener {
            startActivity<LoginActivity>()
        }
    }

    private fun isCbChecked(): Boolean {
        if (binding.cbTerms.isChecked) {
            return true
        } else {
            binding.cbTerms.error = "Must be checked"
            return false
        }

    }

    private fun formFilled(): Boolean {
        var ok = true
        arrayOf(
            binding.etUsername,
            binding.etRegEmail,
            binding.etRegPassword,
            binding.etPasswordRepeat
        ).forEach {
            if (it.text.isNullOrEmpty()) {
                it.error = "Please insert"
                ok = false
            }
        }
        return ok
    }

    private fun isPasswordValid(password: String): Boolean {
        println(password)
        if (password.length < 6) {
            binding.etRegPassword.error = "Too short"
            Toast.makeText(this, "Password must be at least 6 characters long!", Toast.LENGTH_SHORT)
                .show()
            return false
        } else if (password != binding.etPasswordRepeat.text.toString().trim()) {
            binding.etPasswordRepeat.error = "Must match"
            Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun isEmailValid(email: String): Boolean {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true
        } else {
            binding.etRegEmail.error = "Invalid"
            return false
        }
    }
}