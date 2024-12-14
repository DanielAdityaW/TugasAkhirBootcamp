package com.bcafbootcamp.tugasakhirbootcampandroid

import android.app.Activity
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bcafbootcamp.tugasakhirbootcampandroid.Client.RetrofitClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ExcelUploadActivity : AppCompatActivity() {

    private lateinit var uploadButton: Button
    private lateinit var downloadButton: Button
    private lateinit var drawerLayout: DrawerLayout
    private var selectedFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excel_upload)

        uploadButton = findViewById(R.id.uploadButton)
        downloadButton = findViewById(R.id.downloadButton)
        drawerLayout = findViewById(R.id.drawerLayout)

        // Tombol menu untuk membuka/menutup drawer
        findViewById<ImageButton>(R.id.btnMenu).setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        // Navigasi drawer
        findViewById<TextView>(R.id.tvTable).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        findViewById<TextView>(R.id.tvPredictionHistory).setOnClickListener {
            val intent = Intent(this, PredictionHistoryActivity::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        findViewById<TextView>(R.id.tvUploadExcel).setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START) // Tetap di halaman ini
        }

        findViewById<TextView>(R.id.tvManualInput).setOnClickListener {
            val intent = Intent(this, ManualInputActivity::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        findViewById<TextView>(R.id.tvLogout).setOnClickListener {
            logoutFromBackend()
        }

        uploadButton.setOnClickListener {
            selectFile()
        }

        downloadButton.setOnClickListener {
            downloadPredictedFile()
        }
    }

    // Logout backend seperti di MainActivity
    private fun logoutFromBackend() {
        RetrofitClient.logoutApi.logout().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ExcelUploadActivity, "Logged out successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ExcelUploadActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@ExcelUploadActivity, "Logout failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@ExcelUploadActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun selectFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" // MIME type untuk .xlsx
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            selectedFileUri = data?.data
            val contentResolver = contentResolver
            val mimeType = selectedFileUri?.let { contentResolver.getType(it) }

            // Validasi tipe file
            if (mimeType == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") {
                selectedFileUri?.let { uploadExcelFile(it) }
            } else {
                Toast.makeText(this, "Invalid file type. Please select an .xlsx file.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun uploadExcelFile(fileUri: Uri) {
        try {
            // Buka InputStream dari URI
            val inputStream = contentResolver.openInputStream(fileUri) ?: return
            val tempFile = File.createTempFile("upload", ".xlsx", cacheDir)

            // Salin isi InputStream ke file sementara
            inputStream.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            // Siapkan RequestBody untuk Retrofit
            val mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".toMediaType()
            val requestFile = tempFile.asRequestBody(mediaType)
            val body = MultipartBody.Part.createFormData("file", tempFile.name, requestFile)

            // Panggil API Retrofit
            RetrofitClient.excelUploadApi.uploadExcel(body).enqueue(object : Callback<Map<String, String>> {
                override fun onResponse(
                    call: Call<Map<String, String>>,
                    response: Response<Map<String, String>>
                ) {
                    if (response.isSuccessful) {
                        val message = response.body()?.get("message")
                        Toast.makeText(this@ExcelUploadActivity, message, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(
                            this@ExcelUploadActivity,
                            "Upload failed: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                    Toast.makeText(this@ExcelUploadActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to upload file: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun downloadPredictedFile() {
        RetrofitClient.excelUploadApi.downloadProcessedFile().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()?.byteStream()
                    val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val file = File(downloadsFolder, "predicted_results.xlsx")

                    try {
                        body?.let {
                            file.outputStream().use { output ->
                                it.copyTo(output)
                            }

                            // Memindai file agar terlihat di aplikasi File Manager
                            MediaScannerConnection.scanFile(
                                this@ExcelUploadActivity,
                                arrayOf(file.absolutePath),
                                null
                            ) { path, uri ->
                                // Callback ketika file selesai dipindai
                                runOnUiThread {
                                    Toast.makeText(
                                        this@ExcelUploadActivity,
                                        "File downloaded to $path",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@ExcelUploadActivity,
                            "Failed to save file: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@ExcelUploadActivity,
                        "Download failed: ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@ExcelUploadActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

}
