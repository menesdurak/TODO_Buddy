package com.menesdurak.todobuddy.ui.note

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.menesdurak.todobuddy.R
import com.menesdurak.todobuddy.databinding.FragmentLoginBinding
import com.menesdurak.todobuddy.databinding.FragmentNoteBinding
import com.menesdurak.todobuddy.model.Group
import com.menesdurak.todobuddy.ui.adapter.HomeAdapter
import com.menesdurak.todobuddy.ui.adapter.NoteAdapter
import com.menesdurak.todobuddy.ui.home.HomeFragmentDirections

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

        val args: NoteFragmentArgs by navArgs()
        groupKey = args.groupKey

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

        binding.fbAddNote.setOnClickListener {
            findNavController().navigate(R.id.addNoteFragment)
        }
    }
}