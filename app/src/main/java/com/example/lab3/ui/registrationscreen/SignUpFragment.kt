package com.example.lab3.ui.registrationscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lab3.shared.model.GameViewModel
import com.example.lab3.R
import com.example.lab3.databinding.FragmentSignupBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().title = "Sign up"
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_signupFragment_to_registrationFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tfEmail.setOnClickListener {
            binding.tfEmail.isErrorEnabled = false
        }

        binding.tfPassword.setOnClickListener {
            binding.tfPassword.isErrorEnabled = false
        }
//
        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty()) {
                binding.tfEmail.error = "Enter an email!"
                binding.tfEmail.isErrorEnabled = true
                return@setOnClickListener
            }

//            if (!viewModel.isEmailValid(email)) {
//                binding.tfEmail.error = "Invalid email!"
//                binding.tfEmail.isErrorEnabled = true
//                return@setOnClickListener
//            }

            if (password.isEmpty()) {
                binding.tfPassword.error = "Enter a password!"
                binding.tfPassword.isErrorEnabled = true
                return@setOnClickListener
            }

//            if (!viewModel.isUsernameValid(password)) {
//                binding.tfUsername.error = "Username must be 3-16 characters!"
//                binding.tfUsername.isErrorEnabled = true
//                return@setOnClickListener
//            }


            signupUser(email, password)

//                binding.tfUsername.isErrorEnabled = false
//                binding.tfEmail.isErrorEnabled = false
//                viewModel.setUsername(username)
//                viewModel.setEmail(email)
//            findNavController().navigate(R.id.action_signupFragment_to_gameMenuFragment)
        }

        binding.btnLogIn.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_logInFragment)
        }
    }

    private fun signupUser(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                viewModel.auth.createUserWithEmailAndPassword(email, password).await()
                withContext(Dispatchers.Main) {
                    checkLoggedInState(email)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkLoggedInState(email: String) {
        if (viewModel.auth.currentUser == null) {
            Toast.makeText(requireContext(), "You are not logged in", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "You are logged in!", Toast.LENGTH_SHORT).show()
            viewModel.setEmail(email)
            findNavController().navigate(R.id.action_signupFragment_to_gameMenuFragment)
        }
    }
}