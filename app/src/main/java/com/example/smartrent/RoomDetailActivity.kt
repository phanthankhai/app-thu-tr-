package com.example.smartrent

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.JsonObject
import org.json.JSONObject
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
    private lateinit var btnTerminateContract: Button
    private lateinit var progressBar: ProgressBar

    private var currentRoomId: Int = -1
    private var currentRoomName: String = ""
    private var authToken: String = ""
    private var userRole: String = "admin"
    private var currentRoom: Room? = null

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
        btnTerminateContract = findViewById(R.id.btnTerminateContractInRoom)
        progressBar = findViewById(R.id.progressBar)
        val btnAssignServices = findViewById<Button>(R.id.btnAssignServices)
        val btnReportIncident = findViewById<Button>(R.id.btnReportIncident)

        // 2. Lấy ID và Tên phòng được truyền từ danh sách bên ngoài sang
        currentRoomId = intent.getIntExtra("ROOM_ID", -1)
        currentRoomName = intent.getStringExtra("ROOM_NAME") ?: ""
        userRole = intent.getStringExtra("USER_ROLE") ?: "admin"
        
        Log.d("DEBUG_ROLE", "User role hiện tại là: $userRole")

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

        btnTerminateContract.setOnClickListener {
            val contract = currentRoom?.contracts?.getOrNull(0) ?: return@setOnClickListener
            if (userRole == "admin") {
                if (contract.status == "pending_termination") {
                    Toast.makeText(this, "Đang chờ khách thuê xác nhận!", Toast.LENGTH_SHORT).show()
                } else {
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Yêu cầu thanh lý")
                        .setMessage("Bạn chắc chắn muốn gửi yêu cầu thanh lý hợp đồng này?")
                        .setPositiveButton("Gửi") { _, _ -> requestContractTermination(contract.id) }
                        .setNegativeButton("Hủy", null)
                        .show()
                }
            } else {
                // Logic click cho Tenant
                if (contract.status == "pending_termination") {
                    if (contract.tenant_approved == 1) {
                        Toast.makeText(this, "Bạn đã xác nhận rồi!", Toast.LENGTH_SHORT).show()
                    } else {
                        showConfirmDialog(contract.id)
                    }
                }
            }
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
                    currentRoom = response.body()!!.data
                    displayRoomInfo(currentRoom!!)
                } else {
                    // SỬA CHỖ NÀY: In ra lỗi thật sự từ server
                    val errorMsg = response.errorBody()?.string()
                    Log.e("API_ERROR", "Lỗi trả về: $errorMsg")
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

            if (userRole == "admin") {
                btnTerminateContract.visibility = View.VISIBLE
                if (contract.status == "pending_termination") {
                    btnTerminateContract.text = "ĐANG CHỜ XÁC NHẬN..."
                    btnTerminateContract.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray))
                } else {
                    btnTerminateContract.text = "YÊU CẦU THANH LÝ"
                    btnTerminateContract.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.holo_red_dark))
                }
            } else {
                // Logic hiện nút cho Tenant
                if (contract.status == "pending_termination") {
                    btnTerminateContract.visibility = View.VISIBLE
                    btnTerminateContract.text = "XÁC NHẬN THANH LÝ"
                    btnTerminateContract.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.holo_red_dark))
                } else {
                    btnTerminateContract.visibility = View.GONE
                }
            }
        } else {
            tvContractInfo.text = "Chưa có hợp đồng"
            btnTerminateContract.visibility = View.GONE
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

    private fun showConfirmDialog(contractId: Int) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Xác nhận thanh lý")
            .setMessage("Bạn chắc chắn muốn xác nhận thanh lý hợp đồng này?")
            .setPositiveButton("Đồng ý") { _, _ -> confirmContractTermination(contractId) }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun requestContractTermination(contractId: Int) {
        progressBar.visibility = View.VISIBLE
        RetrofitClient.getInstance(this)
            .requestTerminateContract(authToken, contractId)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        Toast.makeText(this@RoomDetailActivity, "Đã gửi yêu cầu!", Toast.LENGTH_SHORT).show()
                        loadRoomDetail()
                    } else {
                        val errorMsg = try {
                            val errorJson = JSONObject(response.errorBody()?.string() ?: "{}")
                            errorJson.getString("message")
                        } catch (e: Exception) { "Gửi yêu cầu thất bại!" }
                        Toast.makeText(this@RoomDetailActivity, errorMsg, Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@RoomDetailActivity, "Lỗi mạng!", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun confirmContractTermination(contractId: Int) {
        progressBar.visibility = View.VISIBLE
        RetrofitClient.getInstance(this)
            .confirmTerminateContract(authToken, contractId)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        Toast.makeText(this@RoomDetailActivity, "Xác nhận thành công!", Toast.LENGTH_SHORT).show()
                        loadRoomDetail()
                    } else {
                        val errorMsg = try {
                            val errorJson = JSONObject(response.errorBody()?.string() ?: "{}")
                            errorJson.getString("message")
                        } catch (e: Exception) { "Xác nhận thất bại!" }
                        Toast.makeText(this@RoomDetailActivity, errorMsg, Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@RoomDetailActivity, "Lỗi mạng!", Toast.LENGTH_SHORT).show()
                }
            })
    }
}