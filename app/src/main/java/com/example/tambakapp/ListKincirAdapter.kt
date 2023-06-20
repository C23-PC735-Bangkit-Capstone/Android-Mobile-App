package com.example.tambakapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tambakapp.data.response.ResponseDeviceItem

class ListKincirAdapter(private val listKincir: List<KincirData>, private val context: Context) : RecyclerView.Adapter<ListKincirAdapter.ListViewHolder>() {

    /*
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }*/

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvBattery: TextView = itemView.findViewById(R.id.tv_battery)
        val tvCondition: TextView = itemView.findViewById(R.id.tv_condition)
        val tvConnection: TextView = itemView.findViewById(R.id.tv_connection)
        val clDetails: ConstraintLayout = itemView.findViewById(R.id.cl_details)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_kincir, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = listKincir[position]
        holder.tvName.text = "Kincir " + currentItem.deviceId.toString()

        val statusGreenColor = ContextCompat.getColor(context, R.color.status_green)
        val statusRedColor = ContextCompat.getColor(context, R.color.status_red)
        val statusYellowColor = ContextCompat.getColor(context, R.color.status_yellow)
        val statusGreyColor = ContextCompat.getColor(context, R.color.grey)
        holder.tvBattery.text = currentItem.batteryStrength.toString() + "%"
        when (currentItem.batteryStrength) {
            in 70..100 -> holder.tvBattery.setTextColor(statusGreenColor)
            in 47..69 -> holder.tvBattery.setTextColor(statusYellowColor)
            in 0..46 -> holder.tvBattery.setTextColor(statusRedColor)
            else -> holder.tvBattery.setTextColor(statusGreyColor)
        }

        holder.tvCondition.text = when (currentItem.paddlewheelCondition) {
            "Perfect" -> "Sangat Baik"
            "Good" -> "Baik"
            "Bad" -> "Buruk"
            else -> "<kategori belum terbarui>"
        }
        when (currentItem.paddlewheelCondition) {
            "Perfect" -> holder.tvCondition.setTextColor(statusGreenColor)
            "Good" -> holder.tvCondition.setTextColor(statusYellowColor)
            "Bad" -> holder.tvCondition.setTextColor(statusRedColor)
            else -> holder.tvCondition.setTextColor(statusGreyColor)
        }

        holder.tvConnection.text = when (currentItem.signalStrength) {
            in 80..100 -> "Baik"
            in 0..79 -> "Inkonsisten"
            else -> "Abnormal"
        }
        when (currentItem.signalStrength) {
            in 80..100 -> holder.tvConnection.setTextColor(statusGreenColor)
            in 0..79 -> holder.tvConnection.setTextColor(statusYellowColor)
            else -> holder.tvConnection.setTextColor(statusGreyColor)
        }

        val isVisible: Boolean = currentItem.kincirViewIsVisible
        holder.clDetails.visibility = if (isVisible) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            currentItem.kincirViewIsVisible = !currentItem.kincirViewIsVisible
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = listKincir.size

    /*
    interface OnItemClickCallback{
        fun onItemClicked(data: KincirData)
    }*/

}