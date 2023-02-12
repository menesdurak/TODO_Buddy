package com.menesdurak.todobuddy.ui.note

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.menesdurak.todobuddy.databinding.FragmentNoteBinding
import com.menesdurak.todobuddy.model.Group
import com.menesdurak.todobuddy.model.Note
import com.menesdurak.todobuddy.ui.adapter.NoteAdapter

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    private var groupKey: String = "0"
    private var userMail: String = "0"
    private lateinit var database: FirebaseDatabase
    private lateinit var noteRef: DatabaseReference
    private lateinit var notesList: ArrayList<Note>
    private lateinit var keyList: ArrayList<String>
    private lateinit var drawnList: ArrayList<Boolean>
    private lateinit var noteAdapter: NoteAdapter

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = Firebase.auth

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = "Notes"

        groupKey = arguments?.getString("key").toString()
        println(groupKey)
        userMail = auth.currentUser!!.email!!
        println(userMail)

        notesList = arrayListOf()
        keyList = arrayListOf()
        drawnList = arrayListOf()

        //CREATE DATABASE AND WRITE
        database = Firebase.database
        noteRef = database.getReference("groups").child(groupKey).child("notes")

        noteRef.get().addOnSuccessListener {
            for (i in it.children) {
                val note = i.getValue(Note::class.java)
                notesList.add(note!!)
                keyList.add(i.key!!)
            }
            println(keyList)
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            noteAdapter = NoteAdapter(notesList)
            binding.recyclerView.adapter = noteAdapter
            noteAdapter.setOnDeleteClickListener(object : NoteAdapter.NoteDeleteClickListener {
                override fun onDeleteClicked(position: Int) {
                    noteRef.child(keyList[position]).removeValue()
                    val action =
                        NoteFragmentDirections.actionNoteFragmentToHomeFragment(userMail)
                    findNavController().navigate(action)
                }
            })
            noteAdapter.setOnDoneClickListener(object : NoteAdapter.NoteDoneClickListener {
                override fun onDoneClicked(position: Int) {
                    val childUpdate = hashMapOf<String, Any>(
                        "/drawn" to !(notesList[position].drawn!!)
                    )
                    noteRef.child(keyList[position]).updateChildren(childUpdate)
                    val action =
                        NoteFragmentDirections.actionNoteFragmentToHomeFragment(userMail)
                    findNavController().navigate(action)
                }
            })
        }

        val args: NoteFragmentArgs by navArgs()
        val key = args.key

        binding.fbAddNote.setOnClickListener {
            val action = NoteFragmentDirections.actionNoteFragmentToAddNoteFragment(key)
            findNavController().navigate(action)
        }

        binding.btnDeleteGroup.setOnClickListener {
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setTitle("Delete Group")
                    setMessage("Do you want to delete this group?")
                    setPositiveButton("Yes",
                        DialogInterface.OnClickListener { _, _ ->
                            database.getReference("groups").child(groupKey).removeValue()
                            val action =
                                NoteFragmentDirections.actionNoteFragmentToHomeFragment(userMail)
                            findNavController().navigate(action)
                        })
                    setNegativeButton("No",
                        DialogInterface.OnClickListener { _, _ ->

                        })
                }
                builder.create()
            }
            alertDialog?.show()
        }
    }
}
