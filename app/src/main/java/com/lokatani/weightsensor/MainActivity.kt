package com.lokatani.weightsensor

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lokatani.weightsensor.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        // Email/Password Login button click listener
        binding.buttonLogin.setOnClickListener {
            loginWithEmail()
        }

        // Email/Password Register button click listener
        binding.buttonRegister.setOnClickListener {
            registerWithEmail()
        }

        // Forgot password functionality (optional)
        binding.forgotPassword.setOnClickListener {
            val email = binding.editTextEmail.editText?.text.toString().trim()
            if (email.isNotEmpty()) {
                firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Failed to send reset email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Login with email and password
    private fun loginWithEmail() {
        val email = binding.editTextEmail.editText?.text.toString().trim() ?: ""
        val password = binding.editTextPassword.editText?.text.toString().trim() ?: ""

        // Validate inputs
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Show progress indicator
        binding.buttonLogin.isEnabled = false
        binding.buttonRegister.isEnabled = false

        // Sign in with email and password
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                // Enable buttons
                binding.buttonLogin.isEnabled = true
                binding.buttonRegister.isEnabled = true

                if (task.isSuccessful) {
                    // Login success
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                    // Get current user
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        // Save user info
                        SavedPreference.setEmail(this, user.email ?: "")
                        SavedPreference.setUsername(this, user.displayName ?: user.email ?: "User")

                        // Navigate to dashboard
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // If login fails, display a message to the user
                    Toast.makeText(
                        this,
                        "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    // Register with email and password
    private fun registerWithEmail() {
        val email = binding.editTextEmail.editText?.text.toString().trim() ?: ""
        val password = binding.editTextPassword.editText?.text.toString().trim() ?: ""

        // Validate inputs
        when {
            email.isEmpty() -> {
                Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
                return
            }
            password.isEmpty() -> {
                Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show()
                return
            }
            password.length < 6 -> {
                Toast.makeText(this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Show progress indicator
        binding.buttonLogin.isEnabled = false
        binding.buttonRegister.isEnabled = false

        // Create user with email and password
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                // Enable buttons
                binding.buttonLogin.isEnabled = true
                binding.buttonRegister.isEnabled = true

                if (task.isSuccessful) {
                    // Registration success
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                    // Get current user
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        // Save user info
                        SavedPreference.setEmail(this, user.email ?: "")
                        SavedPreference.setUsername(this, user.displayName ?: user.email ?: "User")

                        // Navigate to dashboard
                        val intent = Intent(this, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // If registration fails, display a message to the user
                    Toast.makeText(
                        this,
                        "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in with email
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            // User is already logged in with email, redirect to dashboard
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
    }
}