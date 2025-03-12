package com.lokatani.weightsensor

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth using Kotlin extension
        auth = Firebase.auth

        // Initialize UI elements
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)

        // Set click listeners
        buttonLogin.setOnClickListener { loginUser() }
        buttonRegister.setOnClickListener { registerUser() }
    }

    private fun loginUser() {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        // Validate inputs
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Show progress
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Logging in...")
        progressDialog.show()

        // Sign in with email and password
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    // Login success
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                    // Navigate to main activity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
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

    private fun registerUser() {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

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

        // Show progress
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Registering...")
        progressDialog.show()

        // Create user with email and password
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    // Registration success
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                    // Navigate to main activity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
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
        // Check if user is already signed in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is already logged in, redirect to main activity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}