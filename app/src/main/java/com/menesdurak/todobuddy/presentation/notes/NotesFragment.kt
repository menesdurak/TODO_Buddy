package com.menesdurak.todobuddy.presentation.notes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.menesdurak.todobuddy.data.local.entity.Note
import com.menesdurak.todobuddy.databinding.FragmentNotesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesFragment : Fragment() {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private var groupReference = ""

    private val notes = mutableListOf<Note>()

    private lateinit var databaseReference: DatabaseReference

    private val noteAdapter: NoteAdapter by lazy { NoteAdapter(::onDoneClicked, ::onDeleteClicked) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val view = binding.root

        //Receiving arguments
        val args: NotesFragmentArgs by navArgs()
        groupReference = args.groupKey

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseReference =
            Firebase.database.reference.child("groups").child(groupReference).child("notes")

        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = noteAdapter
        }

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notes.clear()
                for (postSnapshot in snapshot.children) {
                    val note = convertDataSnapshotToNote(postSnapshot)
                    notes.add(note)
                }
                noteAdapter.updateNotesList(notes)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("12345", "loadPost:onCancelled", databaseError.toException())
            }
        }

        databaseReference.addValueEventListener(postListener)

        binding.btnAddNote.setOnClickListener {
            val action =
                NotesFragmentDirections.actionNotesFragmentToAddNoteFragment(groupReference)
            findNavController().navigate(action)
        }
    }

    private fun convertDataSnapshotToNote(dataSnapshot: DataSnapshot): Note {
        val isDrawn = dataSnapshot.child("drawn").getValue(Boolean::class.java)
        val note = dataSnapshot.child("note").getValue(String::class.java)
        val noteReference = dataSnapshot.child("noteReference").getValue(String::class.java)

        return Note(
            note = note!!,
            isDrawn = isDrawn!!,
            noteReference = noteReference!!
        )
    }

    private fun onDoneClicked(position: Int) {
//        noteAdapter.updateDrawnStatus(position)
//        val childUpdate = hashMapOf<String, Any>(
//            "/drawn" to (noteAdapter.getNoteDrawnStatus(position))
//        )
//        databaseReference.child(noteAdapter.getNoteReference(position)).updateChildren(childUpdate)
    }

    private fun onDeleteClicked(position: Int) {
        noteAdapter.removeNote(position)
        databaseReference.child(noteAdapter.getNoteReference(position)).removeValue()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}