package com.bcafbootcamp.tugasakhirbootcampandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bcafbootcamp.tugasakhirbootcampandroid.Adapter.CustomerAdapter
import com.bcafbootcamp.tugasakhirbootcampandroid.Client.RetrofitClient
import com.bcafbootcamp.tugasakhirbootcampandroid.Model.Customer
import com.bcafbootcamp.tugasakhirbootcampandroid.Model.CustomerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.GravityCompat
import okhttp3.ResponseBody

class MainActivity : AppCompatActivity() {

    private lateinit var customerAdapter: CustomerAdapter
    private val customerList = mutableListOf<Customer>()

    private var currentPage = 0
    private val pageSize = 100
    private var isLoading = false
    private var isLastPage = false
    private var totalPages = 0

    private var currentVehicleType = "Car" // Default filter
    private var isDescendingOrder = true // Default sort order: descending

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            // Navigate to PredictionHistoryActivity
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
            val intent = Intent(this, ManualInputActivity::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START) // Close the drawer after item click
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        customerAdapter = CustomerAdapter(customerList)
        recyclerView.adapter = customerAdapter

        val btnFirst = findViewById<Button>(R.id.btnFirst)
        val btnPrevious = findViewById<Button>(R.id.btnPrevious)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val btnLast = findViewById<Button>(R.id.btnLast)
        val tvPageInfo = findViewById<TextView>(R.id.tvPageInfo)
        val spinnerVehicleType = findViewById<Spinner>(R.id.spinnerVehicleType)
        val btnSortRiskScore = findViewById<Button>(R.id.btnSortRiskScore)
        val etCustomerSearch = findViewById<EditText>(R.id.etCustomerSearch)
        val btnSearchCustomer = findViewById<Button>(R.id.btnSearchCustomer)

        // Spinner Filter Listener
        spinnerVehicleType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentVehicleType = when (position) {
                    1 -> "Motorcycle"
                    else -> "Car"
                }
                currentPage = 0
                customerList.clear()
                fetchCustomerData("")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Sort Button Listener
        btnSortRiskScore.setOnClickListener {
            isDescendingOrder = !isDescendingOrder
            btnSortRiskScore.text = if (isDescendingOrder) "Sort: Risk Score ↓" else "Sort: Risk Score ↑"
            currentPage = 0
            customerList.clear()
            fetchCustomerData("")
        }

        btnSearchCustomer.setOnClickListener {
            val searchQuery = etCustomerSearch.text.toString().trim()
            currentPage = 0
            customerList.clear()
            fetchCustomerData(searchQuery)
        }

        btnFirst.setOnClickListener { navigateToPage(0) }
        btnPrevious.setOnClickListener { navigateToPage(currentPage - 1) }
        btnNext.setOnClickListener { navigateToPage(currentPage + 1) }
        btnLast.setOnClickListener { navigateToPage(totalPages - 1) }

        fetchCustomerData("")
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
                    Toast.makeText(this@MainActivity, "Logged out successfully!", Toast.LENGTH_SHORT).show()

                    // Redirect to login activity or clear session here
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish() // Optional: Close the current activity
                } else {
                    // Handle failed logout (server error, etc.)
                    Toast.makeText(this@MainActivity, "Logout failed. Please try again.", Toast.LENGTH_SHORT).show()

                    // Log the error response code and message
                    Log.e("LogoutError", "Logout failed with response code: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle network failure
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()

                // Log the error to the console for better debugging
                Log.e("LogoutError", "Logout failed: ${t.message}", t)  // Logs the message and stack trace
            }
        })
    }


    private fun navigateToPage(page: Int) {
        if (page in 0 until totalPages) {
            currentPage = page
            customerList.clear()
            fetchCustomerData("")
        }
    }

    private fun fetchCustomerData(searchQuery : String) {
        if (isLoading) return
        isLoading = true

        val sortOrder = if (isDescendingOrder) "desc" else "asc"
        val vehicleType = if (currentVehicleType == "All") "" else currentVehicleType

        RetrofitClient.apiService.getCustomers(currentPage, pageSize, sortOrder, vehicleType, searchQuery)
            .enqueue(object : Callback<CustomerResponse> {
                override fun onResponse(call: Call<CustomerResponse>, response: Response<CustomerResponse>) {
                    if (response.isSuccessful) {
                        val customers = response.body()?.content ?: emptyList()
                        totalPages = response.body()?.totalPages ?: 1
                        isLastPage = currentPage + 1 >= totalPages

                        if (customers.isNotEmpty()) {
                            customerList.addAll(customers)
                            customerAdapter.notifyDataSetChanged()
                        }

                        findViewById<TextView>(R.id.tvPageInfo).text = "Page ${currentPage + 1} of $totalPages"
                        findViewById<Button>(R.id.btnFirst).isEnabled = currentPage > 0
                        findViewById<Button>(R.id.btnPrevious).isEnabled = currentPage > 0
                        findViewById<Button>(R.id.btnNext).isEnabled = !isLastPage
                        findViewById<Button>(R.id.btnLast).isEnabled = !isLastPage
                    } else {
                        Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                    }
                    isLoading = false
                }

                override fun onFailure(call: Call<CustomerResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    isLoading = false
                }
            })
    }
}
