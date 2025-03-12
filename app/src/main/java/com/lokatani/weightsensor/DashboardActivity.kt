package com.lokatani.weightsensor

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lokatani.weightsensor.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            // Set user information with null checks
            val currentUser = auth.currentUser
            binding.userNameText.text = currentUser?.displayName ?:
                    SavedPreference.getUsername(this) ?:
                    "User"
            binding.userEmailText.text = currentUser?.email ?:
                    SavedPreference.getEmail(this) ?:
                    "No email"

            if (currentUser == null) {
                // If no user is signed in, return to MainActivity
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                return
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Dashboard initialization failed: ${e.message}", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.logout.setOnClickListener {
            // Sign out from Firebase
            auth.signOut()

            // Clear saved preferences
            SavedPreference.clearPreference(this)

            // Navigate back to login screen
            val intent = Intent(this, MainActivity::class.java)
            Toast.makeText(this, "Logging Out", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }
    }
}