package com.menesdurak.todobuddy.presentation.notes

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
import com.menesdurak.todobuddy.data.local.entity.Note
import com.menesdurak.todobuddy.databinding.FragmentAddNoteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNoteFragment : Fragment() {
    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private var groupReference = ""

    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        val view = binding.root

        //Receiving arguments
        val args: NotesFragmentArgs by navArgs()
        groupReference = args.groupKey

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseReference = Firebase.database.reference.child("groups").child(groupReference).child("notes")

        binding.btnAddNote.setOnClickListener {
            val noteText = binding.etNote.text.toString()

            if (noteText.isNotBlank()) {
                val noteKey =
                    databaseReference.push()
                val note = Note(note = noteText, noteReference = noteKey.key!!)
                databaseReference.child(noteKey.key!!).setValue(note)
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Please write a note.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}