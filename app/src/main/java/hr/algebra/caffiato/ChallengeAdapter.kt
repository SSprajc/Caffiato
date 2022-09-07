package hr.algebra.caffiato

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.caffiato.models.Challenge

class ChallengeAdapter(private val context: Context, private val challenges: MutableList<Challenge>) :
    RecyclerView.Adapter<ChallengeAdapter.ViewHolder>() {
    class ViewHolder(challengeView: View) : RecyclerView.ViewHolder(challengeView) {

        private val tvChallengeName = challengeView.findViewById<TextView>(R.id.tvChallengeName)
        private val tvChallengeDescription = challengeView.findViewById<TextView>(R.id.evChallengeDesc)

        fun bind(challenge: Challenge) {
            tvChallengeName.text = challenge.name
            tvChallengeDescription.text = challenge.description
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            challengeView = LayoutInflater.from(parent.context).inflate(R.layout.challenge, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(challenges[position])
    }

    override fun getItemCount() = challenges.size
}