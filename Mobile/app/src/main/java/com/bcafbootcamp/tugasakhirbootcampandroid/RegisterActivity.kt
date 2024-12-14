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

class RegisterActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginTextView: TextView  // New field for Login TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)
        loginTextView = findViewById(R.id.loginTextView)  // Initialize the Login TextView

        // Handle registration button click
        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                registerUser(username, password)
            } else {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle "Already have an account?" click
        loginTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()  // Optional: finish the RegisterActivity so the user can't go back to it
        }
    }

    private fun registerUser(username: String, password: String) {
        val client = OkHttpClient()

        // Create JSON body
        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)

        val mediaType = "application/json".toMediaType()
        val body = RequestBody.create(mediaType, json.toString())

        val request = Request.Builder()
            .url("https://4ef1-103-165-222-114.ngrok-free.app/api/auth/register")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Registration failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("RegisterError", "Registration failed", e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Registration successful", Toast.LENGTH_SHORT).show()
                        finish() // Close the RegisterActivity after success
                    }
                } else {
                    runOnUiThread {
                        // Log the response body for debugging
                        val errorBody = response.body?.string() ?: "No error body"
                        Toast.makeText(applicationContext, "Registration failed: $errorBody", Toast.LENGTH_SHORT).show()
                        Log.e("RegisterError", "Error response: $errorBody")
                    }
                }
            }
        })
    }
}
