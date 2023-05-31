package com.example.tambakapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class ListTambakDetailAdapter(private val listTambak: ArrayList<TambakData>, private val listKincir: ArrayList<KincirData>, private val context: Context) : RecyclerView.Adapter<ListTambakDetailAdapter.ListViewHolder>() {


    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTambakName: TextView = itemView.findViewById(R.id.tv_tambak_detail_name)
        val clTambakDetails: ConstraintLayout = itemView.findViewById(R.id.cl_tambak_detail_details)
        val rvKincir: RecyclerView = itemView.findViewById(R.id.rv_kincir_detail)
        val cvTambakStatus: MaterialCardView = itemView.findViewById(R.id.cv_tambak_status)
        val tvTambakStatus: TextView = itemView.findViewById(R.id.tv_tambak_status)
        val tvCycleCounter: TextView = itemView.findViewById(R.id.tv_cycle_counter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_tambak_detail, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = listTambak[position]
        holder.tvTambakName.text = currentItem.name
        holder.tvCycleCounter.text = "Hari ke-" + currentItem.cycleCount.toString()
        holder.tvTambakStatus.text = currentItem.status.toString()

        val statusColorGreen = ContextCompat.getColor(context, R.color.status_green)
        val statusColorYellow = ContextCompat.getColor(context, R.color.status_yellow)
        val statusColorRed = ContextCompat.getColor(context, R.color.status_red)

        if (currentItem.status >= 70) {
            holder.cvTambakStatus.setCardBackgroundColor(statusColorGreen)
        } else if (currentItem.status >= 40) {
            holder.cvTambakStatus.setCardBackgroundColor(statusColorYellow)
        } else {
            holder.cvTambakStatus.setCardBackgroundColor(statusColorRed)
        }

        val isKincirVisible: Boolean = currentItem.kincirIsVisible
        holder.clTambakDetails.visibility = if (isKincirVisible) View.VISIBLE else View.GONE

        val listKincirSorted = ArrayList<KincirData>()
        for (kincir in listKincir) {
            if (kincir.tambakId == currentItem.id) {
                listKincirSorted.add(kincir)
            }
        }

        holder.rvKincir.layoutManager = LinearLayoutManager(context)
        val listKincirAdapter = ListKincirDetailAdapter(listKincirSorted, context)
        holder.rvKincir.adapter = listKincirAdapter

        holder.itemView.setOnClickListener {
            currentItem.kincirIsVisible = !currentItem.kincirIsVisible
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = listTambak.size

}