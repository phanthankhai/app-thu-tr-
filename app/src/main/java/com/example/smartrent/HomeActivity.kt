package com.example.smartrent

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerViewRooms: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 1. Ánh xạ RecyclerView và cấu hình hiển thị dạng hàng dọc (LinearLayout)
        recyclerViewRooms = findViewById(R.id.recyclerViewRooms)
        recyclerViewRooms.layoutManager = LinearLayoutManager(this)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Đặt tab mặc định được chọn là Phòng trọ
        bottomNavigationView.selectedItemId = R.id.nav_rooms

        val fabAddRoom = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAddRoom)
        fabAddRoom.setOnClickListener {
            val intent = Intent(this, AddRoomActivity::class.java)
            startActivity(intent)
        }

        // 1. Ánh xạ nút bấm từ XML sang Kotlin
        val btnGoToApprovePayments = findViewById<Button>(R.id.btnGoToApprovePayments)

        // 2. Cài đặt sự kiện: Bấm vào là mở màn hình Duyệt Tiền
        btnGoToApprovePayments.setOnClickListener {
            val intent = Intent(this, AdminApprovePaymentActivity::class.java)
            startActivity(intent)
        }

        // Bắt sự kiện khi bấm vào các tab
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_rooms -> {
                    // Đang ở màn hình phòng trọ nên không làm gì cả
                    true
                }
                R.id.nav_bills -> {
                    val intent = Intent(this, BillListActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_incidents -> {
                    val intent = Intent(this, AdminIncidentActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, ServiceListActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Khởi tạo tiêu đề cho các thẻ Dashboard
        setupDashboardTitles()

        findViewById<View>(R.id.btnLogout).setOnClickListener {
            executeLogoutFlow()
        }
    }

    private fun executeLogoutFlow() {
        val sharedPrefs = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE)
        val token = sharedPrefs.getString("AUTH_TOKEN", "") ?: ""

        if (token.isEmpty()) {
            clearLocalSessionAndRedirect()
            return
        }

        // 1. Gọi API báo cho Server hủy Token
        RetrofitClient.getInstance(this).logout(token).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                clearLocalSessionAndRedirect()
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                clearLocalSessionAndRedirect()
            }
        })
    }

    private fun clearLocalSessionAndRedirect() {
        // 2. Xóa sạch dữ liệu Token đã lưu trong SharedPreferences
        val sharedPrefs = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE)
        sharedPrefs.edit().remove("AUTH_TOKEN").apply()

        Toast.makeText(this, "Đã đăng xuất tài khoản", Toast.LENGTH_SHORT).show()

        // 3. Chuyển về màn hình Đăng nhập (MainActivity) và XÓA SẠCH bộ nhớ Stack các màn hình cũ
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun setupDashboardTitles() {
        findViewById<View>(R.id.cardEmptyRooms).findViewById<TextView>(R.id.tvCardTitle).text = "Phòng Trống"
        findViewById<View>(R.id.cardTotalTenants).findViewById<TextView>(R.id.tvCardTitle).text = "Tổng Khách"
        findViewById<View>(R.id.cardRevenue).findViewById<TextView>(R.id.tvCardTitle).text = "Doanh Thu"
        findViewById<View>(R.id.cardUnpaid).findViewById<TextView>(R.id.tvCardTitle).text = "Chưa Thu"
    }

    override fun onResume() {
        super.onResume()
        // Cứ mỗi khi màn hình Home được quay trở lại, lập tức gọi hàm tải lại danh sách và Dashboard
        loadRoomList()
        loadDashboardData()
    }

    private fun loadDashboardData() {
        val sharedPreferences = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE)
        val authToken = sharedPreferences.getString("AUTH_TOKEN", "") ?: ""
        
        if (authToken.isEmpty()) return

        RetrofitClient.getInstance(this).getDashboard(authToken).enqueue(object : Callback<DashboardResponse> {
            override fun onResponse(call: Call<DashboardResponse>, response: Response<DashboardResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    // Đổ dữ liệu vào các View thông qua View cha (include)
                    findViewById<View>(R.id.cardEmptyRooms).findViewById<TextView>(R.id.tvCardValue).text = data?.empty_rooms.toString()
                    findViewById<View>(R.id.cardTotalTenants).findViewById<TextView>(R.id.tvCardValue).text = data?.total_tenants.toString()
                    findViewById<View>(R.id.cardRevenue).findViewById<TextView>(R.id.tvCardValue).text = "${data?.current_month_revenue} đ"
                    findViewById<View>(R.id.cardUnpaid).findViewById<TextView>(R.id.tvCardValue).text = data?.unpaid_bills_count.toString()
                }
            }
            override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Lỗi tải Dashboard", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadRoomList() {
        // 2. Lấy Token đã lưu từ màn hình Login
        val sharedPreferences = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE)
        val authToken = sharedPreferences.getString("AUTH_TOKEN", "") ?: ""

        // Nếu không tìm thấy token (chưa đăng nhập hoặc lỗi), đá người dùng ra ngoài
        if (authToken.isEmpty()) {
            Toast.makeText(this, "Phiên làm việc hết hạn, vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // 3. Bắt đầu gọi API lấy danh sách phòng trọ
        RetrofitClient.getInstance(this).getRooms(authToken).enqueue(object : Callback<RoomResponse> {

            // Trường hợp Server phản hồi thành công
            override fun onResponse(call: Call<RoomResponse>, response: Response<RoomResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val roomResponse = response.body()

                    if (roomResponse?.success == true) {
                        // Lấy danh sách phòng từ cục dữ liệu data
                        val listRooms = roomResponse.data

                        // 4. Giao danh sách cho thợ Adapter và gắn vào RecyclerView để hiển thị lên màn hình
                        val adapter = RoomAdapter(listRooms) { selectedRoom ->
                            // Khi một phòng bị bấm, code trong khối này sẽ chạy!
                            val intent = Intent(this@HomeActivity, RoomDetailActivity::class.java)

                            // Đóng gói ID của phòng bị bấm để gửi sang màn hình Chi tiết
                            intent.putExtra("ROOM_ID", selectedRoom.id)
                            intent.putExtra("ROOM_NAME", selectedRoom.name)

                            startActivity(intent)
                        }
                        recyclerViewRooms.adapter = adapter
                    }
                } else {
                    Toast.makeText(this@HomeActivity, "Lấy danh sách phòng thất bại!", Toast.LENGTH_SHORT).show()
                    Log.e("API_ERROR", "Mã lỗi danh sách: ${response.code()}")
                }
            }

            // Trường hợp lỗi mạng, sập server...
            override fun onFailure(call: Call<RoomResponse>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Lỗi tải danh sách: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
