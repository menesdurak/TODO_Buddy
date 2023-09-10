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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.menesdurak.todobuddy.data.local.entity.Group
import com.menesdurak.todobuddy.data.local.entity.GroupUi
import com.menesdurak.todobuddy.databinding.FragmentGroupsBinding

class GroupsFragment : Fragment() {
    private var _binding: FragmentGroupsBinding? = null
    private val binding get() = _binding!!

    private var email = ""

    private val groups = mutableListOf<GroupUi>()

    private lateinit var databaseReference: DatabaseReference

    private val groupAdapter: GroupAdapter by lazy { GroupAdapter(::onItemClick) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val group = convertDataSnapshotToGroupUi(postSnapshot)
                    groups.add(group)
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

        binding.btnAddGroup.setOnClickListener {
            val action = GroupsFragmentDirections.actionGroupsFragmentToAddGroupFragment(email)
            findNavController().navigate(action)
        }
    }

    private fun onItemClick(groupKey: String) {

    }

    private fun convertDataSnapshotToGroupUi(dataSnapshot: DataSnapshot): GroupUi {
        val name = dataSnapshot.child("name").getValue(String::class.java)
        val groupReference = dataSnapshot.child("groupReference").getValue(String::class.java)
        return GroupUi(
            name = name!!,
            groupReference = groupReference!!
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}