package com.example.tambakapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class ListKincirAdapter(private val listKincir: ArrayList<KincirData>) : RecyclerView.Adapter<ListKincirAdapter.ListViewHolder>() {

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
        holder.tvName.text = currentItem.name
        holder.tvBattery.text = currentItem.battery.toString() + "%"
        holder.tvCondition.text = currentItem.condition
        holder.tvConnection.text = currentItem.connection

        val isVisible: Boolean = currentItem.viewIsVisible
        holder.clDetails.visibility = if (isVisible) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            currentItem.viewIsVisible = !currentItem.viewIsVisible
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = listKincir.size

    /*
    interface OnItemClickCallback{
        fun onItemClicked(data: KincirData)
    }*/

}