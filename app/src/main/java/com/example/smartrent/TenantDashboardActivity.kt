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
import com.google.gson.Gson
import java.util.Locale
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TenantDashboardActivity : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var tvRoomName: TextView
    private lateinit var tvRoomPrice: TextView
    private lateinit var tvBillStatus: TextView
    private lateinit var btnPayMoMo: Button
    private lateinit var btnSplitBill: Button
    private lateinit var btnReportIncident: Button
    private lateinit var btnLogout: ImageView
    
    private var token: String = ""
    private var currentRoom: Room? = null
    private var currentBill: BillData? = null
    private var myUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tenant_dashboard)

        // 1. Ánh xạ View
        tvWelcome = findViewById(R.id.tvWelcome)
        tvRoomName = findViewById(R.id.tvRoomName)
        tvRoomPrice = findViewById(R.id.tvRoomPrice)
        tvBillStatus = findViewById(R.id.tvBillAmount) // Dùng chung id với tvBillAmount cũ
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
            if (currentBill == null || currentRoom == null) return@setOnClickListener
            
            val intent = Intent(this, SplitBillActivity::class.java)
            
            intent.putExtra("BILL_ID", currentBill!!.id)
            intent.putExtra("MY_PAYMENT_STATUS", currentBill!!.my_payment_status)
            intent.putExtra("TOTAL_RENT", currentRoom!!.price)
            
            // Tính toán tiền điện nước dựa trên chỉ số chốt
            val tienDien = ((currentBill!!.new_electric ?: 0) - (currentBill!!.old_electric ?: 0)) * 3500.0
            val tienNuoc = ((currentBill!!.new_water ?: 0) - (currentBill!!.old_water ?: 0)) * 15000.0
            
            intent.putExtra("TOTAL_ELECTRIC", tienDien)
            intent.putExtra("TOTAL_WATER", tienNuoc)

            // Đóng gói danh sách thành viên gửi qua
            val usersList = currentRoom!!.users ?: emptyList()
            // Map User model sang Roommate model để tương thích SplitBillActivity
            val roommateList = ArrayList<Roommate>()
            for (user in usersList) {
                roommateList.add(Roommate(user.id, user.name))
            }
            
            intent.putExtra("TENANTS_LIST", Gson().toJson(roommateList))
            
            // THÊM DÒNG NÀY: Truyền ID của chính mình sang để lọc tiền
            intent.putExtra("MY_USER_ID", myUserId)

            startActivity(intent)
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
                    val dashboardData = response.body()?.data

                    if (dashboardData != null) {
                        currentRoom = dashboardData.room
                        currentBill = dashboardData.bill
                        myUserId = dashboardData.my_info.id

                        tvWelcome.text = "Xin chào, ${dashboardData.my_info.name}!"
                        tvRoomName.text = "Phòng: ${currentRoom?.name}"
                        tvRoomPrice.text = "Giá thuê: ${String.format(Locale.getDefault(), "%,.0fđ", currentRoom?.price)}"

                        if (currentBill != null) {
                            // TỰ TÍNH TỔNG TIỀN NGAY TẠI ĐÂY CHO CHẮC CHẮN
                            val tienDien = ((currentBill!!.new_electric ?: 0) - (currentBill!!.old_electric ?: 0)) * 3500.0
                            val tienNuoc = ((currentBill!!.new_water ?: 0) - (currentBill!!.old_water ?: 0)) * 15000.0
                            val tongTien = (currentRoom?.price ?: 0.0) + tienDien + tienNuoc

                            tvBillStatus.text = "Có hóa đơn tháng này: ${String.format(Locale.getDefault(), "%,.0fđ", tongTien)}"
                            tvBillStatus.setTextColor(ContextCompat.getColor(this@TenantDashboardActivity, android.R.color.holo_red_dark))

                            btnSplitBill.isEnabled = true
                            btnPayMoMo.visibility = View.VISIBLE
                        } else {
                            tvBillStatus.text = "Chưa có hóa đơn"
                            tvBillStatus.setTextColor(ContextCompat.getColor(this@TenantDashboardActivity, android.R.color.darker_gray))

                            btnSplitBill.isEnabled = false
                            btnPayMoMo.visibility = View.GONE
                        }
                    }
                } else {
                    // Bắt lỗi cực mạnh: Hiển thị luôn Server chửi gì lên màn hình
                    val errorMsg = response.errorBody()?.string() ?: "Lỗi không xác định"
                    Toast.makeText(this@TenantDashboardActivity, "Lỗi Server: Mã ${response.code()}", Toast.LENGTH_LONG).show()
                    Log.e("DASHBOARD_ERROR", errorMsg)
                    tvRoomName.text = "Lỗi kết nối Server"
                }
            }

            override fun onFailure(call: Call<TenantDashboardResponse>, t: Throwable) {
                Log.e("API_ERROR", "Sập mạng: ${t.message}")
                Toast.makeText(this@TenantDashboardActivity, "Lỗi mạng hoặc Server đang tắt!", Toast.LENGTH_SHORT).show()
                tvRoomName.text = "Mất mạng"
            }
        })
    }
}
