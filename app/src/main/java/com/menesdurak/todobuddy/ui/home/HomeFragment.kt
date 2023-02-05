package com.menesdurak.todobuddy.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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
    private lateinit var groupsRef: DatabaseReference
    private lateinit var titleList: ArrayList<String>
    private lateinit var keyList: ArrayList<String>
    private lateinit var homeAdapter: HomeAdapter
    private var userEmail: String = "0"
    private var isAllowedToAddTitle: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        //Get user email from login fragment
        val args: HomeFragmentArgs by navArgs()
        userEmail = args.email

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//-------------------------------------------------------------------------------
        //CREATE DATABASE AND WRITE
        database = Firebase.database
        groupsRef = database.getReference("groups")
        val notesRef2 = database.reference.child("path").child("111").child("222")
        val note1 = Note("123")
        val note2 = Note("abc", true)
        val note3 = Note("1a2b")
        val notesList = arrayListOf(note1, note2)
        val notesList2 = arrayListOf(note1, note2, note3)
        val emptyNoteList = arrayListOf<Note>()
        val newGroupRef = groupsRef.push()
        val newNoteRef = groupsRef.child("-NNVh0CtgesTkoNuUCRG").child("notes").push()
        val newUserIdRef = groupsRef.child("-NNVsUJdz6lwyj_uP2Sy").child("userId").push()
        val key = newGroupRef.key
//        Log.e("1234", key!!)
        val userList = arrayListOf("12bfa", "ab123")
        val emptyUserList = arrayListOf<String>()
        val newGroup = Group("Car", emptyNoteList, emptyUserList, key)
//        newGroupRef.setValue(newGroup)
        val newNote = Note("market arabasi")
//        newNoteRef.setValue(newNote)
//        newUserIdRef.setValue(userEmail)

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

        titleList = arrayListOf()
        keyList = arrayListOf()

        groupsRef.get().addOnSuccessListener {
            for (i in it.children) {
                for (j in i.children) {
                    for (k in j.children) {
                        if (k.value == userEmail) {
                            isAllowedToAddTitle = true
                        }
                    }
                    if (j.key == "z_title" && isAllowedToAddTitle) {
                        titleList.add(j.value.toString())
                        keyList.add(i.key.toString())
                        isAllowedToAddTitle = false
                    }
                }
            }
            binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
            homeAdapter = HomeAdapter(titleList)
            binding.recyclerView.adapter = homeAdapter
            homeAdapter.setOnItemClickListener(object : HomeAdapter.HomeListClickListener {
                override fun onItemClicked(position: Int) {
                    val action =
                        HomeFragmentDirections.actionHomeFragmentToNoteFragment(keyList[position])
                    findNavController().navigate(action)
                }
            })
        }
//*************************************************************************************************
//        notesRef.get().addOnSuccessListener {
//            for (i in it.children) {
//                val group = i.getValue(Group::class.java)
//                if (group != null) {
//                    for (j in group.userIds!!) {
//                        if (j == userEmail) {
//                            titleList.add(group.title!!)
//                            keyList.add(group.key!!)
//                        }
//                    }
//                }
//            }
//            binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
//            homeAdapter = HomeAdapter(titleList)
//            binding.recyclerView.adapter = homeAdapter
//            homeAdapter.setOnItemClickListener(object : HomeAdapter.HomeListClickListener {
//                override fun onItemClicked(position: Int) {
//                    val action = HomeFragmentDirections.actionHomeFragmentToNoteFragment(keyList[position])
//                    findNavController().navigate(action)
//                }
//            })
//        }
//*************************************************************************************************
//        groupsRef.get().addOnSuccessListener {
//            for (i in it.children) {
//                for (j in i.children) {
//                    if (j.key == "notes") {
//                        for (k in j.children) {
//                            for (l in k.children) {
//                                if (l.key == "note") {
//                                    println(l.value)
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
//            homeAdapter = HomeAdapter(titleList)
//            binding.recyclerView.adapter = homeAdapter
//            homeAdapter.setOnItemClickListener(object : HomeAdapter.HomeListClickListener {
//                override fun onItemClicked(position: Int) {
//                    val action = HomeFragmentDirections.actionHomeFragmentToNoteFragment(keyList[position])
//                    findNavController().navigate(action)
//                }
//            })
//        }
//*************************************************************************************************
    }
}