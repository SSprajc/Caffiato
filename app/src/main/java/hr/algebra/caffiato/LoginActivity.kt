package hr.algebra.caffiato

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hr.algebra.caffiato.api.IukService
import hr.algebra.caffiato.databinding.ActivityLoginBinding
import hr.algebra.caffiato.framework.startActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        binding.btnLogin.setOnClickListener {
            if (formFilled()) {

                val email = binding.etLogEmail.text.toString().trim()
                val password = binding.etLogPassword.text.toString().trim()
                println(email)
                println(password)

                IukService(this).getUser(email) {
                    if (it?.oib != ""){
                        Toast.makeText(this, "Account already exists as a caffe owner!", Toast.LENGTH_SHORT).show()
                        //logout
                        finish()
                    }
                }
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser

                            startActivity<MainActivity>()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Login failed, try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.etLogPassword.text.clear()
                        }
                    }
            }
        }
        setupOtherListeners()
    }

    private fun setupOtherListeners() {
        binding.tvForgotPass.setOnClickListener {

            if (!binding.etLogEmail.text.isNullOrEmpty() && isEmailValid(
                    binding.etLogEmail.text.toString().trim()
                )
            ) {

                val emailAddress = binding.etLogEmail.text.toString().trim()

                Firebase.auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Email sent.")
                            Toast.makeText(this, "Email sent to: $emailAddress", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(
                                this,
                                "Couldn't send to $emailAddress Try again",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    this,
                    "1. Input correct email address for your account in field 'Email'\n" +
                            "2. Click 'Forgot password'\n" +
                            "3. Check your email (spam folder) and follow further instructions",
                    Toast.LENGTH_LONG
                ).show()
                /*AlertDialog.Builder(this).apply {
                        setTitle("To change your password: ")
                        setMessage("1. Input correct email address for your account in field 'Email'\n" +
                                "2. Click 'Forgot password'\n" +
                                "3. Check your email (spam folder) and follow further instructions")
                        setCancelable(true)
                        setIcon(R.drawable.ic_about)
                        show()
                    }
                     */
            }
        }
        binding.tvLoginToRegister.setOnClickListener {
            startActivity<RegisterActivity>()
        }
    }

    private fun isEmailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun formFilled(): Boolean {
        var ok = true
        arrayOf(
            binding.etLogEmail,
            binding.etLogPassword
        ).forEach {
            if (it.text.isNullOrEmpty()) {
                it.error = "Please insert"
                ok = false
            }
        }
        return ok
    }
}