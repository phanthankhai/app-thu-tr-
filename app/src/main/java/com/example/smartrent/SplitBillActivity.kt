package com.example.smartrent

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class SplitBillActivity : AppCompatActivity() {

    private lateinit var rvRoommates: RecyclerView
    private lateinit var tvResult: TextView
    private lateinit var adapter: RoommateAdapter

    private var totalRent: Double = 0.0
    private var totalElectric: Double = 0.0
    private var totalWater: Double = 0.0
    private var totalService: Double = 0.0
    private var roomMembers: List<Roommate> = emptyList()
    private var token: String = ""
    private var billId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_split_bill)

        val sharedPrefs = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE)
        token = "Bearer " + sharedPrefs.getString("AUTH_TOKEN", "")

        // 1. Nhận dữ liệu từ Intent
        billId = intent.getIntExtra("BILL_ID", -1)
        totalRent = intent.getDoubleExtra("TOTAL_RENT", 0.0)
        
        // Hiện tại dashboard chỉ truyền gộp TOTAL_UTILITIES, tạm thời chia tỷ lệ 70:30 nếu không có data riêng
        val totalUtils = intent.getDoubleExtra("TOTAL_UTILITIES", 0.0)
        totalElectric = intent.getDoubleExtra("TOTAL_ELECTRIC", totalUtils * 0.7)
        totalWater = intent.getDoubleExtra("TOTAL_WATER", totalUtils * 0.3)
        totalService = intent.getDoubleExtra("TOTAL_SERVICE", 0.0)
        
        @Suppress("DEPRECATION")
        val receivedMembers = intent.getSerializableExtra("ROOM_MEMBERS") as? ArrayList<Roommate>
        roomMembers = receivedMembers ?: emptyList()

        rvRoommates = findViewById(R.id.rvRoommates)
        tvResult = findViewById(R.id.tvResult)

        rvRoommates.layoutManager = LinearLayoutManager(this)
        
        adapter = RoommateAdapter(roomMembers) {
            calculateLiveSplit()
        }
        rvRoommates.adapter = adapter

        val rgPaymentMethod = findViewById<RadioGroup>(R.id.rgPaymentMethod)
        val rbTransfer = findViewById<RadioButton>(R.id.rbTransfer)
        val btnSubmitPayment = findViewById<Button>(R.id.btnSubmitPayment)

        rbTransfer.isEnabled = false

        btnSubmitPayment.setOnClickListener {
            // Kiểm tra billId trước khi gửi
            if (billId == -1) {
                Toast.makeText(this, "Lỗi: Không tìm thấy ID hóa đơn!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedId = rgPaymentMethod.checkedRadioButtonId
            val method = if (selectedId == R.id.rbCash) "cash" else "transfer"
            val requestBody = mapOf("payment_method" to method)

            RetrofitClient.getInstance(this).tenantNotifyPayment(token, billId, requestBody)
                .enqueue(object : Callback<BaseResponse> {
                    override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@SplitBillActivity, "Gửi thành công! Chờ Admin duyệt.", Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            // Dùng Log để xem lỗi chi tiết từ Server
                            val error = response.errorBody()?.string()
                            Log.e("DEBUG_ERROR", "Server trả về: $error")
                            Toast.makeText(this@SplitBillActivity, "Lỗi Server: " + response.code(), Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                        Toast.makeText(this@SplitBillActivity, "Lỗi kết nối", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        // Tính toán lần đầu
        calculateLiveSplit()
    }

    private fun calculateLiveSplit() {
        val totalMembersCount = roomMembers.size
        if (totalMembersCount == 0) {
            tvResult.text = "Chưa có danh sách thành viên để chia tiền."
            return
        }

        // Đếm số người chịu từng khoản
        val electricPayersCount = roomMembers.count { it.isSharingElectric }
        val waterPayersCount = roomMembers.count { it.isSharingWater }
        val servicePayersCount = roomMembers.count { it.isSharingServices }

        // Tính đơn giá cho từng người
        val rentPerPerson = totalRent / totalMembersCount
        val electricPerPerson = if (electricPayersCount > 0) totalElectric / electricPayersCount else 0.0
        val waterPerPerson = if (waterPayersCount > 0) totalWater / waterPayersCount else 0.0
        val servicePerPerson = if (servicePayersCount > 0) totalService / servicePayersCount else 0.0

        val resultBuilder = StringBuilder("Hóa đơn chi tiết từng người:\n\n")

        for (member in roomMembers) {
            // Cộng dồn những khoản người này bị đánh dấu tick
            var memberTotal = rentPerPerson
            val eCost = if (member.isSharingElectric) electricPerPerson else 0.0
            val wCost = if (member.isSharingWater) waterPerPerson else 0.0
            val sCost = if (member.isSharingServices) servicePerPerson else 0.0
            
            memberTotal += (eCost + wCost + sCost)

            // Format tiền
            val fRent = String.format(Locale.getDefault(), "%,.0fđ", rentPerPerson)
            val fElec = String.format(Locale.getDefault(), "%,.0fđ", eCost)
            val fWater = String.format(Locale.getDefault(), "%,.0fđ", wCost)
            val fServ = String.format(Locale.getDefault(), "%,.0fđ", sCost)
            val fTotal = String.format(Locale.getDefault(), "%,.0fđ", memberTotal)
            
            resultBuilder.append("👤 ${member.name}:\n")
            resultBuilder.append("   • Tiền phòng (Cố định): $fRent\n")
            
            if (member.isSharingElectric) resultBuilder.append("   • Tiền điện: $fElec\n") 
            else resultBuilder.append("   • Tiền điện: 0đ (Miễn trừ)\n")
            
            if (member.isSharingWater) resultBuilder.append("   • Tiền nước: $fWater\n") 
            else resultBuilder.append("   • Tiền nước: 0đ (Miễn trừ)\n")
            
            if (member.isSharingServices) resultBuilder.append("   • Dịch vụ: $fServ\n") 
            else resultBuilder.append("   • Dịch vụ: 0đ (Miễn trừ)\n")
            
            resultBuilder.append("   => TỔNG CỘNG: $fTotal\n\n")
        }

        tvResult.text = resultBuilder.toString()
    }
}
