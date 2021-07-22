package com.flowz.clarigojobtaskapp.adapter

import androidx.recyclerview.widget.DiffUtil
import com.flowz.daggerexampleapp.model.RecyclerData


class DiffCallback : DiffUtil.ItemCallback<RecyclerData>(){
    override fun areItemsTheSame(oldItem: RecyclerData, newItem: RecyclerData): Boolean {
        return oldItem.name == newItem.name
    }
    override fun areContentsTheSame(oldItem: RecyclerData, newItem: RecyclerData): Boolean {
        return oldItem == newItem
    }
}