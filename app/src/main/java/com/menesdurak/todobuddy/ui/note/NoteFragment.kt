package com.menesdurak.todobuddy.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.menesdurak.todobuddy.databinding.FragmentNoteBinding
import com.menesdurak.todobuddy.model.Group
import com.menesdurak.todobuddy.ui.adapter.NoteAdapter

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    private var groupKey: String = "0"
    private lateinit var database: FirebaseDatabase
    private lateinit var noteRef: DatabaseReference
    private lateinit var notesList: ArrayList<String>
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        val view = binding.root

        groupKey = arguments?.getString("key").toString()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notesList = arrayListOf()

        //CREATE DATABASE AND WRITE
        database = Firebase.database
        noteRef = database.getReference("groups").child(groupKey)

        noteRef.get().addOnSuccessListener {
            val group = it.getValue(Group::class.java)
            if (group != null) {
                for (i in group.notes!!) {
                    notesList.add(i.note!!)
                }
            }
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            noteAdapter = NoteAdapter(notesList)
            binding.recyclerView.adapter = noteAdapter
        }

        val args: NoteFragmentArgs by navArgs()
        val key = args.key

        binding.fbAddNote.setOnClickListener {
            val action = NoteFragmentDirections.actionNoteFragmentToAddNoteFragment(key)
            findNavController().navigate(action)
        }
    }
}