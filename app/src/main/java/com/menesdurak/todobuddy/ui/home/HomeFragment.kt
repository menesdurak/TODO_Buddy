package com.menesdurak.todobuddy.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.menesdurak.todobuddy.databinding.FragmentHomeBinding
import com.menesdurak.todobuddy.model.Group
import com.menesdurak.todobuddy.model.Note
import com.menesdurak.todobuddy.ui.adapter.HomeAdapter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: FirebaseDatabase
    private lateinit var notesRef: DatabaseReference
    private lateinit var titleList: ArrayList<String>
    private lateinit var homeAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//-------------------------------------------------------------------------------
        //CREATE DATABASE AND WRITE
        database = Firebase.database
        notesRef = database.getReference("groups")
        val notesRef2 = database.reference.child("path").child("111").child("222")
        val note1 = Note("123")
        val note2 = Note("abc", true)
        val note3 = Note("1a2b")
        val notesList = arrayListOf(note1, note2)
        val notesList2 = arrayListOf(note1, note2, note3)
        val newGroupRef = notesRef.push()
        val key = newGroupRef.key
//        Log.e("1234", key!!)
        val userList = arrayListOf("12abc", "ab123")
        val newGroup = Group("Home", notesList2, userList)
//        newGroupRef.setValue(newGroup)

//        //Creating new reference for note
//        val newRef = notesRef.push()
//        //Getting unique key of new reference
//        val key = newRef.key
//        Log.e("1234", key!!)
//        //Creating a new Group class
//        val group = Group(notesList2, key)
//        //Pushing new Group
//        newRef.setValue(group)

//------------------------------------------------------------------------------------
        //UPDATE
//        val newNote = Note("1aaaaa")
//        val newList = notesList.add(newNote)
//        val updateUsers = hashMapOf<String, Any>(
//            "userIds" to "abc123a"
//        )
//        userList.add("3456")
//        notesRef.child("-NNBp0ELFnsHt4fvLFWZ").updateChildren(updateUsers)
//        notesRef.child("-NNBshFWr-YjutIivAzn").child("userIds").setValue(userList)

//-------------------------------------------------------------------------------------
        //READ
//        notesRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for(i in snapshot.children) {
//                    val group = i.getValue(Group::class.java)
//                    if (group != null) {
//                        Log.e("******","******")
//                        Log.e("Key", "${i.key}")
//                        for (j in group.notes!!){
//                            Log.e("Note", j.note!!)
//                            Log.e("isDrawn", "${j.drawn}")
//                        }
//                        for(j in group.userIds!!) {
//                            Log.e("UserID", j)
//                        }
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.w("value2", "Failed to read value.", error.toException())
//            }
//
//        })
//-------------------------------------------------------------------------------------

        titleList = arrayListOf()

        notesRef.get().addOnSuccessListener {
            for (i in it.children) {
                val group = i.getValue(Group::class.java)
                if (group != null) {
                    for (j in group.userIds!!) {
                        if (j == "12a") {
                            titleList.add(group.title!!)
                        }
                    }
                }
            }
            binding.recyclerView.layoutManager = GridLayoutManager(context,2)
            homeAdapter = HomeAdapter(titleList)
            binding.recyclerView.adapter = homeAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}