package hr.algebra.caffiato

import android.app.AlertDialog
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hr.algebra.caffiato.api.IukService
import hr.algebra.caffiato.api.UserItem
import hr.algebra.caffiato.databinding.ActivityProfileBinding
import hr.algebra.caffiato.framework.startActivity
import hr.algebra.caffiato.models.User

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userFire = Firebase.auth.currentUser!!

        IukService(this).getUser(userFire.email.toString()) {
            if (it?.idUserCaffe != null) {
                var user = User(
                    it.idUserCaffe,
                    it.email,
                    it.username,
                    it.points
                )
                var userItem = it
                bindValues(user)
                setupListeners(user, userItem)
            } else {
                println("Error in getting user by email")
                Toast.makeText(
                    this,
                    "Error in getting data, check your connection",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }


        //staviback

    }

    private fun setupListeners(user: User, userItem: UserItem) {
        binding.btnSave.setOnClickListener {
            val newName = binding.etEditUsername.text.toString().trim()
            if (user.username != newName) {
                userItem.username = newName
                user.username = newName
                IukService(this).updateUser(user._id!!, userItem) {
                    if (it?.idUserCaffe != null) {
                        Toast.makeText(
                            this,
                            "New username is: '${it.username}'.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this, "Error in saving username", Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                Toast.makeText(this, "Enter new name to save.", Toast.LENGTH_SHORT).show()
            }

        }
        binding.btnDeleteAcc.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Delete account")
                setMessage("Are you sure you want to permanently delete your account?")
                setIcon(R.drawable.ic_delete)
                setCancelable(true)
                setNegativeButton("Cancel", null)
                setPositiveButton("Yes") { _, _ -> delete(user) }
                show()
            }
        }
    }

    private fun delete(user: User) {

        val userFire = Firebase.auth.currentUser!!

        IukService(this).deleteUser(user._id!!)

        userFire.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                    startActivity<LoginActivity>()
                    MainActivity.mainActivity.finish()
                    finish()

                } else {
                    Toast.makeText(this, "Error, try again later", Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun bindValues(user: User) {
        binding.tvProfileEmail.text = user.email
        binding.etEditUsername.setText(user.username)
    }
}