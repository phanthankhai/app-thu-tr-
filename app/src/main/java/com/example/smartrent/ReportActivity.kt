package com.example.smartrent

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val btnSendReport = findViewById<Button>(R.id.btnSendReport)

        btnSendReport.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ tiêu đề và mô tả!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // SỬA LỖI 1 Ở ĐÂY: Thêm "Bearer " vào trước token
            val savedToken =
                getSharedPreferences("SmartRentPrefs", MODE_PRIVATE).getString("AUTH_TOKEN", "")
                    ?: ""
            if (savedToken.isEmpty()) {
                Toast.makeText(
                    this,
                    "Phiên làm việc hết hạn, vui lòng đăng nhập lại!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val token = "Bearer $savedToken" // <--- Rất quan trọng

            val report = mapOf("title" to title, "description" to description)

            Toast.makeText(this, "Đang gửi báo cáo...", Toast.LENGTH_SHORT).show()

            RetrofitClient.getInstance(this).sendReport(token, report)
                .enqueue(object : Callback<Any> {
                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@ReportActivity,
                                "Gửi báo cáo thành công!",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        } else {
                            // Đã thêm Log để báo chính xác lỗi do đâu (sai URL, lỗi 403, hay lỗi 500)
                            val errorMsg = response.errorBody()?.string()
                            android.util.Log.e("API_ERROR", "Lỗi: ${response.code()} - $errorMsg")

                            Toast.makeText(
                                this@ReportActivity,
                                "Lỗi ${response.code()}: Không thể gửi",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        android.util.Log.e("API_ERROR", "Mất mạng: ${t.message}")
                        Toast.makeText(this@ReportActivity, "Lỗi kết nối mạng", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        }
    }
}
