package com.bcafbootcamp.tugasakhirbootcampandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bcafbootcamp.tugasakhirbootcampandroid.Adapter.PredictionHistoryAdapter
import com.bcafbootcamp.tugasakhirbootcampandroid.Client.RetrofitClient
import com.bcafbootcamp.tugasakhirbootcampandroid.Model.PredictionHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PredictionHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var predictionHistoryAdapter: PredictionHistoryAdapter
    private val predictionHistoryList = mutableListOf<PredictionHistory>()
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prediction_history)

        drawerLayout = findViewById(R.id.drawerLayout)

        findViewById<TextView>(R.id.tvLogout).setOnClickListener {
            // Trigger logout from the backend
            logoutFromBackend()
        }

        // Menu button click
        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)
        btnMenu.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        // Navigation item click listeners
        findViewById<TextView>(R.id.tvPredictionHistory).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        findViewById<TextView>(R.id.tvTable).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        findViewById<TextView>(R.id.tvUploadExcel).setOnClickListener {
            val intent = Intent(this, ExcelUploadActivity::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        findViewById<TextView>(R.id.tvManualInput).setOnClickListener {
            val intent = Intent(this, ManualInputActivity::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        // Initialize RecyclerView for displaying prediction history
        recyclerView = findViewById(R.id.recyclerViewPredictionHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)

        predictionHistoryAdapter = PredictionHistoryAdapter(predictionHistoryList)
        recyclerView.adapter = predictionHistoryAdapter

        // Load prediction history
        fetchPredictionHistory()
    }

    private fun logoutFromBackend() {
        // Show loading state or progress dialog if needed

        // Make the logout request
        RetrofitClient.logoutApi.logout().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Read the response as a string to log it
                    val responseString = response.body()?.string()
                    Log.d("LogoutSuccess", "Response: $responseString")

                    // Successfully logged out, show a toast or navigate to login screen
                    Toast.makeText(this@PredictionHistoryActivity, "Logged out successfully!", Toast.LENGTH_SHORT).show()

                    // Redirect to login activity or clear session here
                    val intent = Intent(this@PredictionHistoryActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish() // Optional: Close the current activity
                } else {
                    // Handle failed logout (server error, etc.)
                    Toast.makeText(this@PredictionHistoryActivity, "Logout failed. Please try again.", Toast.LENGTH_SHORT).show()

                    // Log the error response code and message
                    Log.e("LogoutError", "Logout failed with response code: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle network failure
                Toast.makeText(this@PredictionHistoryActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()

                // Log the error to the console for better debugging
                Log.e("LogoutError", "Logout failed: ${t.message}", t)  // Logs the message and stack trace
            }
        })
    }

    private fun fetchPredictionHistory() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = RetrofitClient.predictionHistoryApi.getPredictionHistory()
                predictionHistoryList.clear()
                predictionHistoryList.addAll(response)
                predictionHistoryAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(this@PredictionHistoryActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
