package com.menesdurak.todobuddy.presentation.groups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.menesdurak.todobuddy.R
import com.menesdurak.todobuddy.data.local.entity.Group
import com.menesdurak.todobuddy.databinding.FragmentAddGroupBinding
import com.menesdurak.todobuddy.databinding.FragmentGroupsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddGroupFragment : Fragment() {
    private var _binding: FragmentAddGroupBinding? = null
    private val binding get() = _binding!!

    private var email = ""

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddGroupBinding.inflate(inflater, container, false)
        val view = binding.root

        //Receiving arguments
        val args: AddGroupFragmentArgs by navArgs()
        email = args.email

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Firebase.database.reference

        binding.btnAddGroup.setOnClickListener {
            val groupName = binding.etGroupName.text.toString()
            val buddysEmail = binding.etBuddysEmail.text.toString()
            //Check edittext fields are empty or not
            val isEditTextBlank = groupName.isNotBlank() && buddysEmail.isNotBlank()
            //Check buddy email is valid or not
            val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(buddysEmail).matches()
            if (isEditTextBlank && isEmailValid) {
                addNewGroup(groupName, buddysEmail, database)
//                val action =
//                    AddGroupFragmentDirections.actionAddGroupFragmentToGroupsFragment(email)
//                findNavController().navigate(action)
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Enter valid values!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addNewGroup(
        groupName: String,
        buddysEmail: String,
        databaseReference: DatabaseReference,
    ) {
        val listOfEmails = listOf(email, buddysEmail)
        val groupKey = databaseReference.child("groups").push()
        val newGroup = Group(groupName, listOfEmails, groupReference = groupKey.key!!)
        println(groupKey.key)
        groupKey.setValue(newGroup)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}