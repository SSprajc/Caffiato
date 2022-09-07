package hr.algebra.caffiato

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hr.algebra.caffiato.api.FeedbackItem
import hr.algebra.caffiato.api.IukService
import hr.algebra.caffiato.databinding.ActivityFeedbackBinding

class FeedbackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedbackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val manager = ReviewManagerFactory.create(this)

        setupListeners()
    }

    private fun setupListeners() {

        binding.tvSendFeedback.setOnClickListener {
            if (!binding.etFeedback.text.isNullOrEmpty()) {
                val message = binding.etFeedback.text.toString().trim()
                sendFeedback(message)
                binding.etFeedback.text.clear()
            } else {
                Toast.makeText(this, "First input your feedback in text area, than click send button", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendFeedback(message: String) {
        //feedbackIUK123
        val feedback = FeedbackItem(null, message)
        IukService(this).addFeedback(feedback) {
            if (it?.idFeedback != null) {
                Toast.makeText(this, "Thank you for giving us feedback!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Feedback was not sent, please try again later.", Toast.LENGTH_SHORT).show()
            }
        }
    }
/*
    private fun requestReviewInfoObject() {
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = task.result
            } else {
                // There was some problem, log or handle the error code.
                @ReviewErrorCode val reviewErrorCode = (task.getException() as TaskException).errorCode
            }
        }
    }

    private fun launchInAppReview() {
        val flow = manager.launchReviewFlow(activity, reviewInfo)
        flow.addOnCompleteListener { _ ->
            // The flow has finished. The API does not indicate whether the user
            // reviewed or not, or even whether the review dialog was shown. Thus, no
            // matter the result, we continue our app flow.
        }
    }

 */
}