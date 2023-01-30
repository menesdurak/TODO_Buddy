package com.menesdurak.todobuddy.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.menesdurak.todobuddy.R
import com.menesdurak.todobuddy.databinding.FragmentHomeBinding
import com.menesdurak.todobuddy.model.Group
import com.menesdurak.todobuddy.model.Note

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

        val database = Firebase.database
//        val myRef = database.getReference("message")
        val notesRef = database.getReference("groups")
        val note1 = Note("123")
        val note2 = Note("abc", true)
        val note3 = Note("1a2b")
        val notesList = arrayListOf(note1, note2)
        val notesList2 = arrayListOf(note1, note2, note3)

        val newRef = notesRef.push()
        val key = newRef.key
        Log.e("1234", key!!)
        val group = Group(notesList2, key)
        newRef.setValue(group)

//        notesRef.child("-NN0ywFBKK-wlE1-K9ac").removeValue()
//-------------------------------------------------------------------------------------
//        val userId = "1234"
//        val listId = "1212"
//        val userId2 = "12345"
//        val listId2 = "12121"

//        myRef.root.child(userId).child(listId).setValue("Test 1111")

//        myRef.setValue("Hello, World!")

//        myRef.child(userId).child(listId).addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val value = snapshot.getValue()
//                Log.d("value1", "Value is $value")
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.w("value2", "Failed to read value.", error.toException())
//            }
//
//        })
//-------------------------------------------------------------------------------------
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}