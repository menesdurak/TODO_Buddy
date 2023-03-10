package com.menesdurak.todobuddy.ui.title

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.menesdurak.todobuddy.R
import com.menesdurak.todobuddy.databinding.FragmentAddGroupBinding
import com.menesdurak.todobuddy.databinding.FragmentAddNoteBinding
import com.menesdurak.todobuddy.model.Group
import com.menesdurak.todobuddy.model.Note
import com.menesdurak.todobuddy.ui.MainActivity
import com.menesdurak.todobuddy.ui.home.HomeFragmentArgs

class AddGroupFragment : Fragment() {

    private var _binding: FragmentAddGroupBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: FirebaseDatabase
    private lateinit var groupsRef: DatabaseReference
    private var userEmail: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity).showActionBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddGroupBinding.inflate(inflater, container, false)
        val view = binding.root

        //Get user email from Home fragment
        val args: HomeFragmentArgs by navArgs()
        userEmail = args.email

        database = Firebase.database
        groupsRef = database.getReference("groups")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = getString(com.menesdurak.todobuddy.R.string.add_note_title)

        val emptyNoteList = arrayListOf<Note>()
        val emptyUserList = arrayListOf<String>()
        val newGroupRef = groupsRef.push()
        val key = newGroupRef.key
        val newUserIdRef = groupsRef.child(key!!).child("userId").push()
        val newUserIdRef2 = groupsRef.child(key).child("userId").push()

        binding.btnCreate.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val buddyMail = binding.etBuddyMail.text.toString()
            if (title.isNotEmpty() && buddyMail.isNotEmpty()) {
                val newGroup = Group(title, emptyNoteList, emptyUserList, key)
                newGroupRef.setValue(newGroup)
                newUserIdRef.setValue(userEmail)
                newUserIdRef2.setValue(buddyMail)
                val action =
                    AddGroupFragmentDirections.actionAddGroupFragmentToHomeFragment(userEmail)
                findNavController().navigate(action)
            } else if (title.isEmpty()) {
                Toast.makeText(requireContext(), resources.getString(R.string.enter_a_group_title), Toast.LENGTH_SHORT).show()
            } else if (buddyMail.isEmpty()) {
                Toast.makeText(requireContext(), resources.getString(R.string.enter_your_buddy_s_e_mail), Toast.LENGTH_SHORT).show()
            }
        }
    }
}