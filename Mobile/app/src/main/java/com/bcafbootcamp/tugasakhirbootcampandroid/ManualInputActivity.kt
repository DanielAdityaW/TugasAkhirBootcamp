package com.bcafbootcamp.tugasakhirbootcampandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bcafbootcamp.tugasakhirbootcampandroid.Client.RetrofitClient
import com.bcafbootcamp.tugasakhirbootcampandroid.R
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManualInputActivity : AppCompatActivity() {

    private lateinit var creditScoreEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var tenureEditText: EditText
    private lateinit var balanceEditText: EditText
    private lateinit var vehicleTypeSpinner: Spinner
    private lateinit var installmentAmountEditText: EditText
    private lateinit var paymentHistoryEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var resultTextView: TextView
    private lateinit var errorTextView: TextView

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_input)

        drawerLayout = findViewById(R.id.drawerLayout)

        findViewById<TextView>(R.id.tvLogout).setOnClickListener {
            // Trigger logout from the backend
            logoutFromBackend()
        }

        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)
        btnMenu.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        findViewById<TextView>(R.id.tvTable).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START) // Close the drawer after item click
        }

        findViewById<TextView>(R.id.tvPredictionHistory).setOnClickListener {
            // Navigate to PredictionHistoryActivity
            val intent = Intent(this, PredictionHistoryActivity::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START) // Close the drawer after item click
        }

        findViewById<TextView>(R.id.tvExcelUpload).setOnClickListener {
            // Navigate to PredictionHistoryActivity
            val intent = Intent(this, ExcelUploadActivity::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START) // Close the drawer after item click
        }

        findViewById<TextView>(R.id.tvManualInput).setOnClickListener {
            // Navigate to PredictionHistoryActivity
            drawerLayout.closeDrawer(GravityCompat.START) // Close the drawer after item click
        }

        // Initialize views
        creditScoreEditText = findViewById(R.id.creditScoreEditText)
        ageEditText = findViewById(R.id.ageEditText)
        tenureEditText = findViewById(R.id.tenureEditText)
        balanceEditText = findViewById(R.id.balanceEditText)
        vehicleTypeSpinner = findViewById(R.id.vehicleTypeSpinner)
        installmentAmountEditText = findViewById(R.id.installmentAmountEditText)
        paymentHistoryEditText = findViewById(R.id.paymentHistoryEditText)
        submitButton = findViewById(R.id.submitButton)
        resultTextView = findViewById(R.id.resultTextView)
        errorTextView = findViewById(R.id.errorTextView)

        // Set up Spinner
        val vehicleTypes = arrayOf("Car", "Motorcycle")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vehicleTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        vehicleTypeSpinner.adapter = adapter

        // Set up submit button click listener
        submitButton.setOnClickListener {
            submitData()
        }
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
                    Toast.makeText(this@ManualInputActivity, "Logged out successfully!", Toast.LENGTH_SHORT).show()

                    // Redirect to login activity or clear session here
                    val intent = Intent(this@ManualInputActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish() // Optional: Close the current activity
                } else {
                    // Handle failed logout (server error, etc.)
                    Toast.makeText(this@ManualInputActivity, "Logout failed. Please try again.", Toast.LENGTH_SHORT).show()

                    // Log the error response code and message
                    Log.e("LogoutError", "Logout failed with response code: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle network failure
                Toast.makeText(this@ManualInputActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()

                // Log the error to the console for better debugging
                Log.e("LogoutError", "Logout failed: ${t.message}", t)  // Logs the message and stack trace
            }
        })
    }

    private fun submitData() {
        // Get input values
        val creditScore = creditScoreEditText.text.toString().toIntOrNull()
        val age = ageEditText.text.toString().toIntOrNull()
        val tenure = tenureEditText.text.toString().toIntOrNull()
        val balance = balanceEditText.text.toString().toDoubleOrNull()
        val vehicleType = vehicleTypeSpinner.selectedItem.toString()
        val installmentAmount = installmentAmountEditText.text.toString().toDoubleOrNull()
        val paymentHistory = paymentHistoryEditText.text.toString().toIntOrNull()

        // Check if all fields are valid
        if (creditScore == null || age == null || tenure == null || balance == null || installmentAmount == null || paymentHistory == null) {
            errorTextView.text = "Please fill all fields correctly."
            errorTextView.visibility = View.VISIBLE
            resultTextView.visibility = View.GONE
            return
        }

        // Prepare the input data in JSON format
        val inputData = JSONObject().apply {
            put("Credit Score", creditScore)
            put("Age", age)
            put("Tenure", tenure)
            put("Balance", balance)
            put("Vehicle Type", vehicleType)
            put("Installment Amount", installmentAmount)
            put("Payment History", paymentHistory)
        }

        // Send data to Flask API
        val url = "https://2398-103-165-222-114.ngrok-free.app/api/predict-manual"
        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, inputData,
            { response ->
                // Handle success response
                val predictedRisk = response.getString("predictedRisk")
                val predictedRiskScore = response.getDouble("predictedRiskScore")

                // Set result text and color based on risk
                resultTextView.text = "Risk Cluster: $predictedRisk\nRisk Score: $predictedRiskScore"
                resultTextView.visibility = View.VISIBLE
                errorTextView.visibility = View.GONE

                when (predictedRisk.lowercase()) {
                    "low" -> resultTextView.setTextColor(getColor(R.color.green))
                    "medium" -> resultTextView.setTextColor(getColor(R.color.yellow))
                    "high" -> resultTextView.setTextColor(getColor(R.color.red))
                    else -> resultTextView.setTextColor(getColor(android.R.color.black))
                }
            },
            { error ->
                // Handle error response
                errorTextView.text = "Failed to get prediction. Please try again."
                errorTextView.visibility = View.VISIBLE
                resultTextView.visibility = View.GONE
            })

        queue.add(jsonObjectRequest)
    }
}
