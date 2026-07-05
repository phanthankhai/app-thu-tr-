package com.example.smartrent

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateContractActivity : AppCompatActivity() {

    private var roomId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_contract)

        // Hứng ID phòng truyền từ màn hình chi tiết sang
        roomId = intent.getIntExtra("ROOM_ID", -1)

        val etTenantName = findViewById<EditText>(R.id.etTenantName)
        val etTenantPhone = findViewById<EditText>(R.id.etTenantPhone)
        val etTenantCccd = findViewById<EditText>(R.id.etTenantCccd)
        val etDeposit = findViewById<EditText>(R.id.etDeposit)
        val etStartDate = findViewById<EditText>(R.id.etStartDate)
        val etEndDate = findViewById<EditText>(R.id.etEndDate)
        val btnSaveContract = findViewById<Button>(R.id.btnSaveContract)

        btnSaveContract.setOnClickListener {
            val name = etTenantName.text.toString().trim()
            val phone = etTenantPhone.text.toString().trim()
            val cccd = etTenantCccd.text.toString().trim()
            val depositStr = etDeposit.text.toString().trim()
            val startDate = etStartDate.text.toString().trim()
            val endDateStr = etEndDate.text.toString().trim()

            // Kiểm tra dữ liệu bắt buộc không được trống
            if (name.isEmpty() || phone.isEmpty() || cccd.isEmpty() || startDate.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập Tên, SĐT, CCCD và Ngày bắt đầu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val deposit = if (depositStr.isEmpty()) 0.0 else depositStr.toDouble()
            val endDate = if (endDateStr.isEmpty()) null else endDateStr

            // Lấy token tự động trong két sắt SharedPreferences
            val sharedPreferences = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE)
            val token = sharedPreferences.getString("AUTH_TOKEN", "") ?: ""

            val request = ContractRequest(roomId, name, phone, cccd, deposit, startDate, endDate)

            Toast.makeText(this, "Đang thiết lập hợp đồng...", Toast.LENGTH_SHORT).show()

            // Triển khai gọi kết nối đến Laravel
            RetrofitClient.getInstance(this).createContract(token, request).enqueue(object : Callback<ContractResponse> {
                override fun onResponse(call: Call<ContractResponse>, response: Response<ContractResponse>) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        Toast.makeText(this@CreateContractActivity, "Lập hợp đồng thành công! Phòng đã được thuê.", Toast.LENGTH_LONG).show()
                        setResult(RESULT_OK) // Gửi tín hiệu về để màn hình chi tiết cập nhật lại trạng thái chữ
                        finish()
                    } else {
                        Toast.makeText(this@CreateContractActivity, "Thất bại: Phòng đã có người ở hoặc dữ liệu sai định dạng", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ContractResponse>, t: Throwable) {
                    Toast.makeText(this@CreateContractActivity, "Lỗi kết nối mạng: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
