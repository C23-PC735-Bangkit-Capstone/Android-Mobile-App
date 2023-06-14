package com.example.tambakapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tambakapp.data.response.ResponseDeviceItem

class ListKincirAdapter(private val listKincir: List<ResponseDeviceItem>) : RecyclerView.Adapter<ListKincirAdapter.ListViewHolder>() {

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
        holder.tvBattery.text = currentItem.batteryStrength.toString() + "%"
        holder.tvCondition.text = when (currentItem.paddlewheelCondition) {
            "Perfect" -> {
                "Sangat Baik"
            }
            "Good" -> {
                "Baik"
            }
            "Bad" -> {
                "Buruk"
            }
            else -> {
                "<kategori belum terbarui>"
            }
        }
        holder.tvConnection.text = when (currentItem.signalStrength) {
            in 80..100 -> "Baik"
            in 0..79 -> "Inkonsisten"
            else -> "Abnormal"
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