package com.menesdurak.todobuddy.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.menesdurak.todobuddy.R

class HomeAdapter(private val itemList: ArrayList<String>): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    private lateinit var mListener: HomeListClickListener
    class HomeViewHolder(itemView: View, listener: HomeListClickListener): RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                listener.onItemClicked(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.row_home, parent, false), mListener)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.tvRowTitle).text = itemList[position]
    }

    override fun getItemCount(): Int = itemList.size

    interface HomeListClickListener {
        fun onItemClicked(position: Int)
    }

    fun setOnItemClickListener(listener: HomeListClickListener) {
        mListener = listener
    }

}