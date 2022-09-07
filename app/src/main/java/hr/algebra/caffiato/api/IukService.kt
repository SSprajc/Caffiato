package hr.algebra.caffiato.api

import android.content.Context
import android.util.Log
import hr.algebra.caffiato.models.Address
import hr.algebra.caffiato.models.Caffe
import hr.algebra.caffiato.models.Challenge
import hr.algebra.caffiato.models.Deal
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class IukService(private val context: Context) {
    private var iukApi: IukApi
    private val client = OkHttpClient.Builder().build()

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        iukApi = retrofit.create(IukApi::class.java)
    }

    fun addUser(userItem: UserItem, onResult: (UserItem?) -> Unit) {
        val request = iukApi.addUser(userItem)
        request.enqueue(object : Callback<UserItem> {
            override fun onResponse(call: Call<UserItem>, response: Response<UserItem>) {
                val addedUser = response.body()
                onResult(addedUser)
            }

            override fun onFailure(call: Call<UserItem>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
                onResult(null)
            }

        })
    }

    fun getUser(email: String, onResult: (UserItem?) -> Unit) {
        val request = iukApi.getUser(email)
        request.enqueue(object : Callback<UserItem> {
            override fun onResponse(call: Call<UserItem>, response: Response<UserItem>) {
                val user = response.body()
                onResult(user)
            }

            override fun onFailure(call: Call<UserItem>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
                onResult(null)
            }

        })
    }

    fun deleteUser(id: Int) {
        val request = iukApi.deleteUser(id)
        request.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                println(response.body())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
                println(call)
            }

        })
    }

    fun deleteDeal(id: Int) {
        val request = iukApi.deleteDeal(id)
        request.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                println(response.body())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
                println(call)
            }

        })
    }

    fun updateUser(id: Int, userItem: UserItem, onResult: (UserItem?) -> Unit) {
        val request = iukApi.updateUser(id, userItem)
        request.enqueue(object : Callback<UserItem> {
            override fun onResponse(call: Call<UserItem>, response: Response<UserItem>) {
                val updatedUser = response.body()
                onResult(updatedUser)
            }

            override fun onFailure(call: Call<UserItem>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
                onResult(null)
            }

        })
    }

    fun updatePoints(id: Int, points: Int, onResult: (UserItem?) -> Unit) {
        val request = iukApi.updatePoints(id, points)
        request.enqueue(object : Callback<UserItem> {
            override fun onResponse(call: Call<UserItem>, response: Response<UserItem>) {
                val updatedUser = response.body()
                onResult(updatedUser)
            }

            override fun onFailure(call: Call<UserItem>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
                onResult(null)
            }

        })
    }

    //FEEDBACK
    fun addFeedback(feedbackItem: FeedbackItem, onResult: (FeedbackItem?) -> Unit) {
        val request = iukApi.addFeedback(feedbackItem)
        request.enqueue(object : Callback<FeedbackItem> {
            override fun onResponse(call: Call<FeedbackItem>, response: Response<FeedbackItem>) {
                val sentFeedback = response.body()
            }

            override fun onFailure(call: Call<FeedbackItem>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
                onResult(null)
            }

        })
    }

    //CAFFES
    fun getCaffes(onResult: (List<Caffe>?) -> Unit) {
        val request = iukApi.getCaffes()

        request.enqueue(object : Callback<List<CaffeItem>> {
            override fun onResponse(
                call: Call<List<CaffeItem>>,
                response: Response<List<CaffeItem>>
            ) {
                response.body()?.let {
                    val caffes = populateItems(it)
                    onResult(caffes)
                }
            }

            override fun onFailure(call: Call<List<CaffeItem>>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
                onResult(null)
            }

        })
    }

    //CHALLENGES
    fun getChallenges(onResult: (List<Challenge>?) -> Unit) {
        val request = iukApi.getChallenges()

        request.enqueue(object : Callback<List<ChallengeItem>> {
            override fun onResponse(
                call: Call<List<ChallengeItem>>,
                response: Response<List<ChallengeItem>>
            ) {
                response.body()?.let {
                    val challenges = populateChallenges(it)
                    onResult(challenges)
                }
            }

            override fun onFailure(call: Call<List<ChallengeItem>>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
                onResult(null)
            }

        })
    }


    //TRANSACTION
    fun addTransaction(
        transactionItem: TransactionItem,
        onResult: (TransactionItem?) -> Unit
    ) {
        val request = iukApi.addTransaction(transactionItem)

        request.enqueue(object : Callback<TransactionItem> {
            override fun onResponse(
                call: Call<TransactionItem>,
                response: Response<TransactionItem>
            ) {
                val transaction = response.body()
                onResult(transaction)
            }

            override fun onFailure(call: Call<TransactionItem>, t: Throwable) {
                Log.d(javaClass.name, t.message, t)
                onResult(null)
            }
        })

    }

    private fun populateItems(caffeItems: List<CaffeItem>): MutableList<Caffe> {
        val caffes = mutableListOf<Caffe>()

        caffeItems.forEach {
            caffes.add(
                Caffe(
                    null,
                    it.name,
                    Address(null, it.adress.city, it.adress.streetName, it.adress.streetNumber),
                    populateDeals(it.dealList)
                )
            )
        }
        return caffes
    }


    private fun populateChallenges(challengeItems: List<ChallengeItem>): MutableList<Challenge> {
        val challenges = mutableListOf<Challenge>()

        challengeItems.forEach {
            challenges.add(
                Challenge(
                    it.name,
                    it.description
                )
            )
        }
        return challenges
    }

    private fun populateDeals(dealList: List<DealItem>): List<Deal> {
        val deals = mutableListOf<Deal>()
        dealList.forEach {
            deals.add(
                Deal(
                    null,
                    it.name,
                    it.dateTime,
                    it.points,
                    it.price
                )
            )
        }
        return deals
    }


}