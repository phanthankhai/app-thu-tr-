package com.example.smartrent

import android.content.Context
import android.content.Intent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // QUAN TRỌNG: Cấu hình địa chỉ IP
    // - Nếu bạn chạy test bằng Máy ảo (Emulator) của Android Studio: Dùng "http://10.0.2.2:8000/api/v1/"
    // - Nếu bạn cắm cáp dùng điện thoại thật: Dùng IPv4 của máy tính, ví dụ: "http://192.168.1.x:8000/api/v1/"
    const val BASE_URL = "http://127.0.0.1:8081/api/v1/"
    private var retrofit: Retrofit? = null

    // Hàm khởi tạo nhận vào Context để có thể xử lý việc đá văng người dùng ra ngoài
    fun getInstance(context: Context): ApiService {
        if (retrofit == null) {
            val appContext = context.applicationContext

            // 1. Tạo Interceptor để lắng nghe mọi phản hồi từ Server
            val authInterceptor = Interceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)

                // BẮT LỖI 401 (Token hết hạn hoặc sai)
                if (response.code == 401) {
                    // Xóa token cũ dưới local
                    val sharedPrefs = appContext.getSharedPreferences("SmartRentPrefs", Context.MODE_PRIVATE)
                    sharedPrefs.edit().remove("AUTH_TOKEN").apply()

                    // Tạo Intent điều hướng về Login, xóa sạch lịch sử màn hình
                    val intent = Intent(appContext, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    appContext.startActivity(intent)
                }
                
                response // Trả về response bình thường cho các API xử lý tiếp
            }

            // 2. Bộ theo dõi lỗi (in ra Logcat)
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            // 3. Gắn các Interceptor vào OkHttpClient
            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(logging)
                .build()

            // 4. Khởi tạo Retrofit với client vừa tạo
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!.create(ApiService::class.java)
    }
}
