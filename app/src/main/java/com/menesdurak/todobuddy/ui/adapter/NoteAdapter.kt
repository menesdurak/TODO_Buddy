package com.menesdurak.todobuddy.ui.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.menesdurak.todobuddy.R
import com.menesdurak.todobuddy.model.Note

class NoteAdapter(private val list: ArrayList<Note>): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.row_note, parent, false))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val tv = holder.itemView.findViewById<TextView>(R.id.tvNote)
        tv.text = list[position].note
        //Draw line on text if drawn is true
        if (list[position].drawn == true) {
            tv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            tv.paintFlags
        }
    }

    override fun getItemCount(): Int = list.size
}