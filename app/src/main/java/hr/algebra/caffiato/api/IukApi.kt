package hr.algebra.caffiato.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

//http://localhost:5287/api/
//http://192.168.43.200:5287/api/
//http://10.0.2.2:5287/api/
const val API_URL = "http://10.0.2.2:5287/"

interface IukApi {
    //users
    @Headers("Content-Type: application/json")
    @POST("api/UserCaffes")
    fun addUser(@Body userItem: UserItem) : Call<UserItem>

    @GET("api/UserCaffes/{email}")
    fun getUser(@Path("email") email: String) : Call<UserItem>

    @DELETE("api/UserCaffes/{id}")
    fun deleteUser(@Path("id") id: Int) : Call<ResponseBody>

    @PUT("api/UserCaffes/{id}")
    fun updateUser(
        @Path("id") id: Int,
        @Body userItem: UserItem
    ) : Call<UserItem>

    @PUT("api/UserCaffes/UpdatePoints/{id}")
    fun updatePoints(
        @Path("id") id: Int,
        @Body point: Int
    ) : Call<UserItem>

    //feedback
    @POST("api/Feedbacks")
    fun addFeedback(@Body feedbackItem: FeedbackItem) : Call<FeedbackItem>

    //caffe
    @GET("api/Caffes")
    fun getCaffes() : Call<List<CaffeItem>>

    //deals
    @DELETE("caffebar/api/Deals/{id}")
    fun deleteDeal(@Path("id") id: Int) : Call<ResponseBody>

    //transaction
    @POST("api/Transaction")
    fun addTransaction(@Body transactionItem: TransactionItem) : Call<TransactionItem>

    //challenges
    @GET("api/Challenges")
    fun getChallenges() : Call<List<ChallengeItem>>

}