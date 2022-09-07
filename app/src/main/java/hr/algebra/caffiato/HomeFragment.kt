package hr.algebra.caffiato

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.caffiato.databinding.FragmentHomeBinding
import hr.algebra.caffiato.models.Challenge
import hr.algebra.caffiato.models.Deal4View
import hr.algebra.caffiato.models.User

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var deals = mutableListOf<Deal4View>()
    private var challenges = mutableListOf<Challenge>()
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val caffes = MainActivity.caffes
        caffes.forEach { caffe ->
            caffe.dealList.forEach {
                deals.add(
                    Deal4View(
                        it.name,
                        caffe.name,
                        caffe.address.streetName + " " + caffe.address.streetNumber,
                        it.points,
                        it.price,
                        it.date
                    )
                )
            }
        }
        challenges = MainActivity.challenges

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = MainActivity.user
        binding.tvUserName.text = user.username
        binding.tvPoints.text = user.points.toString()

        binding.rvDeals.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = DealAdapter(context, deals)
        }




        binding.rvChallenge.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ChallengeAdapter(context, challenges)
        }
    }

}