package com.example.smartrent

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class AdminCreateBillActivity : AppCompatActivity() {

    private var roomId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_create_bill)

        // 1. Lấy ID phòng được truyền từ màn hình Chi tiết phòng sang
        roomId = intent.getIntExtra("ROOM_ID", -1)
        val roomName = intent.getStringExtra("ROOM_NAME") ?: ""

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        tvTitle.text = "LẬP HÓA ĐƠN - PHÒNG $roomName"

        val etBillingMonth = findViewById<EditText>(R.id.etBillingMonth)
        val etOldElectric = findViewById<EditText>(R.id.etOldElectric)
        val etNewElectric = findViewById<EditText>(R.id.etNewElectric)
        val etOldWater = findViewById<EditText>(R.id.etOldWater)
        val etNewWater = findViewById<EditText>(R.id.etNewWater)
        val btnSubmitBill = findViewById<Button>(R.id.btnSubmitBill)

        btnSubmitBill.setOnClickListener {
            val month = etBillingMonth.text.toString().trim()
            val oldElecStr = etOldElectric.text.toString().trim()
            val newElecStr = etNewElectric.text.toString().trim()
            val oldWaterStr = etOldWater.text.toString().trim()
            val newWaterStr = etNewWater.text.toString().trim()

            // Kiểm tra rỗng dữ liệu
            if (month.isEmpty() || oldElecStr.isEmpty() || newElecStr.isEmpty() || oldWaterStr.isEmpty() || newWaterStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ toàn bộ thông số!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val oldElec = oldElecStr.toInt()
            val newElec = newElecStr.toInt()
            val oldWater = oldWaterStr.toInt()
            val newWater = newWaterStr.toInt()

            // Chặn lỗi logic ngay trên App trước khi gửi lên Server
            if (newElec < oldElec) {
                etNewElectric.error = "Số điện mới không được nhỏ hơn số cũ!"
                return@setOnClickListener
            }
            if (newWater < oldWater) {
                etNewWater.error = "Số nước mới không được nhỏ hơn số cũ!"
                return@setOnClickListener
            }

            // Lấy Token và cấu hình định dạng Bearer
            val savedToken = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE).getString("AUTH_TOKEN", "") ?: ""
            val token = if (savedToken.startsWith("Bearer ")) savedToken else "Bearer $savedToken"

            // Đóng gói JSON object request
            val billRequest = BillRequest(
                room_id = roomId,
                billing_month = month,
                old_electric = oldElec,
                new_electric = newElec,
                old_water = oldWater,
                new_water = newWater
            )

            btnSubmitBill.isEnabled = false
            Toast.makeText(this, "Đang xử lý hóa đơn...", Toast.LENGTH_SHORT).show()

            // Gọi API lưu dữ liệu
            RetrofitClient.getInstance(this).createBill(token, billRequest)
                .enqueue(object : Callback<BillResponse> {
                    override fun onResponse(call: Call<BillResponse>, response: Response<BillResponse>) {
                        btnSubmitBill.isEnabled = true
                        if (response.isSuccessful && response.body()?.success == true) {
                            val data = response.body()?.data
                            Log.d("DEBUG_FE", "Tiền dịch vụ nhận được: " + data?.services_cost)
                            
                            val total = data?.total_bill ?: 0.0
                            val formattedTotal = String.format(Locale.getDefault(), "%,.0f VNĐ", total)
                            
                            Toast.makeText(this@AdminCreateBillActivity, "Tạo thành công! Tổng bill: $formattedTotal", Toast.LENGTH_LONG).show()
                            finish() // Đóng màn hình, quay về danh sách phòng
                        } else {
                            Toast.makeText(this@AdminCreateBillActivity, "Lỗi: Không thể khởi tạo hóa đơn", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<BillResponse>, t: Throwable) {
                        btnSubmitBill.isEnabled = true
                        Toast.makeText(this@AdminCreateBillActivity, "Lỗi kết nối mạng viễn thông", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
