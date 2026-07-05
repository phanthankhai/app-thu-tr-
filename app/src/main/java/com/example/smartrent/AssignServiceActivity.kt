package com.example.smartrent

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AssignServiceActivity : AppCompatActivity() {

    private var roomId = -1
    private lateinit var llCheckboxContainer: LinearLayout
    private val checkBoxMap = mutableMapOf<Int, CheckBox>() // Lưu trữ ID dịch vụ và Checkbox tương ứng
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assign_service)

        // Nhận ID phòng từ màn hình trước truyền sang
        roomId = intent.getIntExtra("ROOM_ID", -1)
        token = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE).getString("AUTH_TOKEN", "") ?: ""

        val tvRoomInfo = findViewById<TextView>(R.id.tvRoomInfo)
        tvRoomInfo.text = "Cài đặt dịch vụ cho Phòng ID: $roomId"
        llCheckboxContainer = findViewById(R.id.llCheckboxContainer)

        if (roomId != -1) {
            loadAllServices()
        } else {
            Toast.makeText(this, "Lỗi: Không xác định được phòng!", Toast.LENGTH_SHORT).show()
            finish()
        }

        findViewById<Button>(R.id.btnSaveAssignedServices).setOnClickListener {
            saveSelectedServices()
        }
    }

    private fun loadAllServices() {
        // Gọi lại hàm lấy danh sách dịch vụ
        RetrofitClient.getInstance(this).getServices(token).enqueue(object : Callback<ServiceListResponse> {
            override fun onResponse(call: Call<ServiceListResponse>, response: Response<ServiceListResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val services = response.body()?.data ?: emptyList()
                    generateCheckboxes(services)
                }
            }
            override fun onFailure(call: Call<ServiceListResponse>, t: Throwable) {
                Toast.makeText(this@AssignServiceActivity, "Lỗi tải danh sách dịch vụ", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun generateCheckboxes(services: List<ServiceData>) {
        llCheckboxContainer.removeAllViews()
        checkBoxMap.clear()

        for (service in services) {
            val checkBox = CheckBox(this)
            checkBox.text = "${service.name} (${service.price} đ/${service.unit})"
            checkBox.textSize = 16f
            checkBox.setPadding(0, 16, 0, 16)
            
            // Tạm thời để trống
            
            llCheckboxContainer.addView(checkBox)
            checkBoxMap[service.id] = checkBox // Lưu map để lát lấy ID
        }
    }

    private fun saveSelectedServices() {
        // 1. Quét qua tất cả Checkbox, cái nào được tick (isChecked) thì lấy ID của nó cho vào list
        val selectedIds = mutableListOf<Int>()
        for ((id, checkBox) in checkBoxMap) {
            if (checkBox.isChecked) {
                selectedIds.add(id)
            }
        }

        // 2. Đóng gói dữ liệu gửi lên (Thành dạng {"service_ids": [3, 4]})
        val request = SyncServiceRequest(selectedIds)

        // 3. Gọi API
        RetrofitClient.getInstance(this).syncRoomServices(token, roomId, request).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AssignServiceActivity, "Cập nhật dịch vụ thành công!", Toast.LENGTH_LONG).show()
                    finish() // Quay về màn hình trước
                } else {
                    Toast.makeText(this@AssignServiceActivity, "Lỗi cập nhật", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(this@AssignServiceActivity, "Lỗi mạng", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
