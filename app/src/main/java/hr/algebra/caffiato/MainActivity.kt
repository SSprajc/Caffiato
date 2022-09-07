package hr.algebra.caffiato

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hr.algebra.caffiato.api.IukService
import hr.algebra.caffiato.databinding.ActivityMainBinding
import hr.algebra.caffiato.framework.startActivity
import hr.algebra.caffiato.models.Caffe
import hr.algebra.caffiato.models.Challenge
import hr.algebra.caffiato.models.User


class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var mainActivity: MainActivity
        lateinit var caffes: List<Caffe>
        lateinit var user: User
        lateinit var challenges: MutableList<Challenge>
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainActivity = this

        val userFire = Firebase.auth.currentUser
        if (userFire == null) {
            startActivity<LoginActivity>()
            finish()
        } else {
            IukService(this).getUser(userFire.email.toString()) {
                if (it != null) {
                    user = User(
                        it.idUserCaffe,
                        it.email,
                        it.username,
                        it.points
                    )
                } else {
                    println("Error in getting user by email")
                    /*Toast.makeText(
                        this,
                        "Error in getting data, check your connection",
                        Toast.LENGTH_LONG
                    ).show()
                     */
                    Firebase.auth.signOut()
                    finish()
                }
            }
        }

        IukService(this).getCaffes {
            if (!it.isNullOrEmpty()) {
                caffes = it
            } else {
                /*Toast.makeText(this, "Error in getting deals/caffes", Toast.LENGTH_SHORT).show()
                 */
                finish()
            }
        }

        IukService(this).getChallenges {
            if (!it.isNullOrEmpty()) {
                challenges = it as MutableList<Challenge>
            } else {
                //Toast.makeText(this, "Error in getting deals/caffes", Toast.LENGTH_SHORT).show()
            }
        }

        initNavigation()

    }

    private fun initNavigation() {
        NavigationUI.setupWithNavController(
            binding.navigationView,
            Navigation.findNavController(this, R.id.navHostFragment)
        )
    }
}