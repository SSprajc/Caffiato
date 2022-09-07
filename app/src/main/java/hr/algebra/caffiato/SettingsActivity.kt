package hr.algebra.caffiato

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import hr.algebra.caffiato.databinding.ActivitySettingsBinding
import java.util.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding


    lateinit var locale: Locale
    private var currentLanguage = "en"
    private var currentLang: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val languages = arrayOf("hr",  "en")


        binding.tvChangeLanguage.setOnClickListener {
            changeLanguage()
        }

    }

    private fun changeLanguage() {
        val languages = arrayOf("Croatian",  "English")
        AlertDialog.Builder(this).apply {
            setTitle("Languages")
            setSingleChoiceItems(languages, -1) { _, which ->
                if (which == 0) {
                    setLocale("hr")
                } else if (which == 1) {
                    setLocale("en")
                }
            }
            setPositiveButton("Ok", null)
            setCancelable(true)
            show()
        }
    }

    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = this.resources.configuration
        config.setLocale(locale)
        this.createConfigurationContext(config)
        this.resources.updateConfiguration(config, this.resources.displayMetrics)
    }
}