package com.menesdurak.todobuddy.presentation.notes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.menesdurak.todobuddy.data.local.entity.Note
import com.menesdurak.todobuddy.databinding.FragmentNotesBinding

class NotesFragment : Fragment() {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private var email = ""
    private var groupReference = ""

    private val notes = mutableListOf<Note>()

    private lateinit var databaseReference: DatabaseReference

    private val noteAdapter: NoteAdapter by lazy { NoteAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val view = binding.root

        //Receiving arguments
        val args: NotesFragmentArgs by navArgs()
        email = args.email
        groupReference = args.groupKey

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseReference = Firebase.database.reference.child("groups").child(groupReference).child("notes")

//        val note = Note("Abcd", true)
//        val noteKey = databaseReference.child("groups").child(groupReference).child("notes").push()
//        databaseReference.child("groups").child(groupReference).child("notes").child(noteKey.key!!).setValue(note)

        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = noteAdapter
        }

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
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

        databaseReference.get().addOnSuccessListener {
            for (note in it.children) {
                println(note.value)
                databaseReference.child(note.key!!).addValueEventListener(postListener)
            }
        }
    }

    private fun convertDataSnapshotToNote(dataSnapshot: DataSnapshot): Note {
        val isDrawn = dataSnapshot.child("drawn").getValue(Boolean::class.java)
        val note = dataSnapshot.child("note").getValue(String::class.java)

        return Note(
            note = note!!,
            isDrawn = isDrawn!!
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}