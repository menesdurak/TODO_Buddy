package com.menesdurak.todobuddy.presentation.groups

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.menesdurak.todobuddy.data.local.entity.Group
import com.menesdurak.todobuddy.databinding.FragmentGroupsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupsFragment : Fragment() {
    private var _binding: FragmentGroupsBinding? = null
    private val binding get() = _binding!!

    private var email = ""

    private val groups = mutableListOf<Group>()

    private lateinit var auth: FirebaseAuth

    private lateinit var databaseReference: DatabaseReference

    private lateinit var postListener: ValueEventListener

    private val groupAdapter: GroupAdapter by lazy { GroupAdapter(::onItemClick) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGroupsBinding.inflate(inflater, container, false)
        val view = binding.root

        //Receiving arguments
        val args: GroupsFragmentArgs by navArgs()
        email = args.email

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseReference = Firebase.database.reference

        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = groupAdapter
        }

        postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                groups.clear()
                for (postSnapshot in snapshot.children) {
                    val group = convertDataSnapshotToGroup(postSnapshot)
                    if (email in group.buddysEmails) {
                        groups.add(group)
                    }

                }
                groupAdapter.updateGroupList(groups)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("12345", "loadPost:onCancelled", databaseError.toException())
            }
        }

        databaseReference.get().addOnSuccessListener {
            for (group in it.children) {
                databaseReference.child(group.key!!).addValueEventListener(postListener)
            }
        }

        binding.ivLogOut.setOnClickListener {
            Snackbar.make(it, "Do you want to sign out?", Snackbar.LENGTH_SHORT)
                .setAction("Yes") {
                    auth.signOut()
                    val action = GroupsFragmentDirections.actionGroupsFragmentToLoginFragment(email)
                    findNavController().navigate(action)
                }.show()
        }

        binding.btnAddGroup.setOnClickListener {
            val action = GroupsFragmentDirections.actionGroupsFragmentToAddGroupFragment(email)
            findNavController().navigate(action)
        }
    }

    private fun onItemClick(groupKey: String) {
        val action = GroupsFragmentDirections.actionGroupsFragmentToNotesFragment(groupKey)
        findNavController().navigate(action)
    }

    private fun convertDataSnapshotToGroup(dataSnapshot: DataSnapshot): Group {
        val name = dataSnapshot.child("name").getValue(String::class.java)
        val groupReference = dataSnapshot.child("groupReference").getValue(String::class.java)
        val buddysEmails = mutableListOf<String>()
        val buddysEmailsReference = dataSnapshot.child("buddysEmails")
        for (email in buddysEmailsReference.children) {
            buddysEmails.add(email.value as String)
        }

        return Group(
            name = name!!,
            groupReference = groupReference!!,
            buddysEmails = buddysEmails
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}