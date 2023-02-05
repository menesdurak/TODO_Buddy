package com.menesdurak.todobuddy.ui.title

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.menesdurak.todobuddy.R
import com.menesdurak.todobuddy.databinding.FragmentAddGroupBinding
import com.menesdurak.todobuddy.databinding.FragmentAddNoteBinding

class AddGroupFragment : Fragment() {

    private var _binding: FragmentAddGroupBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: FirebaseDatabase
    private lateinit var groupsRef: DatabaseReference
    private var userEmail: String = "0"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddGroupBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }
}