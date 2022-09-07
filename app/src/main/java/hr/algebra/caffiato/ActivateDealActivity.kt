package hr.algebra.caffiato

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import hr.algebra.caffiato.api.IukService
import hr.algebra.caffiato.api.TransactionItem
import hr.algebra.caffiato.api.UserItem
import hr.algebra.caffiato.databinding.ActivityActivateDealBinding
import hr.algebra.caffiato.framework.startActivity
import hr.algebra.caffiato.models.Deal4View
import hr.algebra.caffiato.models.Transaction
import hr.algebra.caffiato.models.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val ITEM_POSITION = "hr.algebra.iuk.item_position"
const val CAFFE_NAME = "hr.algebra.iuk.caffe_name"
const val BILL_URL = "hr.algebra.iuk.bill_url"
const val BONUS = 200


class ActivateDealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActivateDealBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActivateDealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = MainActivity.user

        val itemPosition = intent.getIntExtra(ITEM_POSITION, 0)
        val caffeName = intent.getStringExtra(CAFFE_NAME)
        val billUrl = intent.getStringExtra(BILL_URL)

        if (billUrl != null) {
            val transaction = parseUrl(billUrl)
            if (transaction.datetime > LocalDateTime.now().minusMinutes(5)) {
                binding.tvSpendCaffe.text = "Error"
                binding.tvSpendDeal.text = "Racun se mora skenirati unutar 5 minuta"
            } else {
                var userItem: UserItem
                var transactions = mutableListOf<Transaction>()
                IukService(this).getUser(user.email) {
                    if (it != null) {
                        userItem = it
                        it.transactions?.forEach {
                            transactions.add(
                                Transaction(
                                    it.amount,
                                    LocalDateTime.parse(
                                        it.time,
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.nn")
                                    )
                                )
                            )
                        }
                    } else {
                        println("Error in getting user")
                        Toast.makeText(
                            this,
                            "Error in getting data, check your connection",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                }
                val fifthBonus = transactions.size % 4 == 0 && transactions.size != 0
                when {
                    transaction.datetime.hour in 15..18 -> afternoonBonus(
                        fifthBonus,
                        transaction,
                        user
                    )
                    transaction.datetime.hour < 8 -> morningBonus(fifthBonus, transaction, user)
                    else -> noBonus(fifthBonus, transaction, user)
                }

            }


        } else {


            val caffes = MainActivity.caffes
            val deals = mutableListOf<Deal4View>()
            if (caffeName != null) {
                caffes.forEach { caffe ->
                    if (caffe.name == caffeName) {
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
                }
            } else {
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
            }
            val caffe = deals[itemPosition].caffeName
            val deal = deals[itemPosition].name
            val points = deals[itemPosition].points


            if (user.points >= points) {
                showDeal(caffe, deal, points)
                subtractPoints(user, points)
                //ako je
                //akok je time izmedju 16:00 i 19:00 sati dubli bodovi
                //dodaj u transkciju i provjeri ima li zadnjih 3 u zadnjih tjedan
                //akok je time prije 8:00 sati dubli bodovi

            } else {
                showError(user.points, points)
            }
        }
        Handler(Looper.getMainLooper()).postDelayed(
            { redirect() },
            5000
        )
    }

    private fun noBonus(fifthBonus: Boolean, transaction: Transaction, user: User) {
        if (fifthBonus) {
            val points = transaction.amount * 10 + BONUS
            addPoints(user, points)
            showText("Congrats", "Challenge 'Move it!' completed", "+", points)
        } else {
            val points = transaction.amount * 10
            addPoints(user, points)
            showText("Congrats", "You received:", "+", points)
        }
    }

    private fun morningBonus(fifthBonus: Boolean, transaction: Transaction, user: User) {
        if (fifthBonus) {
            val points = transaction.amount * 10 + 2 * BONUS
            addPoints(user, points)
            showText("Congrats", "Challenge 'Early bird!' and 'Move it!' completed", "+", points)
        } else {
            val points = transaction.amount * 10 + BONUS
            addPoints(user, points)
            showText("Congrats", "Challenge 'Early bird!' completed:", "+", points)
        }
    }

    private fun afternoonBonus(fifthBonus: Boolean, transaction: Transaction, user: User) {
        if (fifthBonus) {
            val points = transaction.amount * 10 + 2 * BONUS
            addPoints(user, points)
            showText("Congrats", "Challenge 'Move it!' and 'No crowd' completed", "+", points)
        } else {
            val points = transaction.amount * 10 + BONUS
            addPoints(user, points)
            showText("Congrats", "You received:", "+", points)
        }
    }

    private fun addPoints(user: User, points: Int) {
        IukService(this).addTransaction(
            TransactionItem(
                null,
                LocalDateTime.now().toString(),
                points,
                user._id!!
            )
        ) {
            if (it == null) {
                Toast.makeText(this, "Connect to internet and try again", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        val newPoints = user.points + points
        IukService(this).updatePoints(user._id!!, newPoints) {
            if (it != null) {
                Toast.makeText(this, "New number of points: '${it.points}'.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Connect to internet and try again", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun showText(topTV: String, midTV: String, prefix: String, points: Int) {
        binding.tvSpendCaffe.text = topTV
        binding.tvSpendDeal.text = midTV
        binding.tvSpendPrefix.text = prefix
        binding.tvSpendPoints.text = points.toString()
    }


    private fun parseUrl(billUrl: String): Transaction {
        //https://porezna.gov.hr/rn/?jir=c82f89a2-85d6-4634-adcd-3685a48ac1a2&datv=20220602_1051&izn=1100
        var temp = billUrl.substring(billUrl.indexOf('=') + 1)
        //d7af0a331a0272d11d91fa7ee39a0627&datv=20220616_1350&izn=90,97
        temp = temp.substring(temp.indexOf('=')+1)
        //20220616_1350&izn=90,97
        val datetimeString = temp.substring(0, 8) + temp.substring(9, 13)
        val amount = temp.substring(temp.indexOf('=')+1, temp.indexOf('=')+3)
        val datetime =
            LocalDateTime.parse(datetimeString, DateTimeFormatter.ofPattern("yyyyMMddHHmm"))
        return Transaction(amount.toInt(), datetime)
    }

    private fun subtractPoints(user: User, points: Int) {

        val newPoints = user.points - points

        IukService(this).updatePoints(user._id!!, newPoints) {
            if (it != null) {
                Toast.makeText(this, "New number of points: '${it.points}'.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Connect to internet and try again", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun showError(userPoints: Int, dealPoints: Int) {

        binding.tvSpendCaffe.text = "Insufficient points"
        binding.tvSpendCaffe.setTextColor(Color.RED)
        binding.tvSpendDeal.text = "You are missing:"
        binding.tvSpendPrefix.text = "->"
        val difference = dealPoints - userPoints
        binding.tvSpendPoints.text = "$difference"
    }

    private fun redirect() {
        startActivity<MainActivity>()
        finish()
    }

    private fun showDeal(caffe: String, deal: String, points: Int) {
        binding.tvSpendCaffe.text = caffe
        binding.tvSpendDeal.text = deal
        binding.tvSpendPrefix.text = "-"
        binding.tvSpendPoints.text = points.toString()
    }
}