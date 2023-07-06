package com.menesdurak.todobuddy.presentation.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.menesdurak.todobuddy.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d("12345", "Already signed!")
            goToGroupsFragment(auth.currentUser!!.email!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etEmail = binding.etEmail.text.toString()
        val etPassword = binding.etPassword.text.toString()

        binding.btnLogin.setOnClickListener {
            if (etEmail.isNotBlank() && etPassword.isNotBlank()) {
                signInUser(etEmail, etPassword)
                goToGroupsFragment(etEmail)
            }
        }

        binding.btnSignUp.setOnClickListener {
            if (etEmail.isNotBlank() && etPassword.isNotBlank()) {
                signUpUser(etEmail, etPassword)
                goToGroupsFragment(etEmail)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun signUpUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    goToGroupsFragment(email)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("12345", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    goToGroupsFragment(email)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("12345", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun signOutUser() {
        Firebase.auth.signOut()
        Log.d("12345", "sign out complete")
    }

    private fun goToGroupsFragment(email: String) {
        val action = LoginFragmentDirections.actionLoginFragmentToGroupsFragment(email)
        findNavController().navigate(action)
    }

}