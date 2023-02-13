package com.menesdurak.todobuddy.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.menesdurak.todobuddy.databinding.FragmentHomeBinding
import com.menesdurak.todobuddy.model.Group
import com.menesdurak.todobuddy.model.Note
import com.menesdurak.todobuddy.ui.MainActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity).showActionBar()
    }

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

        activity?.title = "Note Titles"

        //CREATE DATABASE
        database = Firebase.database
        groupsRef = database.getReference("groups")

        titleList = arrayListOf()
        keyList = arrayListOf()

        val groupListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
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
            override fun onCancelled(error: DatabaseError) {
                Log.w("1234", "Something bad happened")
            }
        }
        groupsRef.addValueEventListener(groupListener)

        binding.fabtnAddGroup.setOnClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToAddGroupFragment(userEmail)
            findNavController().navigate(action)
        }
    }
}