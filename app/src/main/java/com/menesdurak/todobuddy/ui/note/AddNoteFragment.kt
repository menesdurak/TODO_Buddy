package com.menesdurak.todobuddy.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.menesdurak.todobuddy.R
import com.menesdurak.todobuddy.databinding.FragmentAddNoteBinding
import com.menesdurak.todobuddy.databinding.FragmentHomeBinding
import com.menesdurak.todobuddy.databinding.FragmentNoteBinding
import com.menesdurak.todobuddy.model.Group
import com.menesdurak.todobuddy.model.Note

class AddNoteFragment : Fragment() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private var groupKey: String = "0"
    private lateinit var database: FirebaseDatabase
    private lateinit var noteRef: DatabaseReference
    private lateinit var notesList: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        val view = binding.root

        groupKey = arguments?.getString("key").toString()

        //CREATE DATABASE AND WRITE
        database = Firebase.database
        noteRef = database.getReference("groups").child(groupKey).child("notes")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: AddNoteFragmentArgs by navArgs()
        val key = args.key

        binding.btnAdd.setOnClickListener {
            val newNoteNote = binding.etNote.text.toString()
            val newNote = Note(newNoteNote)
            if (newNoteNote.isNotEmpty()) {
                notesList = arrayListOf()

                noteRef.get().addOnSuccessListener {
                    for (i in it.children) {
                        val note = i.getValue(Note::class.java)
                        println(note!!.note)
                    }
                }
                val push = noteRef.push()
                push.setValue(newNote)
//                println(notesList)
//                val action = AddNoteFragmentDirections.actionAddNoteFragmentToNoteFragment(key)
//                findNavController().navigate(action)
            }
        }
    }
}
