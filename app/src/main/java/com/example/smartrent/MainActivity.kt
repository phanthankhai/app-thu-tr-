package com.example.smartrent

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Liên kết với file giao diện activity_main.xml
        setContentView(R.layout.activity_main)

        // 1. Ánh xạ các thành phần từ giao diện (XML) vào biến Kotlin
        val edtPhone = findViewById<EditText>(R.id.edtPhone)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // 2. Bắt sự kiện khi người dùng bấm nút Đăng nhập
        btnLogin.setOnClickListener {
            val phone = edtPhone.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            // Kiểm tra xem người dùng có nhập thiếu không
            if (phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập số điện thoại và mật khẩu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Dừng lại, không chạy tiếp code bên dưới
            }

            // 3. Đóng gói dữ liệu và gọi API
            val request = LoginRequest(phone, password)

            RetrofitClient.getInstance(this).login(request).enqueue(object : Callback<LoginResponse> {

                // Trường hợp 1: Có phản hồi từ Server (Thành công hoặc Báo lỗi sai pass)
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        // 1. Lấy Token từ Server trả về
                        val accessToken = loginResponse?.data?.authorization?.access_token

                        // 2. Lưu Token vào "két sắt" SharedPreferences của điện thoại
                        val sharedPreferences = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("AUTH_TOKEN", "Bearer $accessToken")
                        editor.apply()

                        Toast.makeText(this@MainActivity, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()

                        // 3. Kiểm tra role để chuyển màn hình tương ứng
                        val role = loginResponse?.data?.user?.role
                        if (role == "admin") {
                            // Chuyển sang màn hình Admin (Dashboard của chủ trọ)
                            val intent = Intent(this@MainActivity, HomeActivity::class.java)
                            startActivity(intent)
                        } else {
                            // Chuyển sang màn hình Khách thuê (Dashboard cá nhân)
                            val intent = Intent(this@MainActivity, TenantDashboardActivity::class.java)
                            startActivity(intent)
                        }

                        // 4. Đóng màn hình Login lại
                        finish()
                    } else {
                        // Đăng nhập sai (Lỗi 401 Unauthenticated)
                        Toast.makeText(this@MainActivity, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show()
                    }
                }

                // Trường hợp 2: Lỗi mạng, sập Server, sai IP...
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Lỗi kết nối: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}
