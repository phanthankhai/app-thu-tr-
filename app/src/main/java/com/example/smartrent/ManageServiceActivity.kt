package com.example.smartrent

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageServiceActivity : AppCompatActivity() {

    private var serviceId = -1 // -1 nghĩa là đang ở chế độ Thêm mới

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_service)

        val etServiceName = findViewById<EditText>(R.id.etServiceName)
        val etServicePrice = findViewById<EditText>(R.id.etServicePrice)
        val etServiceUnit = findViewById<EditText>(R.id.etServiceUnit)
        val etServiceDesc = findViewById<EditText>(R.id.etServiceDesc)
        val btnSaveService = findViewById<Button>(R.id.btnSaveService)

        // 1. KIỂM TRA CHẾ ĐỘ (THÊM hay SỬA)
        serviceId = intent.getIntExtra("SERVICE_ID", -1)
        if (serviceId != -1) {
            // Nếu có ID truyền sang -> Chế độ SỬA -> Đổ dữ liệu cũ lên form
            etServiceName.setText(intent.getStringExtra("SERVICE_NAME"))
            
            // Xử lý giá tiền để bỏ phần thập phân .0 nếu có
            val price = intent.getDoubleExtra("SERVICE_PRICE", 0.0)
            val priceStr = if (price % 1.0 == 0.0) price.toLong().toString() else price.toString()
            etServicePrice.setText(priceStr)
            
            etServiceUnit.setText(intent.getStringExtra("SERVICE_UNIT"))
            etServiceDesc.setText(intent.getStringExtra("SERVICE_DESC"))
            btnSaveService.text = "CẬP NHẬT DỊCH VỤ"
        }

        btnSaveService.setOnClickListener {
            val name = etServiceName.text.toString().trim()
            val priceStr = etServicePrice.text.toString().trim()
            val unit = etServiceUnit.text.toString().trim()
            val desc = etServiceDesc.text.toString().trim()

            if (name.isEmpty() || priceStr.isEmpty() || unit.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ Tên, Giá và Đơn vị tính!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val price = priceStr.toDoubleOrNull() ?: 0.0
            val request = ServiceRequest(name, price, unit, if (desc.isEmpty()) null else desc)
            val token = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE).getString("AUTH_TOKEN", "") ?: ""

            // 2. GỌI API TƯƠNG ỨNG
            if (serviceId == -1) {
                // ĐANG THÊM MỚI
                RetrofitClient.getInstance(this@ManageServiceActivity).createService(token, request).enqueue(object : Callback<ServiceCrudResponse> {
                    override fun onResponse(call: Call<ServiceCrudResponse>, response: Response<ServiceCrudResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@ManageServiceActivity, "Thêm thành công!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else { Toast.makeText(this@ManageServiceActivity, "Lỗi: Tên dịch vụ bị trùng!", Toast.LENGTH_SHORT).show() }
                    }
                    override fun onFailure(call: Call<ServiceCrudResponse>, t: Throwable) {
                        Toast.makeText(this@ManageServiceActivity, "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                // ĐANG CẬP NHẬT
                RetrofitClient.getInstance(this@ManageServiceActivity).updateService(token, serviceId, request).enqueue(object : Callback<ServiceCrudResponse> {
                    override fun onResponse(call: Call<ServiceCrudResponse>, response: Response<ServiceCrudResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@ManageServiceActivity, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else { Toast.makeText(this@ManageServiceActivity, "Lỗi cập nhật!", Toast.LENGTH_SHORT).show() }
                    }
                    override fun onFailure(call: Call<ServiceCrudResponse>, t: Throwable) {
                        Toast.makeText(this@ManageServiceActivity, "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}
