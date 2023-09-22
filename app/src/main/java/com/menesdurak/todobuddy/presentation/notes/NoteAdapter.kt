package com.menesdurak.todobuddy.presentation.notes

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.menesdurak.todobuddy.data.local.entity.Note
import com.menesdurak.todobuddy.databinding.ItemNoteBinding

class NoteAdapter(
    private val onDoneClicked: (Int, String, Boolean) -> Unit,
    private val onDeleteClicked: (Int, String) -> Unit,
) : RecyclerView.Adapter<NoteAdapter.NoteHolder>() {

    private val notes = mutableListOf<Note>()

    inner class NoteHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.tvNote.text = note.note
            if (note.isDrawn) {
                binding.tvNote.paintFlags = binding.tvNote.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.tvNote.paintFlags = binding.tvNote.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
            binding.ivDone.setOnClickListener {
                onDoneClicked.invoke((adapterPosition),note.noteReference, note.isDrawn)
            }
            binding.ivDelete.setOnClickListener {
                onDeleteClicked.invoke(adapterPosition, note.noteReference)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val bind = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteHolder(bind)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.bind(notes[position])
    }

    fun updateNotesList(newNotes: List<Note>) {
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }

    fun updateDrawnStatus(position: Int) {
        notes[position].isDrawn = !notes[position].isDrawn
        notifyItemChanged(position)
    }

    fun removeNote(position: Int) {
        notes.removeAt(position)
        notifyDataSetChanged()
    }
}