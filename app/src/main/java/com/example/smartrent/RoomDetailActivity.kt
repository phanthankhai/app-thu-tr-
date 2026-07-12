package com.example.smartrent

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomDetailActivity : AppCompatActivity() {

    private lateinit var tvDetailRoomName: TextView
    private lateinit var tvDetailStatus: TextView
    private lateinit var tvDetailPrice: TextView
    private lateinit var tvDetailArea: TextView
    private lateinit var tvDetailElectricity: TextView
    private lateinit var tvDetailWater: TextView
    private lateinit var ivRoomImage: ImageView
    private lateinit var tvContractInfo: TextView
    private lateinit var llTenantsContainer: LinearLayout
    private lateinit var btnAddMember: Button
    private lateinit var btnCreateContract: Button
    private lateinit var btnCreateBill: Button

    private var currentRoomId: Int = -1
    private var currentRoomName: String = ""
    private var authToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_detail)

        // 1. Ánh xạ toàn bộ các View
        val btnBack = findViewById<TextView>(R.id.btnBack)
        tvDetailRoomName = findViewById(R.id.tvDetailRoomName)
        tvDetailStatus = findViewById(R.id.tvDetailStatus)
        tvDetailPrice = findViewById(R.id.tvDetailPrice)
        tvDetailArea = findViewById(R.id.tvDetailArea)
        tvDetailElectricity = findViewById(R.id.tvDetailElectricity)
        tvDetailWater = findViewById(R.id.tvDetailWater)
        ivRoomImage = findViewById(R.id.ivRoomImage)
        tvContractInfo = findViewById(R.id.tvContractInfo)
        llTenantsContainer = findViewById(R.id.llTenantsContainer)
        btnAddMember = findViewById(R.id.btnAddMember)
        btnCreateContract = findViewById(R.id.btnCreateContract)
        btnCreateBill = findViewById(R.id.btnCreateBill)
        val btnAssignServices = findViewById<Button>(R.id.btnAssignServices)
        val btnReportIncident = findViewById<Button>(R.id.btnReportIncident)

        // 2. Lấy ID và Tên phòng được truyền từ danh sách bên ngoài sang
        currentRoomId = intent.getIntExtra("ROOM_ID", -1)
        currentRoomName = intent.getStringExtra("ROOM_NAME") ?: ""

        // Hiển thị trước tên phòng lên tiêu đề trong lúc chờ tải dữ liệu từ mạng
        tvDetailRoomName.text = "Phòng $currentRoomName"

        // 3. Lấy chìa khóa Token từ két sắt SharedPreferences
        val sharedPreferences = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("AUTH_TOKEN", "") ?: ""
        authToken = token

        // 4. Cài đặt sự kiện nút bấm
        btnBack.setOnClickListener { finish() }

        btnCreateContract.setOnClickListener {
            val intent = Intent(this, CreateContractActivity::class.java)
            intent.putExtra("ROOM_ID", currentRoomId)
            startActivity(intent)
        }

        btnAssignServices.setOnClickListener {
            val intent = Intent(this, AssignServiceActivity::class.java)
            intent.putExtra("ROOM_ID", currentRoomId)
            startActivity(intent)
        }

        btnCreateBill.setOnClickListener {
            val intent = Intent(this, AdminCreateBillActivity::class.java).apply {
                putExtra("ROOM_ID", currentRoomId)
                putExtra("ROOM_NAME", currentRoomName)
            }
            startActivity(intent)
        }

        btnAddMember.setOnClickListener {
            showAddMemberDialog()
        }

        btnReportIncident.setOnClickListener {
            Toast.makeText(this, "Chức năng Báo cáo sự cố đang phát triển...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (currentRoomId != -1 && authToken.isNotEmpty()) {
            loadRoomDetail()
        }
    }

    private fun loadRoomDetail() {
        RetrofitClient.getInstance(this).getRoomDetail(authToken, currentRoomId).enqueue(object : Callback<RoomDetailResponse> {
            override fun onResponse(call: Call<RoomDetailResponse>, response: Response<RoomDetailResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val room = response.body()!!.data
                    displayRoomInfo(room)
                } else {
                    // SỬA CHỖ NÀY: In ra lỗi thật sự từ server
                    val errorMsg = response.errorBody()?.string()
                    android.util.Log.e("API_ERROR", "Lỗi trả về: $errorMsg")
                    Toast.makeText(this@RoomDetailActivity, "Không tìm thấy phòng: $errorMsg", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RoomDetailResponse>, t: Throwable) {
                Toast.makeText(this@RoomDetailActivity, "Lỗi mạng: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun displayRoomInfo(room: Room) {
        // Tải ảnh bằng Glide
        if (!room.image.isNullOrEmpty()) {
            Glide.with(this)
                .load(room.image)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_notify_error)
                .into(ivRoomImage)
        } else {
            ivRoomImage.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        tvDetailPrice.text = "Giá thuê phòng: ${room.price} đ/tháng"
        tvDetailArea.text = "Diện tích phòng: ${room.area} m²"

        // Đồng bộ và chuyển đổi trạng thái
        when (room.status) {
            "empty", "available" -> {
                tvDetailStatus.text = "Trạng thái: Còn trống"
                tvDetailStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
                btnCreateContract.visibility = View.VISIBLE
                btnAddMember.visibility = View.GONE
                btnCreateBill.visibility = View.GONE
            }
            "occupied", "rented" -> {
                tvDetailStatus.text = "Trạng thái: Đã có người thuê"
                tvDetailStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
                btnCreateContract.visibility = View.GONE
                btnAddMember.visibility = View.VISIBLE
                btnCreateBill.visibility = View.VISIBLE
            }
            "maintenance" -> {
                tvDetailStatus.text = "Trạng thái: Đang bảo trì"
                tvDetailStatus.setTextColor(Color.parseColor("#FF9800"))
                btnCreateContract.visibility = View.GONE
                btnAddMember.visibility = View.GONE
                btnCreateBill.visibility = View.GONE
            }
            else -> {
                tvDetailStatus.text = "Trạng thái: Không xác định (${room.status})"
                tvDetailStatus.setTextColor(Color.GRAY)
                btnCreateContract.visibility = View.VISIBLE // Mặc định hiện để an toàn
                btnAddMember.visibility = View.GONE
                btnCreateBill.visibility = View.GONE
            }
        }

        // THÔNG TIN HỢP ĐỒNG (Dùng contracts)
        if (!room.contracts.isNullOrEmpty()) {
            val contract = room.contracts[0]
            tvContractInfo.text = "Người đại diện: ${contract.tenant_name}\nSĐT: ${contract.tenant_phone}"
        } else {
            tvContractInfo.text = "Chưa có hợp đồng"
        }

        // THÀNH VIÊN (Dùng users)
        llTenantsContainer.removeAllViews()
        if (!room.users.isNullOrEmpty()) {
            for (user in room.users) {
                val tvMember = TextView(this)
                tvMember.text = "• ${user.name} - ${user.phone}"
                llTenantsContainer.addView(tvMember)
            }
        } else {
            val tvNoMember = TextView(this)
            tvNoMember.text = "(Chưa có thành viên)"
            tvNoMember.setTextColor(Color.GRAY)
            tvNoMember.textSize = 14f
            llTenantsContainer.addView(tvNoMember)
        }

        tvDetailElectricity.text = "Đơn giá điện: 3.500 đ/kWh"
        tvDetailWater.text = "Đơn giá nước: 15.000 đ/m³"
    }

    private fun showAddMemberDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_member, null)
        val etName = dialogView.findViewById<EditText>(R.id.etMemberName)
        val etPhone = dialogView.findViewById<EditText>(R.id.etMemberPhone)

        AlertDialog.Builder(this)
            .setTitle("Thêm thành viên mới")
            .setView(dialogView)
            .setPositiveButton("Thêm") { _, _ ->
                val name = etName.text.toString().trim()
                val phone = etPhone.text.toString().trim()

                if (name.isNotEmpty() && phone.isNotEmpty()) {
                    executeAddMember(name, phone)
                } else {
                    Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun executeAddMember(name: String, phone: String) {
        val request = AddTenantRequest(currentRoomId, name, phone)
        RetrofitClient.getInstance(this).addCoTenant(authToken, request).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@RoomDetailActivity, "Thêm thành viên thành công!", Toast.LENGTH_SHORT).show()
                    loadRoomDetail() // Tải lại dữ liệu
                } else {
                    Toast.makeText(this@RoomDetailActivity, "Lỗi: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Toast.makeText(this@RoomDetailActivity, "Lỗi kết nối!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}