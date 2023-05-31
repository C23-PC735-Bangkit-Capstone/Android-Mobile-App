package com.example.tambakapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListTambakAdapter(private val listTambak: ArrayList<TambakData>, private val listKincir: ArrayList<KincirData>, private val context: Context) : RecyclerView.Adapter<ListTambakAdapter.ListViewHolder>() {

    /*
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }*/

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTambakName: TextView = itemView.findViewById(R.id.tv_tambak_name)
        val clTambakDetails: ConstraintLayout = itemView.findViewById(R.id.cl_tambak_details)
        val rvKincir: RecyclerView = itemView.findViewById(R.id.rv_kincir)
        val btnCycle: Button = itemView.findViewById(R.id.btn_cycle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_tambak, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = listTambak[position]
        holder.tvTambakName.text = currentItem.name

        val isKincirVisible: Boolean = currentItem.kincirIsVisible
        holder.clTambakDetails.visibility = if (isKincirVisible) View.VISIBLE else View.GONE

        val listKincirSorted = ArrayList<KincirData>()
        for (kincir in listKincir) {
            if (kincir.tambakId == currentItem.id) {
                listKincirSorted.add(kincir)
            }
        }

        holder.rvKincir.layoutManager = LinearLayoutManager(context)
        val listKincirAdapter = ListKincirAdapter(listKincirSorted)
        holder.rvKincir.adapter = listKincirAdapter

        holder.itemView.setOnClickListener {
            currentItem.kincirIsVisible = !currentItem.kincirIsVisible
            notifyItemChanged(position)
        }

        holder.btnCycle.setOnClickListener {
            Toast.makeText(context, "Test mulai siklus ${currentItem.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = listTambak.size

    /*
    interface OnItemClickCallback{
        fun onItemClicked(data: KincirData)
    }*/

}