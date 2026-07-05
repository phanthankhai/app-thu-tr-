package com.example.smartrent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TenantDashboardActivity : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var tvRoomName: TextView
    private lateinit var tvRoomPrice: TextView
    private lateinit var tvBillAmount: TextView
    private lateinit var btnPayMoMo: Button
    private lateinit var btnSplitBill: Button
    private lateinit var btnReportIncident: Button
    private lateinit var btnLogout: ImageView
    
    private var token: String = ""
    private var currentData: TenantDashboardData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tenant_dashboard)

        // 1. Ánh xạ View
        tvWelcome = findViewById(R.id.tvWelcome)
        tvRoomName = findViewById(R.id.tvRoomName)
        tvRoomPrice = findViewById(R.id.tvRoomPrice)
        tvBillAmount = findViewById(R.id.tvBillAmount)
        btnPayMoMo = findViewById(R.id.btnPayMoMo)
        btnSplitBill = findViewById(R.id.btnSplitBill)
        btnReportIncident = findViewById(R.id.btnReportIncident)
        btnLogout = findViewById(R.id.btnLogout)

        // 2. Lấy Token
        val sharedPrefs = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE)
        token = "Bearer " + sharedPrefs.getString("AUTH_TOKEN", "")

        // 3. Sự kiện các nút bấm
        btnLogout.setOnClickListener {
            sharedPrefs.edit().remove("AUTH_TOKEN").apply()
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }

        btnReportIncident.setOnClickListener {
            startActivity(Intent(this, ReportActivity::class.java))
        }

        btnSplitBill.setOnClickListener {
            val data = currentData
            if (data != null) {
                val intent = Intent(this, SplitBillActivity::class.java)
                
                // Truyền billId sang
                intent.putExtra("BILL_ID", data.current_bill?.id ?: -1)

                // Truyền tiền phòng và tổng hóa đơn
                val rent = data.room.price.toDoubleOrNull() ?: 0.0
                val totalBill = data.current_bill?.total_amount?.toDoubleOrNull() ?: 0.0
                val utilities = if (totalBill > rent) totalBill - rent else 0.0
                
                intent.putExtra("TOTAL_RENT", rent)
                intent.putExtra("TOTAL_UTILITIES", utilities)
                
                // Chuyển đổi danh sách UserData sang Roommate
                val members = ArrayList<Roommate>()
                // Thêm bản thân người dùng vào danh sách
                members.add(Roommate(0, data.user.name, true))
                
                // Thêm bạn cùng phòng
                data.roommates?.forEach { roommate ->
                    members.add(Roommate(0, roommate.name, true))
                }
                
                intent.putExtra("ROOM_MEMBERS", members)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Vui lòng đợi tải dữ liệu...", Toast.LENGTH_SHORT).show()
            }
        }

        btnPayMoMo.setOnClickListener {
            Toast.makeText(this, "Đang mở ứng dụng thanh toán...", Toast.LENGTH_SHORT).show()
        }

        // 4. Gọi API tải dữ liệu
        loadDashboardData()
    }

    private fun loadDashboardData() {
        RetrofitClient.getInstance(this).getMyDashboard(token).enqueue(object : Callback<TenantDashboardResponse> {
            override fun onResponse(call: Call<TenantDashboardResponse>, response: Response<TenantDashboardResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()?.data
                    currentData = data
                    
                    if (data != null) {
                        tvWelcome.text = "Xin chào, ${data.user.name}!"
                        tvRoomName.text = "Phòng: ${data.room.name}"
                        tvRoomPrice.text = "Giá thuê: ${data.room.price} VNĐ"

                        if (data.current_bill != null) {
                            tvBillAmount.text = "${data.current_bill.total_amount} VNĐ"
                            tvBillAmount.setTextColor(ContextCompat.getColor(this@TenantDashboardActivity, android.R.color.holo_red_dark))
                            btnPayMoMo.visibility = View.VISIBLE
                        } else {
                            tvBillAmount.text = "Đã thanh toán đủ"
                            tvBillAmount.setTextColor(ContextCompat.getColor(this@TenantDashboardActivity, android.R.color.holo_green_dark))
                            btnPayMoMo.visibility = View.GONE
                        }
                    }
                }
            }

            override fun onFailure(call: Call<TenantDashboardResponse>, t: Throwable) {
                Log.e("API_ERROR", "Lỗi: ${t.message}")
                Toast.makeText(this@TenantDashboardActivity, "Không thể tải dữ liệu", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
