package hr.algebra.caffiato

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hr.algebra.caffiato.databinding.FragmentMoreBinding
import hr.algebra.caffiato.framework.startActivity


class MoreFragment : Fragment() {

    private lateinit var binding: FragmentMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreBinding.inflate(inflater, container, false)

        setupListeners()
        return binding.root
    }

    private fun setupListeners() {
        binding.btnProfile.setOnClickListener {
            requireContext().startActivity<ProfileActivity>()
        }
        binding.btnSettings.setOnClickListener {
            requireContext().startActivity<SettingsActivity>()
        }
        binding.btnFeedback.setOnClickListener {
            requireContext().startActivity<FeedbackActivity>()
        }
        binding.btnAbout.setOnClickListener {
            requireContext().startActivity<AboutActivity>()
        }
        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Log out")
                setMessage("Are you sure you want to log out?")
                setIcon(R.drawable.ic_logout)
                setCancelable(true)
                setNegativeButton("Cancel", null)
                setPositiveButton("Yes"){_, _ -> logout()}
                show()
            }
        }
    }

    private fun logout() {
        Firebase.auth.signOut()
        requireContext().startActivity<LoginActivity>()
        activity?.finish()
    }
}