package com.bcafbootcamp.tugasakhirbootcampandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView  // New field for Register TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerTextView = findViewById(R.id.registerTextView)  // Initialize the TextView

        // Handle login button click
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginUser(username, password)
            } else {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle register link click
        registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(username: String, password: String) {
        val client = OkHttpClient()

        // Create JSON body
        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)

        val mediaType = "application/json".toMediaType()
        val body = RequestBody.create(mediaType, json.toString())

        val request = Request.Builder()
            .url("https://4ef1-103-165-222-114.ngrok-free.app/api/auth/login")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("LoginError", "Login failed", e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_SHORT).show()

                        // Navigate to MainActivity after successful login
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()  // Optional: Finish the LoginActivity so user can't go back
                    }
                } else {
                    runOnUiThread {
                        // Log the response body for debugging
                        val errorBody = response.body?.string() ?: "No error body"
                        Toast.makeText(applicationContext, "Login failed: $errorBody", Toast.LENGTH_SHORT).show()
                        Log.e("LoginError", "Error response: $errorBody")
                    }
                }
            }
        })
    }
}

