package com.flowz.daggerexampleapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flowz.clarigojobtaskapp.adapter.DiffCallback
import com.flowz.daggerexampleapp.R
import com.flowz.daggerexampleapp.databinding.RvItemBinding
import com.flowz.daggerexampleapp.model.RecyclerData

//higher order function
//typealias urlListener = (url: String) -> Unit
typealias urlListener = (item: RecyclerData) -> Unit

class RecyclerViewAdapter (private val listner: urlListener)  : ListAdapter<RecyclerData, RecyclerViewAdapter.RecyclerViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecyclerViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)

        return RecyclerViewHolder(RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)){
//            listner(values[it])
            getItem(it)?.let { item-> listner(item) }
        }
    }


    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        val currentItem = getItem(position)

        holder.binding.apply {

            holder.itemView.apply {
                textviewName.text = "${currentItem?.name}"
                textviewDescription.text = "${currentItem?.description}"


                val imageurl = currentItem?.owner?.avatar_url

                Glide.with(imageView)
                    .load(imageurl)
                    .circleCrop()
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .error(R.drawable.ic_baseline_person_24)
                    .fallback(R.drawable.ic_baseline_person_24)
                    .into(imageView)
            }
        }
    }


    inner class RecyclerViewHolder(val binding: RvItemBinding, private val listner: (Int)-> Unit): RecyclerView.ViewHolder(binding.root){

        init {
            itemView.setOnClickListener {
                listner(adapterPosition)
            }

        }

//        fun bindView(gitItem: RecyclerData){
//
//            binding.apply {
//                listner(adapterPosition)
//            }
//
//            binding.textviewDescription.setOnClickListener {
//                listner(url)
//            }
//
//        }

    }




}