package com.example.smartrent

import android.content.res.ColorStateList
import android.graphics.Color
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
    private var myUserId: Int = -1
    private var myAmountToPay: Double = 0.0
    private var myPaymentStatus: String? = null
    private lateinit var btnSubmitPayment: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_split_bill)

        val sharedPrefs = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE)
        token = "Bearer " + sharedPrefs.getString("AUTH_TOKEN", "")

        // 1. Nhận dữ liệu từ Intent
        billId = intent.getIntExtra("BILL_ID", -1)
        myUserId = intent.getIntExtra("MY_USER_ID", -1)
        myPaymentStatus = intent.getStringExtra("MY_PAYMENT_STATUS")
        totalRent = intent.getDoubleExtra("TOTAL_RENT", 0.0)
        totalElectric = intent.getDoubleExtra("TOTAL_ELECTRIC", 0.0)
        totalWater = intent.getDoubleExtra("TOTAL_WATER", 0.0)
        totalService = intent.getDoubleExtra("TOTAL_SERVICE", 0.0)
        
        // 2. HỨNG VÀ GIẢI MÃ DANH SÁCH THÀNH VIÊN THẬT
        val tenantsJson = intent.getStringExtra("TENANTS_LIST")
        if (!tenantsJson.isNullOrEmpty()) {
            val type = object : TypeToken<ArrayList<Roommate>>() {}.type
            roomMembers = Gson().fromJson(tenantsJson, type)
        }

        rvRoommates = findViewById(R.id.rvRoommates)
        tvResult = findViewById(R.id.tvResult)

        rvRoommates.layoutManager = LinearLayoutManager(this)
        
        adapter = RoommateAdapter(roomMembers) {
            calculateLiveSplit()
        }
        rvRoommates.adapter = adapter

        val rgPaymentMethod = findViewById<RadioGroup>(R.id.rgPaymentMethod)
        val rbTransfer = findViewById<RadioButton>(R.id.rbTransfer)
        btnSubmitPayment = findViewById<Button>(R.id.btnSubmitPayment)

        rbTransfer.isEnabled = false

        // Logic hiển thị nút bấm dựa trên trạng thái
        updateSubmitButtonUI()

        btnSubmitPayment.setOnClickListener {
            // Kiểm tra billId trước khi gửi
            if (billId == -1) {
                Toast.makeText(this, "Lỗi: Không tìm thấy ID hóa đơn!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedId = rgPaymentMethod.checkedRadioButtonId
            val method = if (selectedId == R.id.rbCash) "cash" else "transfer"
            
            // ĐÓNG GÓI SỐ TIỀN CỦA MÌNH VÀO REQUEST ĐỂ GỬI LÊN SERVER
            val requestBody = mapOf(
                "payment_method" to method,
                "amount" to myAmountToPay.toString() // Backend sẽ hứng biến amount này
            )

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

            // LỌC TIỀN CỦA CHÍNH MÌNH: Nếu member này chính là người đang cầm điện thoại
            if (member.id == myUserId) {
                myAmountToPay = memberTotal
                
                // Chỉ đổi chữ trên nút nếu chưa đóng hoặc chưa được duyệt
                if (myPaymentStatus != "approved" && myPaymentStatus != "pending") {
                    val fMyTotal = String.format(Locale.getDefault(), "%,.0fđ", myAmountToPay)
                    btnSubmitPayment.text = "Xác nhận đã gửi $fMyTotal cho Admin"
                }
            }

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

    private fun updateSubmitButtonUI() {
        when (myPaymentStatus) {
            "approved" -> {
                btnSubmitPayment.text = "✅ Admin đã nhận tiền của bạn!"
                btnSubmitPayment.isEnabled = false
                btnSubmitPayment.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
            }
            "pending" -> {
                btnSubmitPayment.text = "⏳ Đang chờ Admin duyệt tiền..."
                btnSubmitPayment.isEnabled = false
                btnSubmitPayment.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF9800"))
            }
            else -> {
                btnSubmitPayment.isEnabled = true
                btnSubmitPayment.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#2196F3"))
            }
        }
    }
}
