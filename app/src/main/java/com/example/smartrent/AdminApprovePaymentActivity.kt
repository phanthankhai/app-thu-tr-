package com.example.smartrent

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminApprovePaymentActivity : AppCompatActivity() {

    private lateinit var rvPayments: RecyclerView
    private lateinit var paymentAdapter: AdminPaymentAdapter
    private var authToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_approve_payment)

        // Lấy Token xác thực quyền Admin đã lưu lúc đăng nhập
        val sharedPrefs = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE)
        authToken = "Bearer " + sharedPrefs.getString("AUTH_TOKEN", "")

        rvPayments = findViewById(R.id.rvPendingPayments)
        rvPayments.layoutManager = LinearLayoutManager(this)

        // Khởi tạo adapter, truyền hàm xử lý khi click nút Duyệt
        paymentAdapter = AdminPaymentAdapter(emptyList()) { selectedPayment ->
            executeApproval(selectedPayment.id)
        }
        rvPayments.adapter = paymentAdapter

        // Tải danh sách giao dịch phòng gửi lên
        fetchPendingPayments()
    }

    private fun fetchPendingPayments() {
        RetrofitClient.getInstance(this).getPendingPayments(authToken)
            .enqueue(object : Callback<PaymentResponse> {
                override fun onResponse(call: Call<PaymentResponse>, response: Response<PaymentResponse>) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        val listData = response.body()?.data ?: emptyList()
                        paymentAdapter.updateList(listData)
                        if (listData.isEmpty()) {
                            Toast.makeText(this@AdminApprovePaymentActivity, "Không có khoản tiền nào cần duyệt!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@AdminApprovePaymentActivity, "Lấy dữ liệu thất bại!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {
                    Toast.makeText(this@AdminApprovePaymentActivity, "Lỗi mạng kết nối tới Server!", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun executeApproval(paymentId: Int) {
        RetrofitClient.getInstance(this).approvePayment(authToken, paymentId)
            .enqueue(object : Callback<BaseResponse> {
                override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        Toast.makeText(this@AdminApprovePaymentActivity, "Duyệt thành công hóa đơn lẻ này!", Toast.LENGTH_SHORT).show()
                        // Tải lại danh sách để tự động xóa dòng vừa duyệt đi
                        fetchPendingPayments()
                    } else {
                        Toast.makeText(this@AdminApprovePaymentActivity, "Duyệt lỗi hoặc không đủ thẩm quyền!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    Toast.makeText(this@AdminApprovePaymentActivity, "Gặp sự cố kết nối!", Toast.LENGTH_SHORT).show()
                }
            })
    }
}