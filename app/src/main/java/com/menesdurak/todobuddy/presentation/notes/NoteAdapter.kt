package com.menesdurak.todobuddy.presentation.notes

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.menesdurak.todobuddy.data.local.entity.Note
import com.menesdurak.todobuddy.databinding.ItemNoteBinding

class NoteAdapter(
    private val onDoneClicked: (Int) -> Unit,
    private val onDeleteClicked: (Int) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteHolder>() {

    private val notes = mutableListOf<Note>()

    inner class NoteHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.tvNote.text = note.note
            if (note.isDrawn) {
                binding.tvNote.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.tvNote.paintFlags
            }
            binding.ivDone.setOnClickListener {
                onDoneClicked.invoke(adapterPosition)
            }
            binding.ivDelete.setOnClickListener {
                onDeleteClicked.invoke(adapterPosition-1)
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

    fun getNoteReference(position: Int): String = notes[position].noteReference

    fun getNoteDrawnStatus(position: Int): Boolean = notes[position].isDrawn

    fun removeNote(position: Int) {
        notes.removeAt(position)
        notifyDataSetChanged()
    }
}