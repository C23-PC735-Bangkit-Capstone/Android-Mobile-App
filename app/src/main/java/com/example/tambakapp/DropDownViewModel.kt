package com.example.tambakapp

import androidx.lifecycle.ViewModel
import com.example.tambakapp.data.response.ResponsePondItem

class DropDownViewModel : ViewModel() {
    private val itemList: MutableList<ResponsePondItem> = mutableListOf()
    private var isVisible: MutableList<Boolean> = mutableListOf()

    fun setItemList(list: List<ResponsePondItem>) {
        itemList.clear()
        itemList.addAll(list)
        isVisible.clear()
        for (i in 0 until itemList.size) {
            isVisible[i] = false
        }
    }

    /*
    fun toggleTambakViewVisibility(position: Int) {
        if (position in itemList.indices) {
            val item = itemList[position]
            item.kincirViewIsVisible = !item.kincirViewIsVisible
        }
    }

    fun getItemList() : List<TambakData> {
        return itemList
    }

     */
}