package hr.algebra.caffiato

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.caffiato.framework.startActivity
import hr.algebra.caffiato.models.Deal4View

class DealAdapter(private val context: Context, private val deals: MutableList<Deal4View>) :
    RecyclerView.Adapter<DealAdapter.ViewHolder>() {
    class ViewHolder(dealView: View) : RecyclerView.ViewHolder(dealView) {

        private val tvDealName = dealView.findViewById<TextView>(R.id.tvDealName)
        private val tvCaffeName = dealView.findViewById<TextView>(R.id.tvCaffeName)
        private val tvAddress = dealView.findViewById<TextView>(R.id.tvAddress)
        private val tvPoints = dealView.findViewById<TextView>(R.id.tvPoints)
        private val tvPrice = dealView.findViewById<TextView>(R.id.tvPrice)

        fun bind(deal: Deal4View) {
            tvDealName.text = deal.name
            tvCaffeName.text = deal.caffeName
            tvAddress.text = deal.address
            tvPoints.text = deal.points.toString()
            tvPrice.text = deal.price.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            dealView = LayoutInflater.from(parent.context).inflate(R.layout.deal, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            AlertDialog.Builder(context).apply {
                setTitle("Are you sure you want to activate this deal?")
                setMessage("Press ok when you are ordering a deal and show the screen to your waiter.")
                setIcon(R.drawable.ic_points)
                setCancelable(true)
                setNegativeButton("Cancel", null)
                setPositiveButton("OK") { _, _ ->
                    context.startActivity<ActivateDealActivity>(
                        ITEM_POSITION,
                        position,
                        CAFFE_NAME,
                        null
                    )
                }
                show()
            }
        }
        holder.bind(deals[position])
    }

    override fun getItemCount() = deals.size
}