package com.example.tambakapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class ListKincirDetailAdapter(private val listKincir: ArrayList<KincirData>, private val context: Context) : RecyclerView.Adapter<ListKincirDetailAdapter.ListViewHolder>() {

    /*
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }*/

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_name_detail)
        val clDetails: ConstraintLayout = itemView.findViewById(R.id.cl_detail_details)
        val lcChart: LineChart = itemView.findViewById(R.id.lc_chart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_kincir_detail, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = listKincir[position]
        holder.tvName.text = currentItem.name

        // Chart
        with(holder.lcChart) {
            animateX(1200, com.github.mikephil.charting.animation.Easing.EaseInSine)
            description.isEnabled = false

            xAxis.setDrawGridLines(false)
            xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1F
            xAxis.valueFormatter = MyAxisFormatter()

            axisRight.isEnabled = false
            extraRightOffset = 30f

            legend.orientation = com.github.mikephil.charting.components.Legend.LegendOrientation.VERTICAL
            legend.verticalAlignment = com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.TOP
            legend.horizontalAlignment = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER
            legend.textSize = 15F
            legend.form = com.github.mikephil.charting.components.Legend.LegendForm.LINE
        }
        val entries = ArrayList<Entry>()
        entries.addAll(currentItem.chartEntries)
        val lastFiveEntries = ArrayList<Entry>()
        if (entries.size >= 5) {
            lastFiveEntries.addAll(entries.subList(entries.size - 5, entries.size))
        } else {
            lastFiveEntries.addAll(entries)
        }
        val weekOneSales = LineDataSet(lastFiveEntries, "Kinerja")
        weekOneSales.lineWidth = 3f
        weekOneSales.valueTextSize = 15f
        weekOneSales.mode = LineDataSet.Mode.LINEAR
        weekOneSales.color = ContextCompat.getColor(context, R.color.purple_500)
        weekOneSales.valueTextColor = ContextCompat.getColor(context, R.color.purple_500)
        weekOneSales.enableDashedLine(20F, 10F, 0F)
        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(weekOneSales)
        val lineData = LineData(dataSet)
        holder.lcChart.data = lineData
        holder.lcChart.invalidate()

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

class MyAxisFormatter : IndexAxisValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val days = value.toInt() + 1 // Assuming 0-based index
        return "Day $days"
    }
}